/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.chat;

import com.jme3.app.Application;
import com.jme3.font.BitmapFont;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.InterfaceConstants;
import name.huliqing.luoying.constants.ResConstants;
import name.huliqing.luoying.data.ChatData;
import name.huliqing.luoying.data.ObjectData;
import name.huliqing.luoying.data.define.CostObject;
//import name.huliqing.luoying.network.UserCommandNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.ItemService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.SkinService;
import name.huliqing.luoying.view.Footer;
import name.huliqing.luoying.view.transfer.SimpleTransferPanel;
import name.huliqing.luoying.view.transfer.TabTransferPanel;
import name.huliqing.luoying.view.transfer.TransferPanel;
import name.huliqing.luoying.manager.ResourceManager;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.item.Item;
import name.huliqing.luoying.object.skin.Skin;
import name.huliqing.luoying.ui.Button;
import name.huliqing.luoying.ui.FrameLayout;
import name.huliqing.luoying.ui.Icon;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.Window;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * 出售物品到商店类角色
 * @author huliqing
 * @param <T>
 */
public class SellChat<T extends ChatData> extends Chat<T> {
//    private final ProtoService protoService = Factory.get(ProtoService.class);
    private final ItemService itemService = Factory.get(ItemService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkinService skinService = Factory.get(SkinService.class);
    private final PlayService playService = Factory.get(PlayService.class);
//    private final UserCommandNetwork userCommandNetwork = Factory.get(UserCommandNetwork.class);

    // 物品售出时的折扣,取值0.0~1.0, 当玩家卖给NPC时会打折扣
    private float discount = 1f;
    
    // ---- inner
    private Window win;
    private LinearLayout bodyPanel;
    private TransferPanel<DataProcessor<ObjectData>> sourcePanel;  // 玩家物品面板,显示角色已经有的物品
    private TransferPanel<DataProcessor<ObjectData>> distPanel;    // 出售面板，展示要出售的物品
    private CenterPanel centerPanel;    // 分隔线
    private FooterPanel footerPanel;   // 显示售出的金额及确认按钮
    
    // 出售者角色，一般为Player.
    private Actor seller;

    @Override
    public void setData(T data) {
        super.setData(data); 
        discount = data.getAsFloat("discount", discount);
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
    public void initialize(Application app) {
        super.initialize(app);
        
        // 记住卖者
        seller = playService.getPlayer();
        
        // 初始化, 数据要复制一份出来，不要去影响角色的包裹中的数据
        List<DataProcessor> tempDatas = new ArrayList<DataProcessor>();
        List<Item> items = itemService.getItems(seller);
        if (items != null) {
            tempDatas.addAll(items);
        }
        List<Skin> skins = skinService.getSkins(seller);
        if (skins != null) {
            tempDatas.addAll(skins);
        }
        // 移除不能出售的物品
        Iterator<DataProcessor> it = tempDatas.iterator();
        while (it.hasNext()) {
            DataProcessor dp = it.next();
            if (dp instanceof Item) {
                Item item = (Item) dp;
                if (!item.getData().isSellable()) {
                    it.remove();
                    continue;
                }
            }
            if (dp instanceof Skin) {
                Skin skin = (Skin) dp;
                if (skin.isAttached() || skin.isBaseSkin() || skin.isSkinning()) {
                    it.remove();
                    continue;
                }
            }
        }
        
        List<DataProcessor<ObjectData>> transforDatas = new ArrayList<DataProcessor<ObjectData>>();
        for (DataProcessor dp : tempDatas) {
            ObjectData dataCopy = DataFactory.createData(dp.getData().getId());
            dataCopy.setTotal(((ObjectData)dp.getData()).getTotal());
            transforDatas.add(Loader.load(dataCopy));
        }
        sourcePanel.setDatas(transforDatas);
        
        // 清空dist面板
        distPanel.setDatas(Collections.EMPTY_LIST);
        
        win.setTitle(getChatName() + "-" + actorService.getName(actor));
    }
    
    // 结算出售的金额
    private void billing() {
        List<DataProcessor<ObjectData>> datas = distPanel.getDatas();
        if (datas.isEmpty())
            return;
        String[] items = new String[datas.size()];
        int[] counts = new int[datas.size()];
        DataProcessor<ObjectData> tempObj;
        for (int i = 0; i < datas.size(); i++) {
            tempObj = datas.get(i);
            items[i] = tempObj.getData().getId();
            counts[i] = tempObj.getData().getTotal();
        }
        userCommandNetwork.chatSell(seller, actor, items, counts, discount);
        // 确认后退出窗口
        playService.removeObject(this);
    }
    
    // 估算价钱,最终价钱由接口计算决定。因为在确认“结算”之前玩家的包裹物品可能
    // 发生变化，并不能确保所有在"distPanel"窗口中的物品及数量都能准确售出。
    private int assess() {
        List<DataProcessor<ObjectData>> datas = distPanel.getDatas();
        if (datas.isEmpty())
            return 0;
        float total = 0;
        for (DataProcessor<ObjectData> pd : datas) {
            if (!(pd.getData() instanceof CostObject)) {
                continue;
            }
            total += ((CostObject) pd.getData()).getCost() * pd.getData().getTotal();
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
