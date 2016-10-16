/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.scene;

import com.jme3.scene.Spatial;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 场景侦听器的适配器
 * @author huliqing
 */
public class SceneListenerAdapter implements SceneListener {

    @Override
    public void onSceneInitialized(Scene scene) {
        // 子类覆盖
    }

    @Override
    public void onSceneEntityAdded(Scene scene, Entity objectAdded) {
        // 子类覆盖
    }

    @Override
    public void onSceneEntityRemoved(Scene scene, Entity objectRemoved) {
        // 子类覆盖
    }
    
}
