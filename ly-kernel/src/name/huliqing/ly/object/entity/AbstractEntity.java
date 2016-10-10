/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.entity;

import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.data.EntityData;
import name.huliqing.ly.data.ModuleData;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.module.Module;
import name.huliqing.ly.object.scene.Scene;

/**
 * @author huliqing
 */
public abstract class AbstractEntity implements Entity {
    
    protected EntityData data;
    protected boolean initialized;
    protected Scene scene;
    
    /**
     * 所有的模块,这里面包含logicModules中的模块。
     */
    protected final SafeArrayList<Module> modules = new SafeArrayList<Module>(Module.class);
    
    @Override
    public void setData(EntityData data) {
        if (this.data != null && this.data != data) {
            throw new IllegalStateException("Data is already set! could not change the data!");
        }
        this.data = data;
    }
    
    @Override
    public EntityData getData() {
        return data;
    }
    
    @Override
    public void updateDatas() {
        if (initialized) {
            for (Module module : modules.getArray()) {
                module.updateDatas();
            }
        }
    }
    
    @Override
    public void initialize(Scene scene) {
        if (initialized) {
            throw new IllegalStateException("Entity already is initialized! entityId=" + data.getId());
        }
        this.scene = scene;
        initialized = true;
        
        // 载入并初始化所有控制器，这里分两步处理:
        // 第一步先添加;
        // 第二步再初始化;
        // 因为一些module在初始化的时候可能会引用到另一些module
        if (data.getModuleDatas() != null) {
            // 添加module
            List<ModuleData> tempMDS= new ArrayList<ModuleData>(data.getModuleDatas());
            for (ModuleData cd : tempMDS) {
                modules.add((Module)Loader.load(cd));
            }
            
            // 初始化module
            for (Module module : modules) {
                module.initialize(this);
            }
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        // 这里要注意反向清理，因为modules是有依赖顺序的,可能存在一些module，这些module在清理的时候会依赖于
        // 其它module.
        for (int i = modules.size() - 1; i >= 0; i--) {
            modules.get(i).cleanup();
        }
        modules.clear();
        
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
    public void addModule(Module module) {
        if (modules.contains(module)) {
            return;
        }
        modules.add(module);
        data.addModuleData(module.getData());
        module.initialize(this);
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
    
    @Override
    public <T extends Module> T getModule(Class<T> moduleType) {
        for (Module m : modules.getArray()) {
            if (moduleType.isAssignableFrom(m.getClass())) {
                return (T) m;
            }
        }
        return null;
    }
}
