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
package name.huliqing.ly.state.lan;

import java.util.List;
import name.huliqing.luoying.data.GameData;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.ly.view.ClientsView;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.luoying.ui.Window;

/**
 * 用于显示进入房间的客户端列表及游戏信息，主机和客户端共用该UI
 * @author huliqing
 */
public class RoomStatePanel extends Window {

    // 客户端列表面板
    private ClientsView clientsView;
    
    // 游戏信息面板
    private LanGameInfoView gameOverview;
    
    public RoomStatePanel(float width, float height) {
        super(ResourceManager.get("lan.clients"), width, height);
        this.setBackgroundVisible(false);
        this.setLayout(Layout.horizontal);
        
        // 客户端列表
        clientsView = new ClientsView();
        // 游戏信息
        gameOverview = new LanGameInfoView();
        
        addView(clientsView);
        addView(gameOverview);
        
    }
    
    public void setGameData(GameData gameData) {
        gameOverview.setGameData(gameData);
    }

    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        float w = getContentWidth();
        float h = getContentHeight();
        clientsView.setWidth(w * MathUtils.GOLD_SEPARATE);
        clientsView.setHeight(h);
        
        gameOverview.setWidth(w - clientsView.getWidth());
        gameOverview.setHeight(h);
        
    }
    
    /**
     * 设置新的客户端列表
     * @param clients
     */
    public void setClients(List<ConnData> clients) {
        clientsView.setClients(clients);
    }
    
    /**
     * 获取当前选中的客户端,注意：该方法可能返回null,因为可能没有选中任何
     * 客户端。
     * @return 
     */
    public ConnData getSelected() {
        return clientsView.getSelected();
    }

}
