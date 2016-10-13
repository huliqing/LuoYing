/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.entity;

import name.huliqing.ly.data.EntityData;
import name.huliqing.ly.object.scene.Scene;

/**
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractEntity<T extends EntityData> implements Entity<T> {
    
    protected final EntityModule entityModule = new EntityModule(this);
    
    protected T data;
    protected boolean initialized;
    protected Scene scene;
    
    @Override
    public void setData(T data) {
        if (this.data != null && this.data != data) {
            throw new IllegalStateException("Data is already set! could not change the data!");
        }
        this.data = data;
    }
    
    @Override
    public T getData() {
        return data;
    }
    
    @Override
    public void updateDatas() {
        // 更新所有模块内容
        entityModule.updateDatas();
    }
    
    @Override
    public void initialize(Scene scene) {
        if (initialized) {
            throw new IllegalStateException("Entity already is initialized! entityId=" + data.getId());
        }
        this.scene = scene;
        initialized = true;
        
        // 初始化所有模块
        entityModule.initialize();
        
        setcontrols
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        // 清理模块，因为modules是有依赖顺序的,可能存在一些module，这些module在清理的时候会依赖于
        entityModule.cleanup();
        
        // 清理后要取消对场景的引用
        scene = null;
        initialized = false;
    }
    
    @Override
    public long getEntityId() {
        return data.getUniqueId();
    }
    
    @Override
    public Scene getScene() {
        return scene;
    }
    
    @Override
    public boolean removeFromScene() {
        if (scene != null) {
            scene.removeEntity(this);
            return true;
        }
        return false;
    }

    @Override
    public EntityModule getEntityModule() {
        return entityModule;
    }
    
}
