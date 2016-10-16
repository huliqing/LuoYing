/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.Inject;
import name.huliqing.luoying.data.SceneData;
import name.huliqing.luoying.object.scene.Scene;

/**
 *
 * @author huliqing
 */
public interface SceneService extends Inject {
    
    /**
     * 载入场景
     * @param sceneId
     * @return 
     */
    Scene loadScene(String sceneId);
    
    /**
     * 载入场景
     * @param sceneData
     * @return 
     */
    Scene loadScene(SceneData sceneData);

    /**
     * 获取场景指定位置高度点，如果位置点超出地形外部，则该方法可能返回null.
     * @param scene
     * @param x
     * @param z
     * @return 
     */
    Vector3f getSceneHeight(Scene scene, float x, float z);
}
