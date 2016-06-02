/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.view;

import com.jme3.font.BitmapFont.VAlign;
import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.game.state.lan.mess.MessPlayClientData;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.ui.LinearLayout;
import name.huliqing.fighter.ui.ListView;
import name.huliqing.fighter.ui.Row;
import name.huliqing.fighter.ui.Text;
import name.huliqing.fighter.ui.UIFactory;
import name.huliqing.fighter.ui.UI;

/**
 * 客户端列表界面,用于显示连接到服务器的所有客户端
 * @author huliqing
 */
public class ClientsView extends LinearLayout {
    
    // 客户端面板 - 标题
    private LinearLayout titlePanel;
    private Text titleName; // 客户端名称
    private Text titleHost; // 客户端地址
    private Text titleActorName;// 玩家控制的角色名
    
    // 客户端面板－玩家列表
    private ClientList clientList;
    
    public ClientsView() {
        super();
        ColorRGBA tColor = UIFactory.getUIConfig().getDesColor();
        titlePanel = new LinearLayout();
        titleName = new Text(ResourceManager.get("lan.client.name"));
        titleName.setFontSize(UIFactory.getUIConfig().getTitleSize());
        titleName.setFontColor(tColor);
        
        titleHost = new Text(ResourceManager.get("lan.client.host"));
        titleHost.setFontSize(UIFactory.getUIConfig().getTitleSize());
        titleHost.setFontColor(tColor);
        
        titleActorName = new Text(ResourceManager.get("lan.client.actorName"));
        titleActorName.setFontSize(UIFactory.getUIConfig().getTitleSize());
        titleActorName.setFontColor(tColor);
        
        titlePanel.setLayout(Layout.horizontal);
        titlePanel.addView(titleName);
        titlePanel.addView(titleHost);
        titlePanel.addView(titleActorName);
        
        clientList = new ClientList(0,0);
        clientList.refreshPageData();
        
        addView(titlePanel);
        addView(clientList);
    }
    
    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        float w = width;
        float h = height;
        float th = UIFactory.getUIConfig().getTitleHeight();
        
        titlePanel.setWidth(w);
        titlePanel.setHeight(th);
        
        titleName.setWidth(w * 0.45f);
        titleHost.setWidth(w * 0.275f);
        titleActorName.setWidth(w * 0.275f);
        
        titleName.setHeight(th);
        titleHost.setHeight(th);
        titleActorName.setHeight(th);
        
//        titleName.setVerticalAlignment(BitmapFont.VAlign.Center);
//        titleHost.setVerticalAlignment(BitmapFont.VAlign.Center);
//        titleActorName.setVerticalAlignment(BitmapFont.VAlign.Center);
        
        clientList.setWidth(w);
        clientList.setHeight(h - th);
    }
    
    /**
     * 设置新的客户端列表,如果给定的列表为null，则将清空列表
     * @param clientData 
     */
    public void setClients(List<MessPlayClientData> clients) {
        // 这个方法必须保证线程同步，也不要在这个方法中去操作界面中的东西，如增
        // 加或减少Spatial之类，即不要去影响render线程的渲染。因为这个方法可能
        // 在其它线程中进行调用。
        synchronized (clientList.clients) {
            clientList.clients.clear();
            if (clients != null) {
                clientList.clients.addAll(clients);
            }
            clientList.setNeedUpdate();
        }
    }
    
    /**
     * 获取客户端数
     * @return 
     */
    public int getClientSize() {
        return clientList.clients.size();
    }
    
    /**
     * 获取当前选中的客户端,注意：该方法可能返回null,因为可能没有选中任何
     * 客户端。
     * @return 
     */
    public MessPlayClientData getSelected() {
        if (clientList.selected != null) {
            return clientList.selected.clientData;
        }
        return null;
    }
    
    // ----------------- inner class -------------------------------------------
    private class ClientList extends ListView<MessPlayClientData> {
        
        private final List<MessPlayClientData> clients = new ArrayList<MessPlayClientData>();
        // 当前选中的行
        private ClientRow selected;
        
        public ClientList(float width, float height) {
            super(width, height);
        }
        
        @Override
        protected Row<MessPlayClientData> createEmptyRow() {
            return new ClientRow(this);
        }
        
        @Override
        public List<MessPlayClientData> getDatas() {
            return clients;
        }

        public void setSelected(ClientRow row) {
            for (int i = 0; i < rows.size(); i++) {
                ((ClientRow) rows.get(i)).setActive(false);
            }
            this.selected = row;
            this.selected.setActive(true);
        }

        @Override
        public void updateView() {
            this.refreshPageData();
            // 在renderer中更新UI
            super.updateView();
        }
        
        // 不要直接从另一个线程去updateUI或是refreshPageData.
        // public这个方法主要是让当前UI知道需要更新，让它在render线程中自己去更新
        // 就行，否则可能引起UI问题
        @Override
        public void setNeedUpdate() {
            super.setNeedUpdate();
        }
    }
    
    private class ClientRow extends Row<MessPlayClientData> {
        private MessPlayClientData clientData;
        private Text nameLabel;     // 客户端标识：手机名称/PC名称
        private Text addressLabel;  // 客户端IP地址
        private Text actorNameLabel;
        
        public ClientRow(ListView listView) {
            super(listView);
            setBackground(UIFactory.getUIConfig().getBackground(), true);
            setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
            setBackgroundVisible(false);
            setLayout(Layout.horizontal);
            nameLabel = new Text("");
            nameLabel.setVerticalAlignment(VAlign.Center);
            addressLabel = new Text("");
            addressLabel.setVerticalAlignment(VAlign.Center);
            actorNameLabel = new Text("");
            actorNameLabel.setVerticalAlignment(VAlign.Center);
            addView(nameLabel);
            addView(addressLabel);
            addView(actorNameLabel);
            
            addClickListener(new Listener() {
                @Override
                public void onClick(UI ui, boolean isPress) {
                    if (isPress) return;
                    clientList.setSelected(ClientRow.this);
                }
            });
        }
        
        @Override
        public void displayRow(MessPlayClientData data) {
            clientData = data;
            nameLabel.setText(clientData.getName());
            String address = clientData.getAddress();
            // 去掉“/”和“:port”
            if (address.startsWith("/")) {
                address = address.substring(1);
            }
            int indexColon = address.indexOf(":");
            if (indexColon != -1) {
                address = address.substring(0, indexColon);
            }
            addressLabel.setText(address);
            
            // 不在这里设置角色名,在游戏运行时才设置
            actorNameLabel.setText(clientData.getActorName() != null ? clientData.getActorName() : " - ");
        }

        @Override
        public void updateViewChildren() {
            super.updateViewChildren();
            nameLabel.setWidth(titleName.getWidth());
            addressLabel.setWidth(titleHost.getWidth());
            actorNameLabel.setWidth(titleActorName.getWidth());
            
            nameLabel.setHeight(height);
            addressLabel.setHeight(height);
            actorNameLabel.setHeight(height);
            
//            nameLabel.setVerticalAlignment(BitmapFont.VAlign.Center);
//            addressLabel.setVerticalAlignment(BitmapFont.VAlign.Center);
//            actorNameLabel.setVerticalAlignment(BitmapFont.VAlign.Center);
            
        }
        
        public void setActive(boolean actived) {
            setBackgroundVisible(actived);
        }

        @Override
        protected void clickEffect(boolean isPressed) {
            // 不使用默认的效果
//            super.clickEffect(isPressed); 
        }
        
    }

}
