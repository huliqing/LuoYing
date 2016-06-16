/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.game;

import java.util.List;
import name.huliqing.fighter.game.mess.MessPlayClientData;

/**
 * 局域网游戏接口
 * @author huliqing
 */
public interface  LanGame  {
    
    /**
     * 获取当前所有连接的客户端信息
     * @return 
     */
    List<MessPlayClientData> getClients();

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
