/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity.impl;

import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.entity.LightEntity;
import name.huliqing.luoying.object.entity.NonModelEntity;
import name.huliqing.luoying.object.scene.Scene;

/**
 * @author huliqing
 */
public class AmbientLightEntity extends NonModelEntity implements LightEntity {

    private final AmbientLight light = new AmbientLight();
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        light.setColor(data.getAsColor("color", light.getColor()));
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("color", light.getColor());
    }
    
    @Override
    public void initEntity() {}

    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene); 
        scene.getRoot().addLight(light);
    }
    
    @Override
    public void cleanup() {
        scene.getRoot().removeLight(light);
        super.cleanup(); 
    }
    
    @Override
    public Light getLight() {
        return light;
    }
    
}
