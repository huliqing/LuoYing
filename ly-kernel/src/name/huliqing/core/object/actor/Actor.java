/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actor;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import name.huliqing.core.data.ActorData;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.ModuleData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.xml.DataProcessor;
import name.huliqing.core.object.module.Module;

/**
 * 角色，角色由数据(ObjectData)和模块处理器(Module)组成。
 * @author huliqing
 */
public class Actor extends AbstractControl implements DataProcessor<ActorData> {
 
    protected ActorData data;
    protected boolean initialized;
    
    /**
     * 所有的模块,这里面包含logicModules中的模块。
     */
    protected final SafeArrayList<Module> modules = new SafeArrayList<Module>(Module.class);
    
    @Override
    public void setData(ActorData data) {
        this.data = data;
    }

    @Override
    public ActorData getData() {
        return data;
    }
    
    /**
     * 初始化角色
     */
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("Actor already initialized! actorId=" + data.getId());
        }
        
        // 载入基本模型并添加当前控制器
        loadModel().addControl(this);
        
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
        
        initialized = true;
    }
    
    /**
     * 判断角色是否已经初始化。
     * @return 
     */
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * 清理角色
     */
    public void cleanup() {
        // 这里要注意反向清理，因为modules是有依赖顺序的,可能存在一些module，这些module在清理的时候会依赖于
        // 其它module.
        for (int i = modules.size() - 1; i >= 0; i--) {
            modules.get(i).cleanup();
        }
        modules.clear();
        initialized = false;
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
    
    // ignore
    @Override
    protected void controlUpdate(float tpf) {}

    // ignore
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}
    
}
