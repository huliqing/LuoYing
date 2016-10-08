/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.env;

import com.jme3.app.Application;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import name.huliqing.ly.data.env.EnvData;
import name.huliqing.ly.object.scene.Scene;
import name.huliqing.ly.utils.DebugUtils;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class LightDirectionalEnv <T extends EnvData> extends AbstractEnv<T> {

    private boolean debug;
    private final DirectionalLight light = new DirectionalLight();
    private Vector3f direction = new Vector3f(-1,-1,-1);
    
    // ---- inner
    private Spatial debugNode;
    
    @Override
    public void setData(T data) {
        super.setData(data);
        debug = data.getAsBoolean("debug", debug);
        direction = data.getAsVector3f("direction", direction);
        light.setColor(data.getAsColor("color", light.getColor()));
    }

    @Override
    public void updateDatas() {
        // ignore
    }

    @Override
    public void initialize(Scene scene) {
        super.initialize(scene);
        
        light.setDirection(data.getRotation().multLocal(direction));
        scene.getRoot().addLight(light);
        
        if (debug) {
            debugNode = createDebugArrow(light);
            scene.addSpatial(debugNode);
        }
    }

    @Override
    public void cleanup() {
        scene.getRoot().removeLight(light);
        if (debugNode != null) {
            scene.removeSpatial(debugNode);
        }
        super.cleanup();
    }

    private Spatial createDebugArrow(DirectionalLight light) {
        Node node = new Node();
        Spatial arrow1 = DebugUtils.createArrow(Vector3f.ZERO, Vector3f.ZERO.add(light.getDirection().normalize()), light.getColor());
        Spatial arrow2 = arrow1.clone();
        Spatial arrow3 = arrow1.clone();
        arrow1.setLocalTranslation(0, 3, 0);
        arrow2.setLocalTranslation(0, 3.5f, 0);
        arrow3.setLocalTranslation(0, 4, 0);
        node.attachChild(arrow1);
        node.attachChild(arrow2);
        node.attachChild(arrow3);
        return node;
    }

    @Override
    public Vector3f getLocation() {
        return data.getLocation();
    }

    @Override
    public void setLocation(Vector3f location) {
        data.setLocation(location);
    }

    @Override
    public Quaternion getRotation() {
        return data.getRotation();
    }

    @Override
    public void setRotation(Quaternion rotation) {
        if (initialized) {
            light.setDirection(data.getRotation().multLocal(direction));
        }
        data.setRotation(rotation);
    }

    @Override
    public Vector3f getScale() {
        return Vector3f.UNIT_XYZ;
    }

    @Override
    public void setScale(Vector3f scale) {
        // ignore,直射光没有缩放的意义
    }
    
    
}
