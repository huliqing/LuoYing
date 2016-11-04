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
    
    /** 侦听器，侦听属性变动 */
    protected SafeArrayList<EntityListener> listeners;
    
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
        
        attributeManager.cleanup();
        
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
    public void addListener(EntityListener listener) {
        if (listeners == null) {
            listeners = new SafeArrayList<EntityListener>(EntityListener.class);
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    @Override
    public boolean removeListener(EntityListener listener) {
        return listeners != null && listeners.remove(listener);
    }

    @Override
    public void hitAttribute(String attribute, Object value, Entity hitter) {
        Attribute attr = attributeManager.getAttribute(attribute);
        if (attr == null) {
            return;
        }
        // 在击中属性之前先通知侦听器
        if (listeners != null) {
            for (EntityListener lis : listeners.getArray()) {
                lis.onHitAttributeBefore(attribute, value, hitter);
            }
        }
        // 击中、设置属性值之前先记住旧的属性值
        Object oldValue = attr.getValue();
        attr.setValue(value);
        // 击中后再通知侦听器一次。
        if (listeners != null) {
            for (EntityListener lis : listeners.getArray()) {
                lis.onHitAttributeAfter(attribute, attr.getValue(), oldValue, hitter);
            }
        }
        
        // remove20161104
//        Object oldValue = attr.getValue();
//        attr.setValue(value);
//        // 注意：newValue必须重新获取，因为一些属性类型可能会限制value的值，所以以重新获得的newValue为准
//        Object newValue = attr.getValue();
//        
//        // 通知当前entity, 已经被击中
//        notifyHitByTarget(hitter, attribute, newValue, oldValue);
//        
//        // 通知hitter，已经击中了一个目标
//        if (hitter instanceof AbstractEntity) {
//            ((AbstractEntity)hitter).notifyHitTarget(this, attribute, newValue, oldValue);
//        }
    }
    
    // remove20161104
//    /**
//     * 通知侦听器，让侦听器知道当前角色Hit了另一个目标角色.<br>
//     */
//    private void notifyHitByTarget(Entity hitter, String hitAttribute, Object newValue, Object oldValue) {
//        if (listeners != null) {
//            for (EntityListener lis : listeners) {
//                lis.onHitByTarget(this, hitter, hitAttribute, newValue, oldValue);
//            }
//        }
//    }
//    
//    /**
//     * 通知侦听器，让侦听器知道当前角色击中了一个目标角色。
//     * @param targetWhichBeHit 被击中的另一个目标角色
//     * @param hitAttribute 指定击中的是目标角色的哪一个属性
//     * @param newValue 击中后属性的值
//     * @param oldValue 击中前属性的值
//     */
//    private void notifyHitTarget(Entity targetWhichBeHit, String hitAttribute, Object newValue, Object oldValue) {
//        if (listeners != null) {
//            for (EntityListener lis : listeners) {
//                lis.onHitTarget(this, targetWhichBeHit, hitAttribute, newValue, oldValue);
//            }
//        }
//    }
    
    /**
     * 初始化物体, 这个方法会在物体载入后调用一次，对物体进行初始化, 
     * 每个物体在载入系统后都应该立即调用这个方法来对物体进行初始化。
     */
    protected abstract void initEntity(); 
}
