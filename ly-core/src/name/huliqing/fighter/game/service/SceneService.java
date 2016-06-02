/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.Inject;
import name.huliqing.fighter.data.SceneData;
import name.huliqing.fighter.object.scene.Scene;

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
