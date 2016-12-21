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
package name.huliqing.ly.object.game;

import com.jme3.app.Application;
import com.jme3.network.HostedConnection;
import name.huliqing.luoying.mess.ActorLoadSavedMess;
import name.huliqing.luoying.mess.ActorLoadSavedResultMess;
import name.huliqing.luoying.mess.GameMess;
import name.huliqing.luoying.network.GameServer;
import name.huliqing.luoying.object.scene.Scene;

/**
 * 局域网服务端，每个玩家包括服务端玩家和客户端玩家都需要通过角色选择界面来选择角色进行游戏。
 * @author huliqing
 */
public class LanServerNetworkRpgGame extends ServerNetworkRpgGame {

    @Override
    public void initialize(Application app) {
        super.initialize(app);
         // 隐藏其它UI界面
        setUIVisiable(false);
    }

    @Override
    public void onSceneLoaded(Scene scene) {
        super.onSceneLoaded(scene);
        // 显示角色选择面板
        showSelectPanel();
    }
    
    @Override
    protected void processGameMess(GameServer gameServer, HostedConnection source, GameMess m) {
        // 客户端会优先偿试从服务端中载入客户端所存档的资源。
        // 但是局域网模式下的服务端不保存客户端资料，所以这里直接返回false，
        // 以便让客户端去弹出角色选择窗口，选择一个角色进行游戏
        if (m instanceof ActorLoadSavedMess) {
            gameServer.send(source, new ActorLoadSavedResultMess(false));
            return;
        }
        
        super.processGameMess(gameServer, source, m); 
    }
}
