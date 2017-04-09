/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.ly.object.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.luoying.data.define.CountObject;
import name.huliqing.ly.data.ChatData;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.ly.view.Footer;
import name.huliqing.ly.view.transfer.SimpleTransferPanel;
import name.huliqing.ly.view.transfer.TabTransferPanel;
import name.huliqing.ly.view.transfer.TransferPanel;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.item.Item;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.transfer.TransferData;
import name.huliqing.luoying.ui.Button;
import name.huliqing.luoying.ui.FrameLayout;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.Window;
import name.huliqing.ly.layer.network.ChatNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 * 出售物品到商店类角色
 * @author huliqing
 */
public class SendChat extends Chat {
    private final GameService gameService = Factory.get(GameService.class);
    private final ChatNetwork chatNetwork = Factory.get(ChatNetwork.class);

    // ---- inner
    private Window win;
    private LinearLayout bodyPanel;
    private TransferPanel sourcePanel;  // 玩家物品面板,显示角色已经有的物品
    private TransferPanel distPanel;    // 等发送物品面板
    private CenterPanel centerPanel;    // 分隔线
    private FooterPanel footerPanel;    // 显示确认按钮
    
    // 发送者角色，一般为Player.
    private Entity sender;
    
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
                scene.removeEntity(SendChat.this);
            }
        });
        
        win.addView(bodyPanel);
        win.addView(footerPanel);
        win.setToCorner(UI.Corner.CC);
        return win;
    }

    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene); 
        
        // 记住卖者
        sender = gameService.getPlayer();
        
        // 初始化, 数据要复制一份出来，不要去影响角色的包裹中的数据
        List<ObjectData> tempDatas = new ArrayList<ObjectData>();
        List<ItemData> items = sender.getData().getObjectDatas(ItemData.class, null);
        if (items != null) {
            tempDatas.addAll(items);
        }
        List<SkinData> skins = sender.getData().getObjectDatas(SkinData.class, null);
        if (skins != null) {
            tempDatas.addAll(skins);
        }
        // 移除不能出售的物品
        Iterator<ObjectData> it = tempDatas.iterator();
        while (it.hasNext()) {
            ObjectData dp = it.next();
            if (dp instanceof Item) {
                Item item = (Item) dp;
                if (!item.getData().isSellable()) {
                    it.remove();
                    continue;
                }
            }
            if (dp instanceof SkinData) {
                SkinData skin = (SkinData) dp;
                if (skin.isUsed() || skin.isBaseSkin()) {
                    it.remove();
                    continue;
                }
            }
        }
        
        List<TransferData> transforDatas = new ArrayList<TransferData>(tempDatas.size());
        for (ObjectData objectData : tempDatas) {
            TransferData td = new TransferData();
            td.setObjectData(objectData);
            if (objectData instanceof CountObject) {
                td.setAmount(((CountObject) objectData).getTotal());
            } else {
                td.setAmount(1);
            }
            transforDatas.add(td);
        }
        sourcePanel.setDatas(transforDatas);
        
        // 清空dist面板
        distPanel.setDatas(Collections.EMPTY_LIST);
        
        win.setTitle(getChatName() + "-" + gameService.getName(actor));
    }
    
    // 给目标发送物品
    private void send() {
        List<TransferData> datas = distPanel.getDatas();
        if (datas.isEmpty()) {
            scene.removeEntity(this);
            return;
        }
        TransferData pd;
        for (int i = 0; i < datas.size(); i++) {
            pd = datas.get(i);
            chatNetwork.chatSend(sender, actor, pd.getObjectData().getUniqueId(), pd.getAmount());
        }
        scene.removeEntity(this);
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
