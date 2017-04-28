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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.attribute.AttributeManager;
import name.huliqing.luoying.object.module.Module;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.xml.ObjectData;

/**
 * Entity的基类
 * @author huliqing
 */
public abstract class AbstractEntity implements Entity {
    
    protected EntityData data;
    protected boolean initialized;
    protected boolean enabled = true;
     
    /** 属性管理器 */
    protected final EntityAttributeManager attributeManager = new EntityAttributeManager(this);
    
    /** 模块管理器 */
    protected final SafeArrayList<Module> modules = new SafeArrayList<Module>(Module.class);
    
    /** 属性侦听器列表 */
    protected SafeArrayList<HitAttributeListener> hitAttributeListeners;
    
    /** 数据侦听器列表 */
    protected SafeArrayList<DataListener> dataListeners;
    
    /** 实体空间 */
    protected Spatial spatial;
    
    /** 实体所在的场景 */
    protected Scene scene;
    
    @Override
    public void setData(EntityData data) {
        if (this.data != null && this.data != data) {
            throw new IllegalStateException("Data is already set! could not change the data!");
        }
        this.data = data;
        this.enabled = data.getAsBoolean("enabled", enabled);
    }
    
    @Override
    public EntityData getData() {
        return data;
    }
    
    @Override
    public void updateDatas() {
        if (spatial != null) {
            data.setLocation(spatial.getLocalTranslation());
            data.setRotation(spatial.getLocalRotation());
            data.setScale(spatial.getLocalScale());
        }
        data.setAttribute("enabled", enabled);
        // 更新属性值
        attributeManager.updateDatas();
        // 更新所有模块内容
        for (Module module : modules.getArray()) {
            module.updateDatas();
        }
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
        
        // 载入并初始化所有模块，这里分两步处理:
        // 第一步先添加; 第二步再初始化;
        // 因为一些module在初始化的时候可能会引用到另一些module
        if (data.getModules() != null) {
            // 添加module
            List<ModuleData> tempMDS= new ArrayList<ModuleData>(data.getModules());
            for (ModuleData cd : tempMDS) {
                Module module = Loader.load(cd);
                modules.add(module);
            }
            // 初始化module
            for (Module module : modules) {
                module.setEntity(this);
                module.initialize();
            }
        }
        
        initialized = true;
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (spatial == null || scene == null) {
            return;
        }
        if (enabled) {
            scene.getRoot().attachChild(spatial);
        } else {
            spatial.removeFromParent();
        }
        scene.notifyEntityStateChanged(this);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void cleanup() {
        // 这里要注意反向清理，因为modules是有依赖顺序的,可能存在一些module，这些module在清理的时候会依赖于
        // 其它module.
        for (int i = modules.size() - 1; i >= 0; i--) {
            modules.get(i).cleanup();
        }
        modules.clear();
        
        // 属性清理
        attributeManager.cleanup();
        
        // 将Spatial清理出场景.
        // 注：Spatial不应该清理为null,这可能会导致bug, 因为getSpatial()方法会被外部大量调用，当Entity被清理后,
        // 一些外部逻辑还可能在引擎Entity.这就可能会导致NPE(NullPointerException)
        if (spatial != null) {
            spatial.removeFromParent();
        }

        // 清理后要取消对场景的引用
        scene = null;
        initialized = false;
    }
    
    @Override
    public void onInitScene(Scene scene) {
        this.scene = scene;
        if (isEnabled()) {
            scene.getRoot().attachChild(spatial);
        }
        // 通知所有模块，实体被添加到了场景中
        for (Module m : modules.getArray()) {
            m.onInitScene(scene);
        }
    }

    @Override
    public long getEntityId() {
        return data.getUniqueId();
    }
    
    @Override
    public Spatial getSpatial() {
        return spatial;
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
    public void addAttribute(Attribute attribute) {
        attributeManager.addAttribute(attribute);
    }

    @Override
    public boolean removeAttribute(Attribute attribute) {
        return attributeManager.removeAttribute(attribute);
    }

    @Override
    public List<Attribute> getAttributes() {
        return attributeManager.getAttributes();
    }
    
    @Override
    public <T extends Attribute> T getAttribute(String attrName) {
        return attributeManager.getAttribute(attrName);
    }
    
    @Override
    public <T extends Attribute> T getAttribute(String attrName, Class<T> type) {
        return attributeManager.getAttribute(attrName, type);
    }

    @Override
    public void hitAttribute(String attribute, Object hitValue, Entity hitter) {
        Attribute attr = attributeManager.getAttribute(attribute);
        if (attr == null) {
            return;
        }
        
        // 在击中属性之前先通知侦听器
        if (hitAttributeListeners != null && !hitAttributeListeners.isEmpty()) {
            for (HitAttributeListener lis : hitAttributeListeners.getArray()) {
                lis.onHitAttributeBefore(attr, hitValue, hitter);
            }
        }
        
        // 应用属性值。
        Object oldValue = attr.getValue();
        attr.setValue(hitValue);
        
        // 击中后再通知侦听器一次。
        if (hitAttributeListeners != null && !hitAttributeListeners.isEmpty()) {
            for (HitAttributeListener lis : hitAttributeListeners.getArray()) {
                lis.onHitAttributeAfter(attr, hitValue, hitter, oldValue);
            }
        }

    }

    @Override
    public void addHitAttributeListener(HitAttributeListener listener) {
        if (hitAttributeListeners == null) {
            hitAttributeListeners = new SafeArrayList<HitAttributeListener>(HitAttributeListener.class);
        }
        if (!hitAttributeListeners.contains(listener)) {
            hitAttributeListeners.add(listener);
        }
    }
    
    @Override
    public boolean removeHitAttributeListener(HitAttributeListener listener) {
        return hitAttributeListeners != null && hitAttributeListeners.remove(listener);
    }
    
    @Override
    public AttributeManager getAttributeManager() {
        return attributeManager;
    }
    
    @Override
    public void addModule(Module module) {
        if (modules.contains(module)) {
            return;
        }
        modules.add(module);
        data.addModuleData(module.getData());
        module.setEntity(this);
        module.initialize();
    }

    @Override
    public boolean removeModule(Module module) {
        if (!modules.contains(module)) {
            return false;
        }
        modules.remove(module);
        data.removeModuleData(module.getData());
        module.cleanup();
        return true;
    }

    /**
     * 返回所有的模块，返回的列表只能只读，不能直接修改。
     * @return 
     */
    @Override
    public List<Module> getModules() {
        return Collections.unmodifiableList(modules);
    }
    
    @Override
    public <T extends Module> T getModule(Class<T> moduleType) {
        for (Module m : modules.getArray()) {
            if (moduleType.isAssignableFrom(m.getClass())) {
                return (T) m;
            } 
        }
        return null;
    }
    
    @Override
    public boolean addObjectData(ObjectData dataAdd, int count) {
        boolean added = false;
        for (Module m : modules.getArray()) {
            if (!m.isEnabled()) 
                continue;
            added = added || m.handleDataAdd(dataAdd, count);
        }
        
        if (added && dataListeners != null) {
            for (DataListener lis : dataListeners.getArray()) {
                lis.onDataAdded(dataAdd, count);
            }
        }
        
        return added;
    }
    
    @Override
    public boolean removeObjectData(ObjectData dataRemove, int count) {
        // 如果有任何一个模块移除了物体，则认为物体是成功移除了的.
        boolean removed = false;
        for (Module m : modules.getArray()) {
            if (!m.isEnabled()) 
                continue;
            removed = removed || m.handleDataRemove(dataRemove, count);
        }
        
        if (removed && dataListeners != null) {
            for (DataListener lis : dataListeners.getArray()) {
                lis.onDataRemoved(dataRemove, count);
            }
        }
        
        return removed;
    }
    
    @Override
    public boolean useObjectData(ObjectData dataUse) {
        // 如果有任何一个模块使用了物体，则认为物体是成功使用了的.
        boolean used = false;
        for (Module m : modules.getArray()) {
            if (!m.isEnabled()) 
                continue;
            used = used || m.handleDataUse(dataUse);
        }
        
        if (used && dataListeners != null) {
            for (DataListener lis : dataListeners.getArray()) {
                lis.onDataUsed(dataUse);
            }
        }
        return used;
    }

    @Override
    public void addDataListener(DataListener listener) {
        if (dataListeners == null) {
            dataListeners = new SafeArrayList<DataListener>(DataListener.class);
        }
        if (!dataListeners.contains(listener)) {
            dataListeners.add(listener);
        }
    }
    
    @Override
    public boolean removeDataListener(DataListener listener) {
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
