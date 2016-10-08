/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.actor;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import name.huliqing.ly.data.ActorData;
import com.jme3.scene.Spatial;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.data.ModuleData;
import name.huliqing.ly.object.AbstractSceneObject;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.module.ActorModule;
import name.huliqing.ly.object.module.Module;
import name.huliqing.ly.object.scene.Scene;

/**
 * 角色，角色由数据(ObjectData)和模块处理器(Module)组成。
 * @author huliqing
 */
public class Actor extends AbstractSceneObject<ActorData> {
 
    protected Spatial model;
    
    /**
     * 所有的模块,这里面包含logicModules中的模块。
     */
    protected final SafeArrayList<Module> modules = new SafeArrayList<Module>(Module.class);

    @Override
    public void updateDatas() {
        for (Module module : modules.getArray()) {
            module.updateDatas();
        }
        if (initialized) {
            data.setLocation(model.getLocalTranslation());
            data.setRotation(model.getLocalRotation());
            data.setScale(model.getLocalScale());
        }
    }
    
    /**
     * 初始化角色
     * @param scene
     */
    @Override
    public void initialize(Scene scene) {
        super.initialize(scene);
        
        // 载入基本模型并添加当前控制器
        model = loadModel();
        
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
        
        scene.addSpatial(model);
        
        initialized = true;
    }
    
    /**
     * 清理角色
     */
    @Override
    public void cleanup() {
        // 这里要注意反向清理，因为modules是有依赖顺序的,可能存在一些module，这些module在清理的时候会依赖于
        // 其它module.
        for (int i = modules.size() - 1; i >= 0; i--) {
            modules.get(i).cleanup();
        }
        modules.clear();
        super.cleanup();
    }
    
    /**
     * 添加角色扩展模块
     * @param module 
     */
    public void addModule(Module module) {
        if (modules.contains(module)) {
            return;
        }
        modules.add(module);
        data.addModuleData(module.getData());
        module.initialize(this);
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
        data.removeModuleData(module.getData());
        module.cleanup();
        return true;
    }
    
    /**
     * 从角色身上获取指定类型的模块, 这个方法返回第一个符合类型的实例，如果找不到符合类型的实例则返回null.
     * @param <T>
     * @param moduleType
     * @return 
     */
    public <T extends Module> T  getModule(Class<T> moduleType) {
        for (Module m : modules.getArray()) {
            if (moduleType.isAssignableFrom(m.getClass())) {
                return (T) m;
            }
        }
        return null;
    }
    
    /**
     * 载入基本模型
     * @return 
     */
    protected Spatial loadModel() {
        return ActorModelLoader.loadActorModel(this);
    }
    
    /**
     * 获取角色模型
     * @return 
     */
    public Spatial getSpatial() {
        return model;
    }

    @Override
    public Vector3f getLocation() {
        if (initialized) {
            return model.getLocalTranslation();
        } 
        return data.getLocation();
    }

    @Override
    public void setLocation(Vector3f location) {
        if (initialized) {
            ActorModule am = getModule(ActorModule.class);
            if (am != null) {
                am.setLocation(location);
                return;
            }
        }
        data.setLocation(location);
    }

    @Override
    public Quaternion getRotation() {
        if (initialized) {
            return model.getLocalRotation();
        }
        return data.getRotation();
    }

    @Override
    public void setRotation(Quaternion rotation) {
        if (initialized) {
            ActorModule am = getModule(ActorModule.class);
            if (am != null) {
                am.setRotation(rotation);
                return;
            }
        }
        data.setRotation(rotation);
    }

    @Override
    public Vector3f getScale() {
        if (initialized) {
            return model.getLocalScale();
        }
        return data.getScale();
    }

    @Override
    public void setScale(Vector3f scale) {
        if (initialized) {
            ActorModule am = getModule(ActorModule.class);
            if (am != null) {
                am.setScale(scale);
                return;
            }
        }
        data.setScale(scale);
    }
    
}
