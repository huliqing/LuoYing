/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.network.PlayNetwork;
import name.huliqing.ly.network.GameServer;

/**
 * 向服务端获取游戏初始信息,主要用于初始化客户端场景物体,这个消息在发送之前
 * 客户端必须确认服务端已经处于running状态，同时客户端必须处于ready状态。
 * @author huliqing
 */
@Serializable
public class MessPlayInitGame extends MessBase {

    @Override
    public void applyOnServer(GameServer server, HostedConnection client) {
        // 向客户端发送初始场景状态信息,客户端只有在接收到这个标记之后才会开始
        // 接收游戏数据
        MessSCInitGameOK initOK = new MessSCInitGameOK();
        server.send(client, initOK);
        
        // 同步所有角色到客户端
        PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
        playNetwork.syncGameInitToClient(client);
    }
    
}
