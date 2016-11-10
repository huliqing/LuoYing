/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.chat;

import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.InterfaceConstants;
import name.huliqing.luoying.constants.ResConstants;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.ly.data.ChatData;
import name.huliqing.luoying.manager.ResourceManager;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.EntityDataListener;
import name.huliqing.luoying.object.item.Item;
import name.huliqing.luoying.ui.Button;
import name.huliqing.luoying.ui.Icon;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.tiles.ColumnBody;
import name.huliqing.luoying.ui.tiles.ColumnIcon;
import name.huliqing.luoying.ui.tiles.ColumnText;
import name.huliqing.luoying.ui.LinearLayout.Layout;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UIConfig;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.Window;
import name.huliqing.luoying.ui.Window.CloseListener;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.layer.network.ChatNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 * 杂物商店,用于玩家向商店角色购买物品
 * @author huliqing
 * @param <T>
 */
public class ShopItemChat<T extends ChatData> extends Chat<T> implements EntityDataListener {
//    private final PlayService playService = Factory.get(PlayService.class);
    private final GameService gameService = Factory.get(GameService.class);
//    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    private final ChatNetwork chatNetwork = Factory.get(ChatNetwork.class);

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
                scene.removeEntity(ShopItemChat.this);
            }
        });
        win.addView(titlePanel);
        win.addView(productPanel);
        win.addView(footerPanel);
        win.setToCorner(UI.Corner.CC);
        return win;
    }

    @Override
    public void setActor(Entity actor) {
        // 移除旧的角色的侦听器（如果存在）
        if (this.actor != null) {
            this.actor.removeEntityDataListener(this);
        }
        
        actor.addEntityDataListener(this);
        super.setActor(actor); 
    }
    
    
    @Override
    public void cleanup() {
        productPanel.datas.clear();
        if (actor != null) {
            actor.removeEntityDataListener(this);
        }
        super.cleanup(); 
    }

    @Override
    public void onDataAdded(ObjectData data, int amount) {
        if (data instanceof ItemData) {
            updateProductPanel(actor, (ItemData) data);
        }
    }

    @Override
    public void onDataRemoved(ObjectData data, int amount) {
        if (data instanceof ItemData) {
            updateProductPanel(actor, (ItemData) data);
        }
    }

    @Override
    public void onDataUsed(ObjectData data) {
        // ignore
    }

    private void updateProductPanel(Entity source, ItemData item) {
         if (isInitialized()) {
            if (this.actor != actor) {
                throw new IllegalStateException(); // 防止BUG
            }
            // 不要直接updateShop，这会导致玩家在买东西的时候列表经验刷新，导致可能误点物品
//            updateShop();
            // 如果指定物品已经卖完则从商品列表中移除。
            productPanel.syncItem(item.getId(), item.getTotal());
            productPanel.refreshPageData();
            // 更新玩家剩余金币数
            footerPanel.update();
        }
    }

    @Override
    public void initEntity() {
        super.initEntity();

        win.setTitle(getChatName() + "-" + actor.getData().getName());
        win.setToCorner(UI.Corner.CC);
        
        // remove20160312,不要直接updateShop，这会导致玩家在买东西的时候列表经验刷新，导致可能误点物品
//        updateShop();
        
        // 载入产品信息
        productPanel.datas.clear();
        productPanel.datas.addAll(actor.getData().getObjectDatas(ItemData.class, new ArrayList<ItemData>()));
        productPanel.refreshPageData();
        // 更新玩家剩余金币数
        footerPanel.update();
        
        // 关闭父窗口
        if (parent != null) {
            scene.removeEntity(parent);
        }
    }
    
    private class ItemList extends ListView<ItemData> {

        final List<ItemData> datas = new ArrayList<ItemData>();
        
        public ItemList(float width, float height) {
            super(width, height);
            setBackground(UIFactory.getUIConfig().getBackground(), true);
            setBackgroundColor(UIFactory.getUIConfig().getBodyBgColor(), true);
            setPageSize(6);
        }

        @Override
        protected Row<ItemData> createEmptyRow() {
            return new ItemRow(this);
        }

        @Override
        public List<ItemData> getDatas() {
            return datas;
        }
        
        // 注意：这里只同步total数量，不要去移除列表datas中的数据，即使total是0.
        // 这可避免在角色快速购买物品时由于物品被移除导致列表刷新带来的错误点击
        public void syncItem(String itemId, int total) {
            // 如果存在列表中则同步total数
            for (ItemData item : datas) {
                if (item.getId().equals(itemId)) {
                    item.setTotal(total);
                    return;
                }
            }
            // 如果列表中不存在，则把data添加进来
            ItemData item = Loader.loadData(itemId);
            item.setTotal(total);
            datas.add(item);
        }
        
        @Override
        protected boolean filter(ItemData item) {
            return !item.isSellable();
        }
        
    }
    
    private class ItemRow extends Row<ItemData> {
        private ItemData data;

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
                    chatNetwork.chatShop(actor, gameService.getPlayer(), data.getId(), 1, discount);
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
        public final void displayRow(ItemData dd) {
            this.data = dd;
            icon.setIcon(data.getIcon());
            body.setNameText(ResourceManager.getObjectName(data));
            body.setDesText(ResourceManager.getObjectDes(data.getId()));
//            cost.setText(data.getData().getCost() + "");
            cost.setText("Need to refactor........");
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
            ItemData gold = gameService.getPlayer().getData().getObjectData( IdConstants.ITEM_GOLD);
            
            int golds = 0;
            if (gold != null) {
                golds = gold.getTotal();
                goldsIcon.setImage(gold.getIcon());
            } else {
                gold = DataFactory.createData(IdConstants.ITEM_GOLD);
                goldsIcon.setImage(gold.getIcon());
            }
            goldsRemain.setText(ResourceManager.get(ResConstants.CHAT_SHOP_GOLDS_REMAIN) + ":" + golds);
            setNeedUpdate();
        }
    }
}
