/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色行为逻辑数据
 * @author huliqing
 */
@Serializable
public class GameData extends ObjectData {
    
    // 场景ID
    private SceneData sceneData;
    // 游戏的逻辑列表
    private List<GameLogicData> gameLogics;
    // 游戏可选的角色列表
    private List<String> availableActors;
    
    public GameData(){}

    public SceneData getSceneData() {
        return sceneData;
    }

    public void setSceneData(SceneData sceneData) {
        this.sceneData = sceneData;
    }

    public List<GameLogicData> getGameLogics() {
        return gameLogics;
    }

    public void setGameLogics(List<GameLogicData> gameLogics) {
        this.gameLogics = gameLogics;
    }

    public List<String> getAvailableActors() {
        if (availableActors == null) {
            availableActors = new ArrayList<String>();
        }
        return availableActors;
    }

    public void setAvailableActors(List<String> availableActors) {
        this.availableActors = availableActors;
    }
    
}
