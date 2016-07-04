/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.app.Application;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import java.util.HashSet;
import java.util.Set;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.object.scene.Scene;
import name.huliqing.fighter.processor.SimpleWaterProcessor;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class WaterEnv<T extends EnvData>  extends Env<T> {

    @Override
    public void initData(T data) {
        super.initData(data); 
    }
    
    @Override
    public void initialize(Application app, Scene scene) {
        
    }
    
    private SimpleWaterProcessor createWaterProcessor() {
        return new SimpleWaterProcessor(Common.getAssetManager());
    }

    private class WaterControl extends AbstractControl {
        
        private Set<ViewPort> vp = new HashSet<ViewPort>(1);

        @Override
        protected void controlUpdate(float tpf) {
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
            
        }
        
    }
}
