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
import name.huliqing.core.data.module.ModuleData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.xml.DataProcessor;
import name.huliqing.core.object.module.Module;

/**
 * 角色
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
            throw new IllegalStateException("Actor already initialized! actor=" + this);
        }
        
        // 载入基本模型并添加当前控制器
        loadModel().addControl(this);
        
        // 载入并初始化所有控制器
        if (data.getModuleDatas() != null) {
            // 注：这里要把data.getModuleDatas迁移到tempMDS中，因为addModule的时候会重新添加进去，
            // 要避免重复添加，并且避免循环时modify异常
            List<ModuleData> tempMDS= new ArrayList<ModuleData>(data.getModuleDatas());
            data.getModuleDatas().clear();
            for (ModuleData cd : tempMDS) {
                addModule((Module)Loader.load(cd));
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
        for (Module module : modules) {
            module.cleanup();
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
        data.getModuleDatas().add(module.getData());
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
        data.getModuleDatas().remove(module.getData());
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
        return ActorModelLoader.loadActorModel(data);
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        // ignore
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        // ignore
    }
}
