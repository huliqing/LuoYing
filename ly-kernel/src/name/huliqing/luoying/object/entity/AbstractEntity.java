/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity;

import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.scene.Scene;

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
    public final void initialize() {
        if (initialized) {
            throw new IllegalStateException("Entity already is initialized! entityId=" + data.getId());
        }
        
        // 1.物体自身的"基本初始化"，这一步必须放在模块初始化之前。
        initEntity();
        
        // 2.物体附加模块的初始化, 注：模块的初始化需要放在”基本初始化“之后，
        // 因为模块是额外控制器，模块的初始化需要确保物体自身已经初始化完毕，是一个完整的物体。
        entityModule.initialize();
        
        initialized = true;
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
    public void onInitScene(Scene scene) {
        this.scene = scene;
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
        
    /**
     * 初始化物体, 这个方法会在物体载入后调用一次，对物体进行初始化, 
     * 每个物体在载入系统后都应该立即调用这个方法来对物体进行初始化。
     */
    protected abstract void initEntity(); 
}
