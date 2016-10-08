/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.scene;

import com.jme3.post.Filter;
import com.jme3.post.SceneProcessor;
import com.jme3.scene.Spatial;
import name.huliqing.ly.object.SceneObject;

/**
 * 场景侦听器
 * @author huliqing
 */
public interface SceneListener {

    /**
     * 当容器初始化完毕后该方法被立即调用
     * @param scene 
     */
    void onSceneInitialized(Scene scene);

    /**
     * 当容器添加了一个物体之后该方法被立即调用。
     * @param scene
     * @param objectAdded 
     */
    void onSceneObjectAdded(Scene scene, SceneObject objectAdded);

    /**
     * 当容器移除了一个物体之后该方法被立即调用
     * @param scene
     * @param objectRemoved 
     */
    void onSceneObjectRemoved(Scene scene, SceneObject objectRemoved);
    
    /**
     * 当场景中新添加了物体后该方法被调用。
     * @param scene
     * @param spatialAdded 新添加的物体
     */
    void onSpatialAdded(Scene scene, Spatial spatialAdded);
    
    /**
     * 当场景中移除了物体后该方法被调用
     * @param scene
     * @param spatialRemoved 被移除的物体
     */
    void onSpatialRemoved(Scene scene, Spatial spatialRemoved);
    
    /**
     * 当场景添加了一个Scene Processor之后该方法被调用
     * @param scene
     * @param processorAdded 新添加的Scene Processor
     */
    void onProcessorAdded(Scene scene, SceneProcessor processorAdded);
    
    /**
     * 当场景移除了一个Scene Processor之后该方法被调用。
     * @param scene
     * @param processorRemoved 已被移除的Scene Processor
     */
    void onProcessorRemoved(Scene scene, SceneProcessor processorRemoved);
    
//    /**
//     * 当场景添加了一个Filter之后该方法被调用
//     * @param scene
//     * @param filterAdded 新添加的Filter
//     */
//    void onFilterAdded(Scene scene, Filter filterAdded);
//    
//    /**
//     * 当场景移除了一个Filter之后该方法被调用
//     * @param scene
//     * @param filterRemoved 已被移除的Filter
//     */
//    void onFilterRemoved(Scene scene, Filter filterRemoved);
}
