/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import name.huliqing.core.data.SceneData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.scene.Scene;

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
