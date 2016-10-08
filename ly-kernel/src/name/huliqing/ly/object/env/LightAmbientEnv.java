/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.env;

import com.jme3.light.AmbientLight;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import name.huliqing.ly.data.env.EnvData;
import name.huliqing.ly.object.scene.Scene;

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
    public void initialize(Scene scene) {
        super.initialize(scene);
        scene.getRoot().addLight(light);
    }
    
    @Override
    public void cleanup() {
        if (scene != null) {
            scene.getRoot().removeLight(light);
        }
        super.cleanup(); 
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
        data.setRotation(rotation);
    }

    @Override
    public Vector3f getScale() {
        return data.getScale();
    }

    @Override
    public void setScale(Vector3f scale) {
        data.setScale(scale);
    }

    
    
    
}
