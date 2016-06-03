/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.chat;

import com.jme3.font.BitmapFont;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.InterfaceConstants;
import name.huliqing.fighter.constants.ResConstants;
import name.huliqing.fighter.data.ChatData;
import name.huliqing.fighter.object.DataLoaderFactory;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.game.network.UserCommandNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.ItemService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.SkinService;
import name.huliqing.fighter.game.view.tiles.Footer;
import name.huliqing.fighter.game.view.transfer.SimpleTransferPanel;
import name.huliqing.fighter.game.view.transfer.TabTransferPanel;
import name.huliqing.fighter.game.view.transfer.TransferPanel;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.ui.Button;
import name.huliqing.fighter.ui.FrameLayout;
import name.huliqing.fighter.ui.Icon;
import name.huliqing.fighter.ui.LinearLayout;
import name.huliqing.fighter.ui.Text;
import name.huliqing.fighter.ui.UI;
import name.huliqing.fighter.ui.UIFactory;
import name.huliqing.fighter.ui.Window;
import name.huliqing.fighter.utils.MathUtils;

/**
 * 出售物品到商店类角色
 * @author huliqing
 */
public class SellChat extends Chat {
    private final ItemService itemService = Factory.get(ItemService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkinService skinService = Factory.get(SkinService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final UserCommandNetwork userCommandNetwork = Factory.get(UserCommandNetwork.class);

    // 物品售出时的折扣,取值0.0~1.0, 当玩家卖给NPC时会打折扣
    private float discount = 1f;
    
    // ---- inner
    private boolean init;
    private Window win;
    private LinearLayout bodyPanel;
    private TransferPanel sourcePanel;  // 玩家物品面板,显示角色已经有的物品
    private TransferPanel distPanel;    // 出售面板，展示要出售的物品
    private CenterPanel centerPanel;    // 分隔线
    private FooterPanel footerPanel;   // 显示售出的金额及确认按钮
    
    // 出售者角色，一般为Player.
    private Actor seller;
    
    public SellChat(ChatData data) {
        super(data);
        discount = data.getAsFloat("discount", discount);
        
        // remove20160323,不要在这里初始化UI，只要在initialize()时再初始化即可。
    }

    @Override
    protected UI createChatUI(float width, float height) {
        float titleHeight = UIFactory.getUIConfig().getTitleHeight();
        float footerHeight = height * 0.15f;
        float bodyHeight = height - titleHeight - footerHeight;
        float ppWidth = width * 0.49f;
        float cpWidth = width * 0.02f;
        float spWidth = width * 0.49f;
        
        bodyPanel = new LinearLayout(width, bodyHeight);
        sourcePanel = new TabTransferPanel(ppWidth, bodyHeight);
        centerPanel = new CenterPanel(cpWidth, bodyHeight);
        distPanel = new SimpleTransferPanel(spWidth, bodyHeight);
        
        // 双向传输
        sourcePanel.setTransfer(distPanel);
        distPanel.setTransfer(sourcePanel);
        
        bodyPanel.setLayout(LinearLayout.Layout.horizontal);
        bodyPanel.addView(sourcePanel);
        bodyPanel.addView(centerPanel);
        bodyPanel.addView(distPanel);
        
        footerPanel = new FooterPanel(width, footerHeight);
        
        win = new Window("", width, height);
        win.setCloseable(true);
        win.setDragEnabled(true);
        win.addCloseListener(new Window.CloseListener() {
            @Override
            public void onClosed(Window win) {
                playService.removeObject(SellChat.this);
            }
        });
        
        win.addView(bodyPanel);
        win.addView(footerPanel);
        win.setToCorner(UI.Corner.CC);
        return win;
    }
    

    @Override
    public void initialize() {
        super.initialize();
        
        // 记住卖者
        seller = playService.getPlayer();
        
        // 初始化, 数据要复制一份出来，不要去影响角色的包裹中的数据
        List<ProtoData> items = itemService.getItems(seller, null);
        List<ProtoData> transferDatas = new ArrayList<ProtoData>(items.size());
        for (ProtoData item : items) {
            // 非卖品
            if (!itemService.isSellable(item)) {
                continue;
            }
            ProtoData dataCopy = DataLoaderFactory.createData(item.getId());
            dataCopy.setTotal(item.getTotal());
            transferDatas.add(dataCopy);
        }
        sourcePanel.setDatas(transferDatas);
        
        // 清空dist面板
        distPanel.setDatas(Collections.EMPTY_LIST);
        
        win.setTitle(getChatName() + "-" + actorService.getName(actor));
    }
    
    // 结算出售的金额
    private void billing() {
        List<ProtoData> datas = distPanel.getDatas();
        if (datas.isEmpty())
            return;
        String[] items = new String[datas.size()];
        int[] counts = new int[datas.size()];
        ProtoData data;
        for (int i = 0; i < datas.size(); i++) {
            data = datas.get(i);
            items[i] = data.getId();
            counts[i] = data.getTotal();
        }
        userCommandNetwork.chatSell(seller, actor, items, counts, discount);
        // 确认后退出窗口
        playService.removeObject(this);
    }
    
    // 估算价钱,最终价钱由接口计算决定。因为在确认“结算”之前玩家的包裹物品可能
    // 发生变化，并不能确保所有在"distPanel"窗口中的物品及数量都能准确售出。
    private int assess() {
        List<ProtoData> datas = distPanel.getDatas();
        if (datas.isEmpty())
            return 0;
        float total = 0;
        for (ProtoData pd : datas) {
            total += pd.getCost() * pd.getTotal();
        }
        total *= discount;
        return (int) total;
    }
    
    // -------------------------------------------------------------Center Panel
    private class CenterPanel extends FrameLayout {
        public CenterPanel(float width, float height) {
            super(width, height);
            setBackground(UIFactory.getUIConfig().getBackground(), false);
            setBackgroundColor(UIFactory.getUIConfig().getFooterBgColor(), false);
        }
    }
    
    // -------------------------------------------------------------Footer Panel
    private class FooterPanel extends Footer {

        private Text discountText;  // 折扣说明
        private Icon gold;  // 金币
        private Text assessText;  // “物品估价：”
        
        private Button submit;
        
        public FooterPanel(float width, float height) {
            super(width, height);
            discountText = new Text(ResourceManager.get(ResConstants.CHAT_SHOP_DISCOUNT) 
                    + ":" + MathUtils.format(discount * 100, "#") + "%");
            discountText.setVerticalAlignment(BitmapFont.VAlign.Center);
            
            assessText = new Text("");
            assessText.setVerticalAlignment(BitmapFont.VAlign.Center);
            assessText.setMargin(10, 0, 0, 0);
            
            gold = new Icon(InterfaceConstants.ITEM_GOLD);
            
            submit = new Button(ResourceManager.get(ResConstants.CHAT_SHOP_CONFIRM_SELL));
            submit.addClickListener(new Listener() {
                @Override
                public void onClick(UI view, boolean isPressed) {
                    if (isPressed) return;
                    billing();
                }
            });
            setPadding(20, 0, 20, 0);
            setLayout(Layout.horizontal);
            addView(discountText);
            addView(gold);
            addView(assessText);
            addView(submit);
        }

        @Override
        protected void updateViewChildren() {
            super.updateViewChildren();
            float cw = getContentWidth();
            
            float btnWidth = UIFactory.getUIConfig().getButtonWidth();
            float iconWidth = height * 0.5f;
            float textWidth = (cw - btnWidth - iconWidth) * 0.5f;
            
            discountText.setWidth(textWidth);
            discountText.setHeight(height);
            
            gold.setWidth(iconWidth);
            gold.setHeight(iconWidth);
            gold.setMargin(0, height * 0.25f, 0, 0);
            
            assessText.setText(ResourceManager.get(ResConstants.CHAT_SHOP_ASSESS) + ": " + assess());
            assessText.setWidth(textWidth);
            assessText.setHeight(height);
            
            submit.setWidth(btnWidth);
            submit.setHeight(UIFactory.getUIConfig().getButtonHeight());
        }

        @Override
        protected void updateViewLayout() {
            super.updateViewLayout(); 
            submit.setToCorner(Corner.RC);
        }
        
    }
    
}
