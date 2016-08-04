/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.chat;

import com.jme3.app.Application;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.data.ChatData;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.mvc.network.UserCommandNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.ItemService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.SkinService;
import name.huliqing.core.view.Footer;
import name.huliqing.core.view.transfer.SimpleTransferPanel;
import name.huliqing.core.view.transfer.TabTransferPanel;
import name.huliqing.core.view.transfer.TransferPanel;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.ui.Button;
import name.huliqing.core.ui.FrameLayout;
import name.huliqing.core.ui.LinearLayout;
import name.huliqing.core.ui.UI;
import name.huliqing.core.ui.UIFactory;
import name.huliqing.core.ui.Window;

/**
 * 出售物品到商店类角色
 * @author huliqing
 * @param <T>
 */
public class SendChat<T extends ChatData> extends Chat<T> {
    private final ItemService itemService = Factory.get(ItemService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkinService skinService = Factory.get(SkinService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final UserCommandNetwork userCommandNetwork = Factory.get(UserCommandNetwork.class);

    // ---- inner
    private Window win;
    private LinearLayout bodyPanel;
    private TransferPanel sourcePanel;  // 玩家物品面板,显示角色已经有的物品
    private TransferPanel distPanel;    // 等发送物品面板
    private CenterPanel centerPanel;    // 分隔线
    private FooterPanel footerPanel;    // 显示确认按钮
    
    // 发送者角色，一般为Player.
    private Actor sender;
    
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
                playService.removeObject(SendChat.this);
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
        sender = playService.getPlayer();
        
        // 初始化, 数据要复制一份出来，不要去影响角色的包裹中的数据
        List<ObjectData> items = itemService.getItems(sender, null);
        List<ObjectData> transferDatas = new ArrayList<ObjectData>(items.size());
        for (ObjectData item : items) {
            // 非卖品
            if (!itemService.isSellable(item)) {
                continue;
            }
            ObjectData dataCopy = DataFactory.createData(item.getId());
            dataCopy.setTotal(item.getTotal());
            transferDatas.add(dataCopy);
        }
        sourcePanel.setDatas(transferDatas);
        
        // 清空dist面板
        distPanel.setDatas(Collections.EMPTY_LIST);
        
        win.setTitle(getChatName() + "-" + actorService.getName(actor));
    }
    
    // 给目标发送物品
    private void send() {
        List<ObjectData> datas = distPanel.getDatas();
        if (datas.isEmpty()) {
            playService.removeObject(this);
            return;
        }
        String[] items = new String[datas.size()];
        int[] counts = new int[datas.size()];
        ObjectData pd;
        for (int i = 0; i < datas.size(); i++) {
            pd = datas.get(i);
            items[i] = pd.getId();
            counts[i] = pd.getTotal();
        }
        userCommandNetwork.chatSend(sender, actor, items, counts);
        playService.removeObject(this);
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

        private Button submit;
        
        public FooterPanel(float width, float height) {
            super(width, height);
            submit = new Button(ResourceManager.get(ResConstants.COMMON_SEND));
            submit.addClickListener(new Listener() {
                @Override
                public void onClick(UI view, boolean isPressed) {
                    if (isPressed) return;
                    send();
                }
            });
            setPadding(20, 0, 20, 0);
            addView(submit);
        }

        @Override
        protected void updateViewChildren() {
            super.updateViewChildren();
            submit.setWidth(UIFactory.getUIConfig().getButtonWidth());
            submit.setHeight(UIFactory.getUIConfig().getButtonHeight());
        }

        @Override
        protected void updateViewLayout() {
            super.updateViewLayout(); 
            submit.setToCorner(Corner.RC);
        }
        
    }
    
}
