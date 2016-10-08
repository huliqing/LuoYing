/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object;

import name.huliqing.ly.data.ObjectData;
import name.huliqing.ly.object.scene.Scene;

/**
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractSceneObject<T extends ObjectData> implements SceneObject<T> {

    protected T data;
    protected boolean initialized;
    protected Scene scene;
    
    @Override
    public void setData(T data) {
        this.data = data;
    }
    
    @Override
    public T getData() {
        return data;
    }

    /**
     * 覆盖这个方法来更新状态数据到data
     */
    @Override
    public void updateDatas() {
        // ignore
    }

    @Override
    public void initialize(Scene scene) {
        if (initialized) {
            throw new IllegalStateException("Entity already is initialized! entityId=" + data.getId());
        }
        this.scene = scene;
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        initialized = false;
    }

    @Override
    public long getObjectId() {
        return data.getUniqueId();
    }

    
}
