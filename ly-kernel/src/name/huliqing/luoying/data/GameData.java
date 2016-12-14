/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.data;

import name.huliqing.luoying.xml.ObjectData;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.network.serializing.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.xml.SimpleCloner;

/**
 * 定义游戏的数据
 * @author huliqing
 */
@Serializable
public class GameData extends ObjectData {
    
    // 场景ID
    private SceneData sceneData;
    // GUI场景
    private SceneData guiSceneData;
    // 游戏的逻辑列表
    private List<GameLogicData> gameLogicDatas;
    
    // 游戏可选的角色列表
    private List<String> availableActors;
    
    /**
     * 获取游戏图标，如果没有设置则返回null.
     * @return 
     */
    public String getIcon() {
        return getAsString("icon");
    }

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

    public SceneData getGuiSceneData() {
        return guiSceneData;
    }

    public void setGuiSceneData(SceneData guiSceneData) {
        this.guiSceneData = guiSceneData;
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
        // 这里要注意：不能把null添加到gameLogicData中，这会造成在网络序列化传送过程中报NPE
        if (gameLogicData != null && !gameLogicDatas.contains(gameLogicData)) {
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

    /**
     * 获取游戏可选的角色id列表
     * @return 
     */
    public List<String> getAvailableActors() {
        return availableActors;
    }

    /**
     * 设置游戏可选的角色id列表
     * @param availableActors 
     */
    public void setAvailableActors(List<String> availableActors) {
        this.availableActors = availableActors;
    }

    @Override
    public ObjectData clone() {
        SimpleCloner cloner = new SimpleCloner();
        GameData clone = (GameData) super.clone();
        clone.sceneData = cloner.clone(sceneData);
        clone.guiSceneData = cloner.clone(guiSceneData);
        clone.gameLogicDatas = cloner.clone(gameLogicDatas);
        
        if (availableActors != null) {
            clone.availableActors = new ArrayList<String>(availableActors.size());
            clone.availableActors.addAll(availableActors);
        }
        return clone;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(sceneData, "sceneData", null);
        oc.write(guiSceneData, "guiSceneData", null);
        if (gameLogicDatas != null) {
            oc.writeSavableArrayList(new ArrayList<GameLogicData>(gameLogicDatas), "gameLogicDatas", null);
        }
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        sceneData = (SceneData) ic.readSavable("sceneData", null);
        guiSceneData = (SceneData) ic.readSavable("guiSceneData", null);
        gameLogicDatas = ic.readSavableArrayList("gameLogicDatas", null);
    }
}
