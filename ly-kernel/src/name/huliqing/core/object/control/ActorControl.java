/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import name.huliqing.core.data.ControlData;
import name.huliqing.core.xml.DataProcessor;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class ActorControl<T extends ControlData> extends AbstractControl implements DataProcessor<T> {
    
    private T data;
    private boolean initialized;

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public T getData() {
        return data;
    }
    
    public void initialize() {
        initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void cleanup() {
        initialized = false;
    }

    @Override
    protected final void controlUpdate(float tpf) {
        if (!initialized) 
            return;
        
        actorUpdate(tpf);
    }

    @Override
    protected final void controlRender(RenderManager rm, ViewPort vp) {
        if (!initialized)
            return;
        
        actorRender(rm, vp);
    }

    /**
     * 实现逻辑
     * @param tpf 
     */
    public abstract void actorUpdate(float tpf);

    public abstract void actorRender(RenderManager rm, ViewPort vp);
  
}
