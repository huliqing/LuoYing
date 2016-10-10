/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.env;

import com.jme3.light.AmbientLight;
import name.huliqing.ly.data.EntityData;
import name.huliqing.ly.object.entity.NoneModelEntity;
import name.huliqing.ly.object.scene.Scene;

/**
 * @author huliqing
 */
public class LightAmbientEnv extends NoneModelEntity {

    private final AmbientLight light = new AmbientLight();
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        light.setColor(data.getAsColor("color", light.getColor()));
    }

    @Override
    public void updateDatas() {
        // ignore
    }
    
    @Override
    public void initialize(Scene scene) {
        super.initialize(scene);
        scene.getRoot().addLight(light);
    }
    
    @Override
    public void cleanup() {
        scene.getRoot().removeLight(light);
        super.cleanup(); 
    }
    
}
