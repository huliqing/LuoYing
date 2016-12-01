/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game;

import com.jme3.app.Application;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.mess.ActorLoadSavedMess;
import name.huliqing.luoying.mess.ActorLoadSavedResultMess;
import name.huliqing.luoying.network.GameServer;
import name.huliqing.luoying.object.entity.Entity;
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
    protected void onAddServerPlayer(Entity actor) {
        super.onAddServerPlayer(actor); 
    }

    @Override
    protected void onAddClientPlayer(ConnData connData, Entity actor) {
        super.onAddClientPlayer(connData, actor); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected boolean processMessage(GameServer gameServer, HostedConnection source, Message m) {
        // 客户端会优先偿试从服务端中载入客户端所存档的资源。
        // 但是局域网模式下的服务端不保存客户端资料，所以这里直接返回false，
        // 以便让客户端去弹出角色选择窗口，选择一个角色进行游戏
        if (m instanceof ActorLoadSavedMess) {
            gameServer.send(source, new ActorLoadSavedResultMess(false));
            return true;
        }
        
        return super.processMessage(gameServer, source, m); 
    }
}
