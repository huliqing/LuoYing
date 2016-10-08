/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.data;

import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huliqing
 */
@Serializable
public class SceneData extends ObjectData {
    
    // 环境物体
    private List<ObjectData> sceneObjectDatas;
    
    /**
     * @return 
     */
    public List<ObjectData> getSceneObjectDatas() {
        return sceneObjectDatas;
    }

    /**
     * @param sceneObjectDatas 
     */
    public void setSceneObjectDatas(List<ObjectData> sceneObjectDatas) {
        this.sceneObjectDatas = sceneObjectDatas;
    }
    
    /**
     * @param sceneObjectData 
     */
    public void addSceneObjectData(ObjectData sceneObjectData) {
        if (sceneObjectDatas == null) {
            sceneObjectDatas = new ArrayList<ObjectData>();
        }
        if (!sceneObjectDatas.contains(sceneObjectData)) {
            sceneObjectDatas.add(sceneObjectData);
        }
    }
    
    /**
     * @param sceneObjectData
     * @return 
     */
    public boolean removeEntityData(ObjectData sceneObjectData) {
        return sceneObjectDatas != null && sceneObjectDatas.remove(sceneObjectData);
    }
}
