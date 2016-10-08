/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.scene;

import com.jme3.post.SceneProcessor;
import com.jme3.scene.Spatial;
import name.huliqing.ly.object.SceneObject;

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
    public void onSceneObjectAdded(Scene scene, SceneObject objectAdded) {
        // 子类覆盖
    }

    @Override
    public void onSceneObjectRemoved(Scene scene, SceneObject objectRemoved) {
        // 子类覆盖
    }

    @Override
    public void onSpatialAdded(Scene scene, Spatial spatialAdded) {
        // 子类覆盖
    }

    @Override
    public void onSpatialRemoved(Scene scene, Spatial spatialRemoved) {
        // 子类覆盖
    }

    @Override
    public void onProcessorAdded(Scene scene, SceneProcessor processorAdded) {
        // 子类覆盖
    }

    @Override
    public void onProcessorRemoved(Scene scene, SceneProcessor processorRemoved) {
        // 子类覆盖
    }
    
}
