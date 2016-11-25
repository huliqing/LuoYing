/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity;

import com.jme3.util.SafeArrayList;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.attribute.AttributeManager;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.xml.ObjectData;

/**
 * Entity的基类
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractEntity<T extends EntityData> implements Entity<T> {
    
    protected T data;
    protected Scene scene;
    protected boolean initialized;
    
    /** 属性管理器 */
    protected final EntityAttributeManager attributeManager = new EntityAttributeManager(this);
    
    /** 模块管理器 */
    protected final ModuleManager moduleManager = new ModuleManager(this);
    
    /** 属性侦听器列表 */
    protected SafeArrayList<EntityAttributeListener> attributeListeners;
    
    /** 数据侦听器列表 */
    protected SafeArrayList<EntityDataListener> dataListeners;
    
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
        // 更新属性值
        attributeManager.updateDatas();
        // 更新所有模块内容
        moduleManager.updateDatas();
    }
    
    @Override
    public final void initialize() {
        if (initialized) {
            throw new IllegalStateException("Entity already is initialized! entityId=" + data.getId());
        }
        
        // 1.物体自身的"基本初始化"，这一步必须放在模块初始化之前。
        initEntity();
        
        // 2.物体属性的初始化.
        attributeManager.initialize(this);
        
        // 3.物体附加模块的初始化, 注：模块的初始化需要放在”基本初始化“及“属性初始化”之后，
        // 因为模块是控制器，模块的初始化可能会引用物体及属性的配置，需要确保物体自身已经初始化完毕,。
        moduleManager.initialize();
        
        initialized = true;
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        // 清理模块，因为modules是有依赖顺序的,可能存在一些module，这些module在清理的时候会依赖于
        moduleManager.cleanup();
        
//        // 属性功能不支持清理
//        attributeManager.cleanup;
        
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
    public AttributeManager getAttributeManager() {
        return attributeManager;
    }

    @Override
    public ModuleManager getModuleManager() {
        return moduleManager;
    }
    
    @Override
    public void hitAttribute(String attribute, Object hitValue, Entity hitter) {
        Attribute attr = attributeManager.getAttribute(attribute);
        if (attr == null) {
            return;
        }
        // 在击中属性之前先通知侦听器
        if (attributeListeners != null && !attributeListeners.isEmpty()) {
            for (EntityAttributeListener lis : attributeListeners.getArray()) {
                lis.onHitAttributeBefore(attr, hitValue, hitter);
            }
        }
        
        // 应用属性值。
        Object oldValue = attr.getValue();
        attr.setValue(hitValue);
        
        // 击中后再通知侦听器一次。
        if (attributeListeners != null && !attributeListeners.isEmpty()) {
            for (EntityAttributeListener lis : attributeListeners.getArray()) {
                lis.onHitAttributeAfter(attr, hitValue, hitter, oldValue);
            }
        }
    }

    @Override
    public boolean addObjectData(ObjectData data, int count) {
        boolean added = moduleManager.addData(data, count);
        if (added && dataListeners != null && !dataListeners.isEmpty()) {
            for (EntityDataListener lis : dataListeners.getArray()) {
                lis.onDataAdded(data, count);
            }
        }
        return added;
    }
    
    @Override
    public boolean removeObjectData(ObjectData data, int count) {
        boolean removed = moduleManager.removeData(data, count);
        if (removed && dataListeners != null && !dataListeners.isEmpty()) {
            for (EntityDataListener lis : dataListeners.getArray()) {
                lis.onDataRemoved(data, count);
            }
        }
        return removed;
    }
    
    @Override
    public boolean useObjectData(ObjectData data) {
        boolean used = moduleManager.useData(data);
        if (used && dataListeners != null && !dataListeners.isEmpty()) {
            for (EntityDataListener lis : dataListeners.getArray()) {
                lis.onDataUsed(data);
            }
        }
        return used;
    }
    
    @Override
    public void addEntityAttributeListener(EntityAttributeListener listener) {
        if (attributeListeners == null) {
            attributeListeners = new SafeArrayList<EntityAttributeListener>(EntityAttributeListener.class);
        }
        if (!attributeListeners.contains(listener)) {
            attributeListeners.add(listener);
        }
    }
    
    @Override
    public boolean removeEntityAttributeListener(EntityAttributeListener listener) {
        return attributeListeners != null && attributeListeners.remove(listener);
    }
    
    @Override
    public void addEntityDataListener(EntityDataListener listener) {
        if (dataListeners == null) {
            dataListeners = new SafeArrayList<EntityDataListener>(EntityDataListener.class);
        }
        if (!dataListeners.contains(listener)) {
            dataListeners.add(listener);
        }
    }
    
    @Override
    public boolean removeEntityDataListener(EntityDataListener listener) {
        return dataListeners != null && dataListeners.remove(listener);
    }
    
    /**
     * 初始化物体, 这个方法会在物体载入后调用一次，对物体进行初始化, 
     * 每个物体在载入系统后都应该立即调用这个方法来对物体进行初始化。
     */
    protected abstract void initEntity(); 
}
