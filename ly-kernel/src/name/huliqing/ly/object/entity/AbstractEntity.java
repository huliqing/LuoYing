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
    protected Scene scene;
    protected boolean initialized;
    
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
    public final void initialize(Scene scene) {
        if (initialized) {
            throw new IllegalStateException("Entity already is initialized! entityId=" + data.getId());
        }
        // 0.设置对场景的引用, 这确保子类在自身初始化的时候可以引用到场景。
        this.scene = scene;
        
        // 1.物体自身的"基本初始化"，这一步必须放在模块初始化之前。
        initialize();
        
        // 2.物体附加模块的初始化, 注：模块的初始化需要放在”基本初始化“之后，
        // 因为模块是额外控制器，模块的初始化需要确保物体自身已经初始化完毕，是一个完整的物体。
        entityModule.initialize();
        
        initialized = true;
    }
    
    /**
     * 初始化物体<br>
     * 注：这个方法会在模块初始化<b>之前</b>被调用。
     */
    protected abstract void initialize();
    
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
