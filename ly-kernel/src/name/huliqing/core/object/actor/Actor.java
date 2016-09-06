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
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.data.ModuleData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.xml.DataProcessor;
import name.huliqing.core.object.module.Module;

/**
 * 角色
 * @author huliqing
 */
public class Actor extends AbstractControl implements DataProcessor<ActorData> {
    
//    public interface ActorObjectListener {
//        
//        /**
//         * 当角色添加了物品时该方法被调用
//         * @param actor
//         * @param data 被添加的物体
//         * @param added 实际添加了的物品的数量
//         */
//        void onObjectAdded(Actor actor, ObjectData data, int added);
//        
//        /**
//         * 当从角色身上移除了物品时该方法被调用.
//         * @param actor
//         * @param data 被移除了物体数量的data,如果移除后的物体的数量为0，则该物体可能被移除出角色身上。
//         * @param removed 实际移除的物体的数量
//         */
//        void onObjectRemoved(Actor actor, ObjectData data, int removed);
//        
//        /**
//         * 当角色使用物体时该方法被调用,侦听器可以实现这个方法来处理物品的使用逻辑.
//         * @param actor
//         * @param data 
//         */
//        void onObjectUse(Actor actor, ObjectData data);
//        
//    }
    
    protected ActorData data;
    protected boolean initialized;
    
    /**
     * 所有的模块,这里面包含logicModules中的模块。
     */
    protected final SafeArrayList<Module> modules = new SafeArrayList<Module>(Module.class);
//    protected final List<ActorObjectListener> listeners = new ArrayList<ActorObjectListener>();
    
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
        // 这里要注意反向清理，因为modules是有依赖顺序的
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
    
    // ------------------------- 考虑增加的方法,统一物体的添加，移除，获取
    
//    /**
//     * 给角色添加物体.
//     * @param objectId
//     * @param amount
//     */
//    public void addObject(String objectId, int amount) {
//        if (amount <= 0) {
//            return;
//        }
//        ObjectData od = data.getObjectData(objectId);
//        if (od == null) {
//            od = Loader.load(objectId);
//            od.setTotal(amount);
//            data.addObjectData(od);
//        } else {
//            od.setTotal(od.getTotal() + amount);
//        }
//        if (!listeners.isEmpty()) {
//            for (int i = 0; i < listeners.size(); i++) {
//                listeners.get(i).onObjectAdded(this, od, amount);
//            }
//        }
//    }
//    
//    /**
//     * 移除物体，如果角色没有存在指定objectId的物体或者amount小于等于0则什么也不做。
//     * @param objectId
//     * @param amount
//     * @return  
//     */
//    public boolean removeObject(String objectId, int amount) {
//        if (amount <= 0 || data.getObjectDatas() == null)
//            return false;
//        
//        // 不存在指定物品
//        ObjectData od = data.getObjectData(objectId);
//        if (od == null) {
//            return false;
//        }
//        
//        int trueRemoved = amount;
//        int oldTotal = od.getTotal();
//        od.setTotal(od.getTotal() - amount);
//        if (od.getTotal() <= 0) {
//            data.getObjectDatas().remove(od);
//            trueRemoved = oldTotal;
//        }
//        
//        // 触发侦听器
//        if (!listeners.isEmpty()) {
//            for (int i = 0; i < listeners.size(); i++) {
//                listeners.get(i).onObjectRemoved(this, od, trueRemoved);
//            }
//        }
//        return true;
//    }
//    
//    /**
//     * 使用一个物体，由子类或侦听器去实现逻辑.
//     * @param data 
//     */
//    public void useObject(ObjectData data) {
//        // 触发侦听器
//        if (!listeners.isEmpty()) {
//            for (int i = 0; i < listeners.size(); i++) {
//                listeners.get(i).onObjectUse(this, data);
//            }
//        }
//    }
//    
//    public void addListener(ActorObjectListener listener) {
//        if (!listeners.contains(listener)) {
//            listeners.add(listener);
//        }
//    }
//    
//    public boolean removeListener(ActorObjectListener listener) {
//        return listeners.remove(listener);
//    }
    
    // ---------------------------------------------- 测试1----------------------------------------------------------------------
    //    /**
//     * 获得角色包裹内的物体
//     * @param <T>
//     * @param id
//     * @return 
//     */
//    public <T extends ObjectData> T getObject(String id) {
//        if (data.getObjectDatas() == null)
//            return null;
//        
//        for (ObjectData od : data.getObjectDatas()) {
//            if (od.getId().equals(id)) {
//                return (T) od;
//            }
//        }
//        return null;
//    }
//    
//    /**
//     * 获取所有物体
//     * @param store
//     * @return 
//     */
//    public List<ObjectData> getObjects(List<ObjectData> store) {
//        if (data.getObjectDatas() == null)
//            return store;
//        
//        if (store == null) {
//            store = new ArrayList<ObjectData>();
//        }
//        store.addAll(data.getObjectDatas());
//        return store;
//    }
//    
//    /**
//     * 获取指定类型的物体
//     * @param <T>
//     * @param store
//     * @param objectType
//     * @return 
//     */
//    public <T extends ObjectData> List<T> getObjects(List<T> store, Class<T> objectType) {
//        if (data.getObjectDatas() == null)
//            return store;
//        
//        if (store == null) {
//            store = new ArrayList<T>();
//        }
//        List<ObjectData> ods = data.getObjectDatas();
//        int size = ods.size();
//        ObjectData temp;
//        for (int i = 0; i < size; i++) {
//            temp = ods.get(i);
//            if (objectType.isAssignableFrom(temp.getClass())) {
//                store.add((T) temp);
//            }
//        }
//        return store;
//    }
}
