/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.data.SceneData;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.object.scene.Scene;

/**
 * just for test
 * @author huliqing
 */
public class SceneServiceImpl implements SceneService {

    @Override
    public void inject() {
        // 
    }

    @Override
    public Scene loadScene(String sceneId) {
        return Loader.loadScene(sceneId);
    }

    @Override
    public Scene loadScene(SceneData sceneData) {
        return Loader.loadScene(sceneData);
    }

}
