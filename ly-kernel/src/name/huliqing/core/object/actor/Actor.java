/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actor;

import name.huliqing.core.data.ActorData;
import com.jme3.scene.Spatial;
import com.jme3.util.SafeArrayList;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.object.control.ActorControl;
import name.huliqing.core.xml.DataProcessor;

/**
 * 角色
 * @author huliqing
 * @param <T>
 */
public class Actor <T extends ActorData> implements DataProcessor<T> {
    
    protected T data;
    protected boolean initialized;
    
    /**
     *  角色模型
     */
    protected Spatial model;
    
    // 控制器
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
        // 载入基本模型
        model = loadModel();
        
        // 载入并初始化所有控制器
        String[] cArr = data.getAsArray("controls");
        if (cArr != null) {
            for (String c : cArr) {
                ActorControl control = Loader.load(c);
                model.addControl(control);
                controls.add(control);
                control.initialize();
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
     * 载入基本模型
     * @return 
     */
    protected Spatial loadModel() {
        return ActorModelLoader.loadActorModel(data);
    }
    
    
}
