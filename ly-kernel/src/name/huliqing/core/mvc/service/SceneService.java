/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import name.huliqing.core.Inject;
import name.huliqing.core.data.SceneData;
import name.huliqing.core.object.scene.Scene;

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
