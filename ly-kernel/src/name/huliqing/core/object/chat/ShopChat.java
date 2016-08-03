/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.chat;

import com.jme3.app.Application;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.constants.InterfaceConstants;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.data.ChatData;
import name.huliqing.core.xml.ProtoData;
import name.huliqing.core.mvc.network.UserCommandNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.ItemService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.ItemListener;
import name.huliqing.core.ui.Button;
import name.huliqing.core.ui.Icon;
import name.huliqing.core.ui.LinearLayout;
import name.huliqing.core.ui.tiles.ColumnBody;
import name.huliqing.core.ui.tiles.ColumnIcon;
import name.huliqing.core.ui.tiles.ColumnText;
import name.huliqing.core.ui.LinearLayout.Layout;
import name.huliqing.core.ui.ListView;
import name.huliqing.core.ui.Row;
import name.huliqing.core.ui.Text;
import name.huliqing.core.ui.UI;
import name.huliqing.core.ui.UIConfig;
import name.huliqing.core.ui.UIFactory;
import name.huliqing.core.ui.Window;
import name.huliqing.core.ui.Window.CloseListener;

/**
 * 商店购物,用于玩家向商店角色购买物品
 * @author huliqing
 * @param <T>
 */
public class ShopChat<T extends ChatData> extends Chat<T> implements ItemListener {
    private final ItemService itemService = Factory.get(ItemService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final UserCommandNetwork userCommandNetwork = Factory.get(UserCommandNetwork.class);

    // 商品卖出时的折扣
    private float discount = 1.0f;
    
    // ---- inner
    private Window win;
    // 物品列表的标题
    private TitlePanel titlePanel;
    private ItemList productPanel;
    // 页脚
    private Footer footerPanel;

    @Override
    public void setData(T data) {
        super.setData(data); 
        discount = data.getAsFloat("discount", discount);
    }

    @Override
    protected UI createChatUI(float width, float height) {
        win = new Window("", width, height);
        float titleHeight = UIFactory.getUIConfig().getListTitleHeight();
        float footerHeight = UIFactory.getUIConfig().getFooterHeight();
        float listHeight = win.getContentHeight() - titleHeight - footerHeight;
        titlePanel = new TitlePanel(win.getContentWidth(), titleHeight);
        productPanel = new ItemList(win.getContentWidth(), listHeight);
        footerPanel = new Footer(win.getContentWidth(), footerHeight);
        
        win.setCloseable(true);
        win.setDragEnabled(true);
        win.addCloseListener(new CloseListener() {
            @Override
            public void onClosed(Window win) {
                playService.removeObject(ShopChat.this);
            }
        });
        win.addView(titlePanel);
        win.addView(productPanel);
        win.addView(footerPanel);
        win.setToCorner(UI.Corner.CC);
        return win;
    }

    @Override
    public void setActor(Actor actor) {
        // 移除旧的角色的侦听器（如果存在）
        if (this.actor != null) {
            this.actor.removeItemListener(this);
        }
        actor.addItemListener(this);
        super.setActor(actor); 
    }

    @Override
    public void onItemAdded(Actor actor, String itemId, int trueAdded) {
        if (isInitialized()) {
            if (this.actor != actor) {
                throw new IllegalStateException(); // 防止BUG
            }
            
            // remove20160312,不要直接updateShop，这会导致玩家在买东西的时候列表经验刷新，导致可能误点物品
//            updateShop();
            
            // 如果新添加了物品则同步物品数量
            ProtoData pd = itemService.getItem(actor, itemId);
            productPanel.syncItem(itemId, pd.getTotal());
            productPanel.refreshPageData();
            
            // 更新玩家剩余金币数
            footerPanel.update();
            
        }
    }

    @Override
    public void onItemRemoved(Actor actor, String itemId, int trueRemoved) {
        if (isInitialized()) {
            if (this.actor != actor) {
                throw new IllegalStateException(); // 防止BUG
            }
            
            // 不要直接updateShop，这会导致玩家在买东西的时候列表经验刷新，导致可能误点物品
//            updateShop();
            
            // 如果指定物品已经卖完则从商品列表中移除。
            ProtoData pd = itemService.getItem(actor, itemId);
            if (pd == null || pd.getTotal() <= 0) {
                productPanel.syncItem(itemId, 0);
            } else {
                productPanel.syncItem(itemId, pd.getTotal());
            }
            productPanel.refreshPageData();
            
            // 更新玩家剩余金币数
            footerPanel.update();
        }
    }

    @Override
    public void initialize(Application app) {
        super.initialize(app);

        win.setTitle(getChatName() + "-" + actorService.getName(actor));
        win.setToCorner(UI.Corner.CC);
        
        // remove20160312,不要直接updateShop，这会导致玩家在买东西的时候列表经验刷新，导致可能误点物品
//        updateShop();
        
        // 载入产品信息
        productPanel.datas.clear();
        itemService.getItems(actor, productPanel.datas);
        productPanel.refreshPageData();
        // 更新玩家剩余金币数
        footerPanel.update();
        
        // 关闭父窗口
        if (parent != null) {
            playService.removeObject(parent);
        }
    }
    
    // remove20160312,不要直接updateShop，这会导致玩家在买东西的时候列表经验刷新，导致可能误点物品
//    private void updateShop() {
//         // 载入角色当前所有的物品进行出售
//        productPanel.datas.clear();
//        itemService.getItems(actor, productPanel.datas);
//        productPanel.refreshPageData();
//        
//        // 更新玩家剩余金币数
//        footerPanel.update();
//    }

    @Override
    public void cleanup() {
        productPanel.datas.clear();
        if (actor != null) {
            actor.removeItemListener(this);
        }
        super.cleanup(); 
    }
    
    private class ItemList extends ListView<ProtoData> {

        final List<ProtoData> datas = new ArrayList<ProtoData>();
        
        public ItemList(float width, float height) {
            super(width, height);
            setBackground(UIFactory.getUIConfig().getBackground(), true);
            setBackgroundColor(UIFactory.getUIConfig().getBodyBgColor(), true);
            setPageSize(6);
        }

        @Override
        protected Row<ProtoData> createEmptyRow() {
            return new ItemRow(this);
        }

        @Override
        public List<ProtoData> getDatas() {
            return datas;
        }
        
        // 注意：这里只同步total数量，不要去移除列表datas中的数据，即使total是0.
        // 这可避免在角色快速购买物品时由于物品被移除导致列表刷新带来的错误点击
        public void syncItem(String itemId, int total) {
            // 如果存在列表中则同步total数
            for (ProtoData pd : datas) {
                if (pd.getId().equals(itemId)) {
                    pd.setTotal(total);
                    return;
                }
            }
            // 如果列表中不存在，则把data添加进来
            ProtoData newData = DataFactory.createData(itemId);
            newData.setTotal(total);
            datas.add(newData);
        }
        
//        public void removeData(String itemId) {
//            Iterator<ProtoData> it = datas.iterator();
//            while (it.hasNext()) {
//                if (it.next().getId().equals(itemId)) {
//                    it.remove();
//                    break;
//                }
//            }
//        }

        @Override
        protected boolean filter(ProtoData data) {
            return !itemService.isSellable(data);
        }
        
    }
    
    private class ItemRow extends Row<ProtoData> {
        private ProtoData data;

        // 物品
        private ColumnIcon icon;
        private ColumnBody body;
        private ColumnText cost; // 商品价值
        private ColumnText num;
        private Button button;

        public ItemRow(ListView listView) {
            super(listView);
            this.setLayout(Layout.horizontal);
            icon = new ColumnIcon(height, height, InterfaceConstants.UI_MISS);
            body = new ColumnBody(height, height, "", "");
            cost = new ColumnText(height, height, "");
            cost.setAlignment(BitmapFont.Align.Right);
            num = new ColumnText(height, height, "");
            num.setAlignment(BitmapFont.Align.Right);
            button = new Button(ResourceManager.get(ResConstants.CHAT_SHOP_BUY));
            button.setFontSize(UIFactory.getUIConfig().getDesSize());
            addView(icon);
            addView(body);
            addView(cost);
            addView(num);
            addView(button);
            
            button.addClickListener(new Listener() {
                @Override
                public void onClick(UI view, boolean isPressed) {
                    if (isPressed) return;
                    userCommandNetwork.chatShop(actor, playService.getPlayer(), data.getId(), 1, discount);
                }
            });
        }

        @Override
        public void updateViewChildren() {
            super.updateViewChildren();
            float iconWidth = height;
            float costWidth = height + 20;
            float numWidth = height + 20;
            float buttonWidth = height * 2;

            icon.setWidth(iconWidth);
            icon.setHeight(height);
            titlePanel.iconTitle.setWidth(icon.getWidth());

            cost.setWidth(costWidth);
            cost.setHeight(height);
            titlePanel.costTitle.setWidth(cost.getWidth());
            
            num.setWidth(numWidth);
            num.setHeight(height);
            titlePanel.numTitle.setWidth(num.getWidth());

            button.setWidth(buttonWidth * 0.8f);
            button.setHeight(height * 0.8f);
            button.setMargin(buttonWidth * 0.1f, height * 0.1f, 0, 0);
//            button.setPreventEvent(true);// remove
            titlePanel.buttonTitle.setWidth(button.getWidth());

            body.setWidth(width - iconWidth - costWidth - numWidth - buttonWidth);
            body.setHeight(height);
            titlePanel.bodyTitle.setWidth(body.getWidth());
            titlePanel.updateView();
        }

        @Override
        public final void displayRow(ProtoData dd) {
            this.data = dd;
            icon.setIcon(data.getIcon());
            body.setNameText(ResourceManager.getObjectName(data));
            body.setDesText(ResourceManager.getObjectDes(data.getId()));
            
            cost.setText(data.getCost() + "");
            num.setText(data.getTotal() + "");
            setNeedUpdate();
        }
    }
    
    private class TitlePanel extends LinearLayout {
        Text iconTitle;
        Text bodyTitle;
        Text costTitle;
        Text numTitle;
        Text buttonTitle;
        
        public TitlePanel(float width, float height) {
            super(width, height);
            setLayout(Layout.horizontal);
            UIConfig uf = UIFactory.getUIConfig();
            ColorRGBA desColor = UIFactory.getUIConfig().getDesColor();
            iconTitle = new Text("", desColor);
            iconTitle.setFontSize(uf.getDesSize());
            
            bodyTitle = new Text(ResourceManager.get(ResConstants.COMMON_NAME), desColor);
            bodyTitle.setFontSize(uf.getDesSize());
            
            costTitle = new Text(ResourceManager.get(ResConstants.CHAT_SHOP_PRICE), desColor);
            costTitle.setAlignment(BitmapFont.Align.Right);
            costTitle.setFontSize(uf.getDesSize());
            
            numTitle = new Text(ResourceManager.get(ResConstants.CHAT_SHOP_STOCK), desColor);
            numTitle.setAlignment(BitmapFont.Align.Right);
            numTitle.setFontSize(uf.getDesSize());
            
            buttonTitle = new Text("", desColor);
            buttonTitle.setFontSize(uf.getDesSize());
            
            iconTitle.setVerticalAlignment(BitmapFont.VAlign.Center);
            bodyTitle.setVerticalAlignment(BitmapFont.VAlign.Center);
            costTitle.setVerticalAlignment(BitmapFont.VAlign.Center);
            numTitle.setVerticalAlignment(BitmapFont.VAlign.Center);
            buttonTitle.setVerticalAlignment(BitmapFont.VAlign.Center);
            addView(iconTitle);
            addView(bodyTitle);
            addView(costTitle);
            addView(numTitle);
            addView(buttonTitle);
            setBackground(UIFactory.getUIConfig().getBackground(), true);
            setBackgroundColor(UIFactory.getUIConfig().getTitleBgColor(), true);
        }

        @Override
        protected void updateViewChildren() {
            super.updateViewChildren();
            iconTitle.setHeight(height);
            bodyTitle.setHeight(height);
            costTitle.setHeight(height);
            numTitle.setHeight(height);
            buttonTitle.setHeight(height);
        }
        
    }
    
    private class Footer extends LinearLayout {
        // 显示角色的剩余金币数
        private final Icon goldsIcon;
        private final Text goldsRemain;
        
        public Footer(float width, float height) {
            super(width, height);
            setLayout(Layout.horizontal);
            setBackground(UIFactory.getUIConfig().getBackground(), true);
            setBackgroundColor(UIFactory.getUIConfig().getFooterBgColor(), false);
            
            goldsIcon = new Icon(UIFactory.getUIConfig().getMissIcon());
            goldsIcon.setWidth(height * 0.8f);
            goldsIcon.setHeight(height * 0.8f);
            goldsIcon.setMargin(10, height * 0.11f, 0, 0);
            goldsRemain = new Text("");
            goldsRemain.setWidth(width - goldsIcon.getWidth());
            goldsRemain.setHeight(height);
            goldsRemain.setFontSize(UIFactory.getUIConfig().getDesSize());
            goldsRemain.setVerticalAlignment(BitmapFont.VAlign.Center);
            goldsRemain.setMargin(10, 0, 0, 2);
            addView(goldsIcon);
            addView(goldsRemain);
        }
        
        public void update() {
            // 注：这里显示的是当前玩家的剩余金币 ，不是卖家
            Actor player = playService.getPlayer();
            ProtoData goldData =  itemService.getItem(player, IdConstants.ITEM_GOLD);
            int golds = 0;
            if (goldData != null) {
                golds = goldData.getTotal();
                goldsIcon.setImage(goldData.getProto().getIcon());
            } else {
                goldData = DataFactory.createData(IdConstants.ITEM_GOLD);
                goldsIcon.setImage(goldData.getProto().getIcon());
            }
            goldsRemain.setText(ResourceManager.get(ResConstants.CHAT_SHOP_GOLDS_REMAIN) + ":" + golds);
            setNeedUpdate();
        }
    }
}
