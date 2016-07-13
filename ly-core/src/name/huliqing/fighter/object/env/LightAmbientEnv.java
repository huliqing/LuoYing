/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.app.Application;
import com.jme3.light.AmbientLight;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.object.scene.Scene;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class LightAmbientEnv <T extends EnvData> extends AbstractEnv<T> {

    private final AmbientLight light = new AmbientLight();
    
    @Override
    public void setData(T data) {
        super.setData(data);
        light.setColor(data.getAsColor("color", light.getColor()));
    }

    @Override
    public void initialize(Application app, Scene scene) {
        super.initialize(app, scene);
        scene.addSceneLight(light);
    }
    
    @Override
    public void cleanup() {
        if (scene != null) {
            scene.removeSceneLight(light);
        }
        super.cleanup(); 
    }
    
}
