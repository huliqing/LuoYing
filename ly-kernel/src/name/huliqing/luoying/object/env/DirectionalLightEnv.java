/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.env;

import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.entity.NoneModelEntity;

/**
 * @author huliqing
 */
public class DirectionalLightEnv extends NoneModelEntity {

    private final DirectionalLight light = new DirectionalLight();
    private Vector3f direction = new Vector3f(-1,-1,-1);
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        direction = data.getAsVector3f("direction", direction);
        light.setColor(data.getAsColor("color", light.getColor()));
    }
    
    @Override
    public void updateDatas() {
        // ignore
    }
    
    @Override
    public void initialize() {
        light.setDirection(direction);
        scene.getRoot().addLight(light);
    }

    @Override
    public void cleanup() {
        scene.getRoot().removeLight(light);
        super.cleanup();
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
