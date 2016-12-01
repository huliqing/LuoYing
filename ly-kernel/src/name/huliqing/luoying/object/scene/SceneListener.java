/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.scene;

import name.huliqing.luoying.object.entity.Entity;

/**
 * 场景侦听器
 * @author huliqing
 */
public interface SceneListener {

    /**
     * 当场景载入完毕后该方法被立即调用，这表示场景中的初始实体完全载入完毕。
     * @param scene 
     */
    void onSceneLoaded(Scene scene);

    /**
     * 当容器添加了一个物体之后该方法被立即调用。
     * @param scene
     * @param entityAdded 
     */
    void onSceneEntityAdded(Scene scene, Entity entityAdded);

    /**
     * 当容器移除了一个物体之后该方法被立即调用
     * @param scene
     * @param entityRemoved 
     */
    void onSceneEntityRemoved(Scene scene, Entity entityRemoved);
    
}
