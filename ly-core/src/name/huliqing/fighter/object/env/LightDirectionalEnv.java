/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.app.Application;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.object.scene.Scene;
import name.huliqing.fighter.utils.DebugUtils;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class LightDirectionalEnv <T extends EnvData> extends AbstractEnv<T> {

    private boolean debug;
    private final DirectionalLight light = new DirectionalLight();
    
    // ---- inner
    private Spatial debugNode;
    
    @Override
    public void setData(T data) {
        super.setData(data);
        debug = data.getAsBoolean("debug", debug);
        light.setDirection(data.getAsVector3f("direction", light.getDirection()));
        light.setColor(data.getAsColor("color", light.getColor()));
    }

    @Override
    public void initialize(Application app, Scene scene) {
        super.initialize(app, scene);
        scene.addSceneLight(light);
        if (debug) {
            debugNode = createDebugArrow(light);
            scene.addSceneObject(debugNode);
        }
    }

    @Override
    public void cleanup() {
        if (scene != null) {
            scene.removeSceneLight(light);
            scene.removeSceneObject(debugNode);
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
}
