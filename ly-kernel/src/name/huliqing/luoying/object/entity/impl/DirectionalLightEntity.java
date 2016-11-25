/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity.impl;

import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.math.Vector3f;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.entity.LightEntity;
import name.huliqing.luoying.object.entity.NonModelEntity;
import name.huliqing.luoying.object.scene.Scene;

/**
 * @author huliqing
 */
public class DirectionalLightEntity extends NonModelEntity implements LightEntity{

    private final DirectionalLight light = new DirectionalLight();
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        light.setColor(data.getAsColor("color", light.getColor()));
        Vector3f direction = data.getAsVector3f("direction");
        if (direction != null) {
            light.setDirection(direction);
        }
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("color", light.getColor());
        data.setAttribute("direction", light.getDirection());
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
    
//    private Spatial createDebugArrow(DirectionalLight light) {
//        Node node = new Node();
//        Spatial arrow1 = DebugUtils.createArrow(Vector3f.ZERO, Vector3f.ZERO.add(light.getDirection().normalize()), light.getColor());
//        Spatial arrow2 = arrow1.clone();
//        Spatial arrow3 = arrow1.clone();
//        arrow1.setLocalTranslation(0, 3, 0);
//        arrow2.setLocalTranslation(0, 3.5f, 0);
//        arrow3.setLocalTranslation(0, 4, 0);
//        node.attachChild(arrow1);
//        node.attachChild(arrow2);
//        node.attachChild(arrow3);
//        return node;
//    }


    
}
