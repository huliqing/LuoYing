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
import name.huliqing.core.loader.Loader;
import name.huliqing.core.object.actormodule.AbstractLogicActorModule;
import name.huliqing.core.object.actormodule.ActorModule;
import name.huliqing.core.xml.DataProcessor;

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
    protected final SafeArrayList<ActorModule> modules = new SafeArrayList<ActorModule>(ActorModule.class);
    
    /**
     * 逻辑模块,这些模块需要"持续"更新,需要实现update(tpf)功能。
     */
    protected final SafeArrayList<AbstractLogicActorModule> logicModules = 
            new SafeArrayList<AbstractLogicActorModule>(AbstractLogicActorModule.class);
    
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
        // 载入基本模型并添加当前控制器
        loadModel().addControl(this);
        
        // 载入并初始化所有控制器
        if (data.getModuleDatas() != null) {
            // 注：这里要把data.getModuleDatas迁移到tempMDS中，因为addModule的时候会重新添加进去，
            // 要避免重复添加，并且避免循环时modify异常
            List<ModuleData> tempMDS= new ArrayList<ModuleData>(data.getModuleDatas());
            data.getModuleDatas().clear();
            for (ModuleData cd : tempMDS) {
                addModule((ActorModule)Loader.load(cd));
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
    
    @Override
    protected void controlUpdate(float tpf) {
        // 更新逻辑模块
        for (AbstractLogicActorModule module : logicModules.getArray()) {
            module.update(tpf);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}
    
    /**
     * 清理角色
     */
    public void cleanup() {
        for (ActorModule module : modules) {
            module.cleanup();
        }
        modules.clear();
        logicModules.clear();
        initialized = false;
    }
    
    /**
     * 添加角色扩展模块
     * @param actorModule 
     */
    public void addModule(ActorModule actorModule) {
        if (modules.contains(actorModule)) {
            return;
        }
        modules.add(actorModule);
        if (actorModule instanceof AbstractLogicActorModule) {
            logicModules.add((AbstractLogicActorModule) actorModule);
        }
        data.getModuleDatas().add(actorModule.getData());
        actorModule.setActor(this);
        actorModule.initialize();
    }
    
    /**
     * 移除指定的角色模块
     * @param actorModule 
     * @return  
     */
    public boolean removeModule(ActorModule actorModule) {
        if (!modules.contains(actorModule)) {
            return false;
        }
        modules.remove(actorModule);
        if (actorModule instanceof AbstractLogicActorModule) {
            logicModules.remove((AbstractLogicActorModule) actorModule);
        }
        data.getModuleDatas().remove(actorModule.getData());
        actorModule.cleanup();
        return true;
    }
    
    /**
     * 从角色身上获取指定类型的模块, 这个方法返回第一个符合类型的实例，如果找不到符合类型的实例则返回null.
     * @param <T>
     * @param moduleType
     * @return 
     */
    public <T extends ActorModule> T  getModule(Class<T> moduleType) {
        for (ActorModule m : modules.getArray()) {
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
    
}
