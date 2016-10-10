/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.data;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 定义游戏的数据
 * @author huliqing
 */
@Serializable
public class GameData extends ObjectData {
    
    // 场景ID
    private SceneData sceneData;
    // 游戏的逻辑列表
    private List<GameLogicData> gameLogicDatas;
    
//    // 游戏可选的角色列表
//    private List<String> availableActors;

    /**
     * 获取场景数据
     * @return 
     */
    public SceneData getSceneData() {
        return sceneData;
    }

    /**
     * 设置场景数据
     * @param sceneData 
     */
    public void setSceneData(SceneData sceneData) {
        this.sceneData = sceneData;
    }

    /**
     * 获取游戏逻辑数据列表
     * @return 
     */
    public List<GameLogicData> getGameLogicDatas() {
        return gameLogicDatas;
    }

    /**
     * 设置游戏逻辑数据列表
     * @param gameLogics 
     */
    public void setGameLogicDatas(List<GameLogicData> gameLogics) {
        this.gameLogicDatas = gameLogics;
    }
    
    /**
     * 添加一个游戏逻辑数据
     * @param gameLogicData 
     */
    public void addGameLogicData(GameLogicData gameLogicData) {
        if (gameLogicDatas == null) {
            gameLogicDatas = new ArrayList<GameLogicData>();
        }
        if (!gameLogicDatas.contains(gameLogicData)) {
            gameLogicDatas.add(gameLogicData);
        }
    }
    
    /**
     * 移除一个游戏逻辑数据
     * @param gameLogicData
     * @return 
     */
    public boolean removeGameLogicData(GameLogicData gameLogicData) {
        return gameLogicDatas != null && gameLogicDatas.remove(gameLogicData);
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(sceneData, "sceneData", null);
        if (gameLogicDatas != null) {
            oc.writeSavableArrayList(new ArrayList<GameLogicData>(gameLogicDatas), "gameLogicDatas", null);
        }
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        sceneData = (SceneData) ic.readSavable("sceneData", null);
        gameLogicDatas = ic.readSavableArrayList("gameLogicDatas", null);
    }

//    public List<String> getAvailableActors() {
//        if (availableActors == null) {
//            availableActors = new ArrayList<String>();
//        }
//        return availableActors;
//    }
//
//    public void setAvailableActors(List<String> availableActors) {
//        this.availableActors = availableActors;
//    }
    
}
