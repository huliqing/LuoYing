/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.state.lan.GameServer;

/**
 * 服务端向客户端，或者客户端向服务端发送“切换游戏”的消息。
 * @author huliqing
 */
@Serializable
public class MessPlayChangeGameState  extends MessBase {
    
    // 要切换的目标游戏数据
    private GameData gameData;
    
    public MessPlayChangeGameState() {}

    public MessPlayChangeGameState(GameData gameData) {
        this.gameData = gameData;
    }

    public GameData getGameData() {
        return gameData;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }
    
    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        if (gameData != null) {
            PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
            playNetwork.changeGame(gameData);
        }
    }

    @Override
    public void applyOnClient() {
        if (gameData != null) {
            PlayService playService = Factory.get(PlayService.class);
            playService.changeGame(gameData);
        }
    }
    
}
