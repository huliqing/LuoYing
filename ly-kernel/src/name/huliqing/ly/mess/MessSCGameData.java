/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.ly.data.GameData;

/**
 * server to client, 告诉客户端当前正在或正要玩的游戏
 * @author huliqing
 */
@Serializable
public class MessSCGameData extends MessBase {
    
    private GameData gameData;
    
    public MessSCGameData() {}
    
    public MessSCGameData(GameData gameData) {
        this.gameData = gameData;
    }

    public GameData getGameData() {
        return gameData;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }
    
}
