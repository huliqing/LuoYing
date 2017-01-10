/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.object.entity;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
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
    
    /** 实体空间 */
    protected Spatial spatial;
    
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
        if (spatial != null) {
            data.setLocation(spatial.getLocalTranslation());
            data.setRotation(spatial.getLocalRotation());
            data.setScale(spatial.getLocalScale());
        }
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
        
        spatial = initSpatial();
        Vector3f location = data.getLocation();
        if (location != null) {
            spatial.setLocalTranslation(location);
        }
        Quaternion rotation = data.getRotation();
        if (rotation != null) {
            spatial.setLocalRotation(data.getRotation());
        }
        Vector3f scale = data.getScale();
        if (scale != null) {
            spatial.setLocalScale(scale);
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
        
        // 属性清理
        attributeManager.cleanup();
        
        // 将Spatial清理出场景
        if (spatial != null) {
            spatial.removeFromParent();
            spatial = null;
        }

        // 清理后要取消对场景的引用
        scene = null;
        initialized = false;
    }

    @Override
    public Spatial getSpatial() {
        return spatial;
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
        // 当Entity被添加到场景的时候把Spatial放到场景中。
        scene.getRoot().attachChild(spatial);
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
     * 初始化实体空间, 该方法会在整个实体生命周期的初始化阶段被调用一次，该方法应该返回一个代表实体本身的空间存在。
     * 这个方法会在实体初始化的时候<b>最先</b>被调用, 之后会调用{@link #initEntity() }来初始化实体的行为。
     * @return 
     * @see #initEntity() 
     */
    protected abstract Spatial initSpatial();
    
    /**
     * 初始化实体, 该方法会在整个实体生命周期的初始化阶段被调用一次（在{@link #initSpatial() }之后）,
     * 并在属性实始化和模块初始化<b>之前</b>调用，子类可以实现这个方法来对实体的行为进行初始化。
     * @see #initSpatial() 
     */
    protected abstract void initEntity();
}
