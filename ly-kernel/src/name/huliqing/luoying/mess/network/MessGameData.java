/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess.network;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.data.GameData;
import name.huliqing.luoying.mess.MessBase;

/**
 * 服务端向客户端发送当前的游戏数据
 * @author huliqing
 */
@Serializable
public class MessGameData extends MessBase {
    
    private GameData gameData;
    
    public MessGameData() {}
    
    public MessGameData(GameData gameData) {
        this.gameData = gameData;
    }

    public GameData getGameData() {
        return gameData;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }
    
}
