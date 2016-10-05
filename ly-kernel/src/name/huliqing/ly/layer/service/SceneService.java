/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import name.huliqing.ly.Inject;
import name.huliqing.ly.data.SceneData;
import name.huliqing.ly.object.scene.Scene;

/**
 *
 * @author huliqing
 */
public interface SceneService extends Inject {
    
    /**
     * 载入场景
     * @return 
     */
    Scene loadScene(String sceneId);
    
    /**
     * 载入场景
     * @param sceneData
     * @return 
     */
    Scene loadScene(SceneData sceneData);

}
