/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity;

import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.module.Module;

/**
 * 这个类用来代理管理Entity的模块
 * @author huliqing
 */
public class EntityModule {
    
    /**
     * 实体
     */
    private final Entity entity;
    
    /**
     * Entity的模块列表
     */
    private final SafeArrayList<Module> modules = new SafeArrayList<Module>(Module.class);
    
    public EntityModule(Entity entity) {
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
        // 载入并初始化所有模块，这里分两步处理:
        // 第一步先添加;
        // 第二步再初始化;
        // 因为一些module在初始化的时候可能会引用到另一些module
        if (entity.getData().getModuleDatas() != null) {
            // 添加module
            List<ModuleData> tempMDS= new ArrayList<ModuleData>(entity.getData().getModuleDatas());
            for (ModuleData cd : tempMDS) {
                modules.add((Module)Loader.load(cd));
            }
            
            // 初始化module
            for (Module module : modules) {
                module.initialize(entity);
            }
        }
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
}
