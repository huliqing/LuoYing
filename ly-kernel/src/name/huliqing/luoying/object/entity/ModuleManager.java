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

import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.module.Module;
import name.huliqing.luoying.xml.ObjectData;

/**
 * Entity模块管理器
 * @author huliqing
 */
public class ModuleManager {
    
    /** 实体 */
    private final Entity entity;
    
    private boolean initialized;
    
    /**
     * Entity的模块列表
     */
    private final SafeArrayList<Module> modules = new SafeArrayList<Module>(Module.class);
        
    private final SafeArrayList<DataHandler> handlers = new SafeArrayList<DataHandler>(DataHandler.class);
    
    public ModuleManager(Entity entity) {
        this.entity = entity;
    }
    
    public void updateDatas() {
        if (entity.isInitialized()) { 
            for (Module module : modules.getArray()) {
                module.updateDatas();
            }
        }
    }
    
    /**
     * 初始化Entity的所有模块
     */
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("ModuleManager already is initialized! entityId=" + entity.getData().getId());
        }
        
        // 载入并初始化所有模块，这里分两步处理:
        // 第一步先添加;
        // 第二步再初始化;
        // 因为一些module在初始化的时候可能会引用到另一些module
        if (entity.getData().getModuleDatas() != null) {
            // 添加module
            List<ModuleData> tempMDS= new ArrayList<ModuleData>(entity.getData().getModuleDatas());
            for (ModuleData cd : tempMDS) {
                Module module = Loader.load(cd);
                modules.add(module);
                if (module instanceof DataHandler) {
                    handlers.add((DataHandler) module);
                }
            }
            
            // 初始化module
            for (Module module : modules) {
                module.initialize(entity);
            }
        }
        initialized = true;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * 清理Entity的所有模块,执行所有模块的cleanup,然后把所有模块从Entity中移除
     */
    public void cleanup() {
        // 这里要注意反向清理，因为modules是有依赖顺序的,可能存在一些module，这些module在清理的时候会依赖于
        // 其它module.
        for (int i = modules.size() - 1; i >= 0; i--) {
            modules.get(i).cleanup();
        }
        modules.clear();
        handlers.clear();
        initialized = false;
    }
    
    /**
     * 给实体添加模块
     * @param module 
     */
    public void addModule(Module module) {
        if (modules.contains(module)) {
            return;
        }
        modules.add(module);
        if (module instanceof DataHandler) {
            handlers.add((DataHandler) module);
        }
        entity.getData().addModuleData(module.getData());
        module.initialize(entity);
    }

    /**
     * 移除指定的角色模块
     * @param module
     * @return 
     */
    public boolean removeModule(Module module) {
        if (!modules.contains(module)) {
            return false;
        }
        modules.remove(module);
        if (module instanceof DataHandler) {
            handlers.remove((DataHandler) module);
        }
        entity.getData().removeModuleData(module.getData());
        module.cleanup();
        return true;
    }
    
    /**
     * 从角色身上获取指定类型的模块, 这个方法返回第一个符合类型的实例，如果找不到符合类型的实例则返回null.
     * @param <T>
     * @param moduleType
     * @return 
     */
    public <T extends Module> T getModule(Class<T> moduleType) {
        for (Module m : modules.getArray()) {
            if (moduleType.isAssignableFrom(m.getClass())) {
                return (T) m;
            } 
        }
        return null;
    }
    
    final boolean addData(ObjectData data, int count) {
        boolean added = false;
        for (DataHandler h : handlers.getArray()) {
            if (h.getHandleType().isAssignableFrom(data.getClass())) {
                added = added || h.handleDataAdd(data, count);
            } 
        }
        return added;
    }
    
    final boolean removeData(ObjectData data, int count) {
        // 如果有任何一个模块移除了物体，则认为物体是成功移除了的.
        boolean removed = false;
        for (DataHandler h : handlers.getArray()) {
            if (h.getHandleType().isAssignableFrom(data.getClass())) {
                removed = removed || h.handleDataRemove(data, count);
            }
        }
        return removed;
    }
    
    final boolean useData(ObjectData data) {
        // 如果有任何一个模块使用了物体，则认为物体是成功使用了的.
        boolean used = false;
        for (DataHandler h : handlers.getArray()) {
            if (h.getHandleType().isAssignableFrom(data.getClass())) {
                used = used || h.handleDataUse(data);
            }
        }
        return used;
    }

}
