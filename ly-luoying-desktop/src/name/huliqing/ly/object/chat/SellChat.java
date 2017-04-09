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

import com.jme3.font.BitmapFont;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.ly.constants.InterfaceConstants;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.luoying.data.define.CountObject;
import name.huliqing.luoying.data.define.TradeInfo;
import name.huliqing.ly.data.ChatData;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.ly.view.Footer;
import name.huliqing.ly.view.transfer.SimpleTransferPanel;
import name.huliqing.ly.view.transfer.TabTransferPanel;
import name.huliqing.ly.view.transfer.TransferPanel;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.ui.Button;
import name.huliqing.luoying.ui.FrameLayout;
import name.huliqing.luoying.ui.Icon;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.Window;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.ly.layer.network.ChatNetwork;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.luoying.data.define.TradeObject;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.transfer.TransferData;
import name.huliqing.ly.constants.IdConstants;

/**
 * 出售物品到商店类角色
 * @author huliqing
 */
public class SellChat extends Chat {
    private final GameService gameService = Factory.get(GameService.class);
    private final ChatNetwork chatNetwork = Factory.get(ChatNetwork.class);

    // 物品售出时的折扣,取值0.0~1.0, 当玩家卖给NPC时会打折扣
    private float discount = 1f;
    
    // ---- inner
    private Window win;
    private LinearLayout bodyPanel;
    private TransferPanel sourcePanel;  // 玩家物品面板,显示角色已经有的物品
    private TransferPanel distPanel;    // 出售面板，展示要出售的物品
    private CenterPanel centerPanel;    // 分隔线
    private FooterPanel footerPanel;   // 显示售出的金额及确认按钮
    
    // 出售者角色，一般为Player.
    private Entity seller;

    @Override
    public void setData(EntityData data) {
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
                scene.removeEntity(SellChat.this);
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
        seller = gameService.getPlayer();
        
        // 初始化, 数据要复制一份出来，不要去影响角色的包裹中的数据
        List<ObjectData> tempDatas = new ArrayList<ObjectData>();
        List<ItemData> items = seller.getData().getObjectDatas(ItemData.class, null);
        if (items != null) {
            tempDatas.addAll(items);
        }
        List<SkinData> skins = seller.getData().getObjectDatas(SkinData.class, null);
        if (skins != null) {
            tempDatas.addAll(skins);
        }
        // 移除不能出售的物品
        Iterator<ObjectData> it = tempDatas.iterator();
        while (it.hasNext()) {
            ObjectData dp = it.next();
            if (dp instanceof ItemData) {
                ItemData item = (ItemData) dp;
                if (!item.isSellable()) {
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
    
    // 结算出售的金额
    private void billing() {
        List<TransferData> datas = distPanel.getDatas();
        if (datas.isEmpty())
            return;
        TransferData tempObj;
        for (int i = 0; i < datas.size(); i++) {
            tempObj = datas.get(i);
            chatNetwork.chatShop(seller, actor, tempObj.getObjectData().getUniqueId(), tempObj.getAmount(), discount);
        }
        // 确认后退出窗口
        scene.removeEntity(this);
    }
    
    // 估算价钱,最终价钱由接口计算决定。因为在确认“结算”之前玩家的包裹物品可能
    // 发生变化，并不能确保所有在"distPanel"窗口中的物品及数量都能准确售出。
    private int assess() {
        List<TransferData> datas = distPanel.getDatas();
        if (datas.isEmpty())
            return 0;
        float total = 0;
        for (TransferData pd : datas) {
            if (!(pd.getObjectData() instanceof TradeObject)) {
                continue;
            }
            TradeObject tradeObject = (TradeObject) pd.getObjectData();
            if (tradeObject.getTradeInfos() == null || tradeObject.getTradeInfos().isEmpty()) {
                continue;
            }
            // 只估算金币
            List<TradeInfo> tis = tradeObject.getTradeInfos();
            for (TradeInfo ti : tis) {
                if (ti.getObjectId().equals(IdConstants.ITEM_GOLD)) {
                    total += ti.getCount() * pd.getAmount();
                }
            }
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
