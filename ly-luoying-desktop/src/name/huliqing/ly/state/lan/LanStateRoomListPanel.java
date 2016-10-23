/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.state.lan;

import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import name.huliqing.luoying.network.GameServer.ServerState;
import name.huliqing.luoying.manager.ResourceManager;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.LinearLayout.Layout;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UI.Listener;
import name.huliqing.luoying.ui.Window;

/**
 * 房间列表面板，用于显示局域网内当前正在运行的服务器
 * @author huliqing
 */
public class LanStateRoomListPanel extends Window {
    private LinearLayout titlePanel;
    private Text titleGame;
    private Text titleHostName;
    private Text titleVersion;
//    private Text titleHost;
    private Text titleState;
    private Text titlePing;
    
    private RoomList roomList;

    public LanStateRoomListPanel(float width, float height) {
        super(ResourceManager.get("lan.rooms"), width, height);
        this.setBackgroundVisible(false); // 隐藏背景
        
        ColorRGBA tColor = UIFactory.getUIConfig().getDesColor();
        titlePanel = new LinearLayout();
        titleGame = new Text("  " + ResourceManager.get("lan.room.game"));
        titleHostName = new Text(ResourceManager.get("lan.room.hostName"));
        titleVersion = new Text(ResourceManager.get("lan.room.version"));
//        titleHost = new Text(ResourceManager.get("lan.room.host"));
        titleState = new Text(ResourceManager.get("lan.room.state"));
        titlePing = new Text(ResourceManager.get("lan.room.ping") + "  ");
        titleGame.setFontColor(tColor);
        titleHostName.setFontColor(tColor);
        titleVersion.setFontColor(tColor);
//        titleHost.setFontColor(tColor);
        titleState.setFontColor(tColor);
        titlePing.setFontColor(tColor);
        
        titlePanel.setLayout(Layout.horizontal);
        titlePanel.addView(titleGame);
        titlePanel.addView(titleHostName);
        titlePanel.addView(titleVersion);
//        titlePanel.addView(titleHost);
        titlePanel.addView(titleState);
        titlePanel.addView(titlePing);
        
        roomList = new RoomList(0, 0);
        roomList.refreshPageData();
        
        addView(titlePanel);
        addView(roomList);
    }
    
    /**
     * 获取当前房间数
     * @return 
     */
    public int getRoomSize() {
        return roomList.getRowTotal();
    }
    
    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        float fw = getContentWidth();
        float fh = getContentHeight();
        float tHeight = UIFactory.getUIConfig().getButtonHeight();
        
        titlePanel.setWidth(fw);
        titlePanel.setHeight(tHeight);
        
        titleGame.setWidth(fw * 0.3f);
        titleHostName.setWidth(fw * 0.3f);
        titleVersion.setWidth(fw * 0.15f);
//        titleHost.setWidth(fw * 0.2f);
        titleState.setWidth(fw * 0.15f);
        titlePing.setWidth(fw * 0.1f);
        
        titleGame.setHeight(tHeight);
        titleHostName.setHeight(tHeight);
        titleVersion.setHeight(tHeight);
//        titleHost.setHeight(titleHeight);
        titleState.setHeight(tHeight);
        titlePing.setHeight(tHeight);
        
        titleGame.setVerticalAlignment(BitmapFont.VAlign.Center);
        titleHostName.setVerticalAlignment(BitmapFont.VAlign.Center);
        titleVersion.setVerticalAlignment(BitmapFont.VAlign.Center);
//        titleHost.setVerticalAlignment(BitmapFont.VAlign.Center);
        titleState.setVerticalAlignment(BitmapFont.VAlign.Center); titleState.setAlignment(BitmapFont.Align.Center);
        titlePing.setVerticalAlignment(BitmapFont.VAlign.Center); titlePing.setAlignment(BitmapFont.Align.Right);
        
