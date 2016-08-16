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
import java.util.List;
import name.huliqing.core.data.ControlData;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.object.control.ActorControl;
import name.huliqing.core.xml.DataProcessor;

/**
 * 角色
 * @author huliqing
 * @param <T>
 */
public class Actor<T extends ActorData> extends AbstractControl implements DataProcessor<T> {
    
    protected T data;
    protected boolean initialized;
    
    /**
     *  角色模型
     */
    protected Spatial model;
    
    /**
     * 角色控制器
     */
    protected final SafeArrayList<ActorControl> controls = new SafeArrayList<ActorControl>(ActorControl.class);
    
    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public T getData() {
        return data;
    }
    
    /**
     * 初始化角色
     */
    public void initialize() {
        // 载入基本模型并添加当前控制器
        model = loadModel();
        model.addControl(this);
        
        // 载入并初始化所有控制器
        List<ControlData> controlDatas= data.getControlDatas();
        if (!controlDatas.isEmpty()) {
            for (ControlData cd : controlDatas) {
                addControl((ActorControl) Loader.load(cd));
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
    protected void controlUpdate(float tpf) {}

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}
    
    /**
     * 清理角色
     */
    public void cleanup() {
        for (ActorControl c : controls) {
            c.cleanup();
        }
        controls.clear();
        initialized = false;
    }
    
    public Spatial getModel() {
        return model;
    }
    
    /**
     * 添加角色控制器
     * @param actorControl 
     */
    public void addControl(ActorControl actorControl) {
        if (controls.contains(actorControl)) {
            return;
        }
        model.addControl(actorControl);
        controls.add(actorControl);
        data.getControlDatas().add(actorControl.getData());
        actorControl.initialize(this);
    }
    
    /**
     * 移除指定的角色控制器
     * @param actorControl 
     * @return  
     */
    public boolean removeControl(ActorControl actorControl) {
        boolean result = controls.remove(actorControl);
        data.getControlDatas().remove(actorControl.getData());
        model.removeControl(actorControl);
        actorControl.cleanup();
        return result;
    }
    
    /**
     * 载入基本模型
     * @return 
     */
    protected Spatial loadModel() {
        return ActorModelLoader.loadActorModel(data);
    }
    
}
