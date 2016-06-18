/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.game;

import java.util.List;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 局域网游戏接口
 * @author huliqing
 */
public interface  LanGame  {
    
    /**
     * 获取当前所有连接的客户端信息
     * @return 
     */
    List<ConnData> getClients();
    
    /**
     * 当客户端列表更新时该方法将被调用，客户端列表更新包含以下几个可能的情况：
     * 1.有新客户端连接<br >
     * 2.有客户端断开连接等。<br >
     * 3.客户端角色有资料更新，如角色名称或其它。<br >
     */
    void onClientListUpdated();
    
    /**
     * 当客户端玩家选择了一个角色进行游戏后调用该方法，实现这个方法可以在玩家选择角色后对角色进行一些属性的初始化。
     * 比如初始化角色的队伍分组，生命，魔法，等级等。
     * @param connData 客户端
     * @param actor 玩家所选择的角色
     */
    void onActorSelected(ConnData connData,  Actor actor);

    /**
     * 踢出一个客户端
     * @param connId 
     */
    void kickClient(int connId);
    
    /**
     * 判断是否为服务端,如果是则返回true,否则返回false
     * @return 
     */
    boolean isServer();
}