        roomList.setWidth(fw);
        roomList.setHeight(fh - tHeight);
        roomList.refreshPageData(); // 这里必须刷新一次，否则可能看不到房间列表
    }
    
    /**
     * 添加一个新房间或者更新房间信息,当房间已经存在时即为更新,以避免添加到
     * 重复的房间
     * @param roomData
     */
    public void addOrUpdateRoom(RoomData roomData) {
        // 注意：为保证线程安全，这里必须上锁，并且不要直接去调用roomList.refreshPageData
        // 因为直接去操作到render线程。
        synchronized (roomList.datas) {
            roomList.addItem(roomData);
            roomList.setNeedUpdate();
            if (roomList.selected == null) {
                roomList.setSelectRoom(0);
            }
        }
    }
    
    /**
     * 移除一个房间
     * @param roomData
     */
    public void removeRoom(RoomData roomData) {

        synchronized (roomList.datas) {
            roomList.removeItem(roomData);
            roomList.setNeedUpdate();
        }
    }
    
    /**
     * 获取当前被选中的房间，注意：反回值可能是null,即可能没有选择任何房间。
     * @return 
     */
    public RoomData getSelected() {
        if (roomList.selected != null) {
            return roomList.selected.data;
        }
        return null;
    }
    
    // ---- inner class --------------------------------------------------------
    
    // 房间列表类
    private class RoomList extends ListView<RoomData>{
        private final List<RoomData> datas = new ArrayList<RoomData>();
        // 当前选择的房间
        private RoomRow selected;
        
        public RoomList(float width, float height) {
            super(width, height, "RoomList");
        }

        /**
         * 添加一个新房间或者更新房间信息,当房间已经存在时即为更新,以避免添加到
         * 重复的房间
         * @param newRoom 
         */
        @Override
        public void addItem(RoomData newRoom) {
            boolean updated = false;
            // 如果房间已经存在则只更新就可以，否则添加新的。
            for (RoomData room : datas) {
                if (room.compare(newRoom)) {
                    room.updateFrom(newRoom);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                datas.add(newRoom);
            }
        }

        // 移除一个房间
        @Override
        public boolean removeItem(RoomData rmRoom) {
            Iterator<RoomData> it = datas.iterator();
            boolean result = false;
            while (it.hasNext()) {
                RoomData room = it.next();
                if (room.compare(rmRoom)) {
                    it.remove();
                    result = true;
                }
            }
            return result;
        }
        
        @Override 
        public void setNeedUpdate() {
            super.setNeedUpdate();
        }
        
        @Override
        protected Row createEmptyRow() {
            return new RoomRow();
        }

        @Override
        public List<RoomData> getDatas() {
            return datas;
        }
        
        public void setSelectRoom(int index) {
            if (index < rows.size() && index >= 0) {
                selectRoom((RoomRow) rows.get(index));
            }
        }
        
        private void selectRoom(RoomRow room) {
            for (Row row : rows) {
                ((RoomRow) row).setActive(false);
            }
            selected = room;
            selected.setActive(true);
        }

    }
    
    private class RoomRow extends Row<RoomData> {
        private RoomData data;
        private Text desLabel;
        private Text hostNameLabel;
        private Text versionLabel;
//        private Text hostLabel;
        // 显示正在游戏，或等待中
        private Text stateLabel;
        // 显示Ping值,单位毫秒
        private Text pingLabel;
        
        public RoomRow() {
            // 用户激活效果 effect event.
            setBackground(UIFactory.getUIConfig().getBackground(), true);
            setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
            setBackgroundVisible(false);
            setLayout(Layout.horizontal);
            
            desLabel = new Text("");
            hostNameLabel = new Text("");
            versionLabel = new Text("");
//            hostLabel = new Text("");
            stateLabel = new Text("");
            pingLabel = new Text("");
            addView(desLabel);
            addView(hostNameLabel);
            addView(versionLabel);
//            addView(hostLabel);
            addView(stateLabel);
            addView(pingLabel);
            
            addClickListener(new Listener() {
                @Override
                public void onClick(UI ui, boolean isPress) {
                    if (isPress) return;
                    roomList.selectRoom(RoomRow.this);
                }
            });
        }
        
        @Override
        public void displayRow(RoomData rd) {
            data = rd;
            desLabel.setText("  " + data.getDes());
            hostNameLabel.setText(data.getHostName());
            versionLabel.setText(data.getVersion());
//            hostLabel.setText(data.getHost());
            if (data.getServerState() == ServerState.waiting) {
                stateLabel.setText(ResourceManager.get("lan.room.wait"));
            } else if (data.getServerState() == ServerState.loading) {
                stateLabel.setText(ResourceManager.get("lan.room.loading"));
            }  else if (data.getServerState() == ServerState.running) {
                stateLabel.setText(ResourceManager.get("lan.room.running"));
            } else if (data.getServerState() == ServerState.shutdown) {
                stateLabel.setText(ResourceManager.get("lan.room.shutdown"));
            } else {
                stateLabel.setText("Unknow!");
            }
            pingLabel.setText(data.getPing() + "  ");
        }

        @Override
        public void updateViewChildren() {
            super.updateViewChildren();
            
            desLabel.setWidth(titleGame.getWidth());
            hostNameLabel.setWidth(titleHostName.getWidth());
            versionLabel.setWidth(titleVersion.getWidth());
//            hostLabel.setWidth(titleHost.getWidth());
            stateLabel.setWidth(titleState.getWidth());
            pingLabel.setWidth(titlePing.getWidth());
            
            desLabel.setHeight(height);
            hostNameLabel.setHeight(height);
            versionLabel.setHeight(height);
//            hostLabel.setHeight(height);
            stateLabel.setHeight(height);
            pingLabel.setHeight(height);
            
            desLabel.setVerticalAlignment(BitmapFont.VAlign.Center);
            hostNameLabel.setVerticalAlignment(BitmapFont.VAlign.Center);
            versionLabel.setVerticalAlignment(BitmapFont.VAlign.Center);
//            hostLabel.setVerticalAlignment(BitmapFont.VAlign.Center);
            stateLabel.setVerticalAlignment(BitmapFont.VAlign.Center); stateLabel.setAlignment(BitmapFont.Align.Center);
            pingLabel.setVerticalAlignment(BitmapFont.VAlign.Center);pingLabel.setAlignment(BitmapFont.Align.Right);
            
        }
        
        public void setActive(boolean actived) {
            setBackgroundVisible(actived);
        }
    }
    
}
