/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.env;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import name.huliqing.ly.Ly;
import name.huliqing.ly.data.env.EnvData;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.scene.Scene;
import name.huliqing.ly.object.sound.Sound;
import name.huliqing.ly.object.sound.SoundManager;

/**
 * @author huliqing
 */
public class AudioEnv extends AbstractEnv<EnvData> {

    private boolean debug;
    private String soundId;
    
    // ---- inner
    private Sound sound;
    private Spatial debugNode;
    private Spatial debugInnerNode;
    
    @Override
    public void setData(EnvData data) {
        super.setData(data);
        debug = data.getAsBoolean("debug", false);
        soundId = data.getAsString("sound");
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        if (initialized) {
            data.setLocation(sound.getLocalTranslation());
            data.setRotation(sound.getLocalRotation());
            data.setScale(sound.getLocalScale());
        }
    }
    
    @Override
    public void initialize(Scene scene) {
        super.initialize(scene);
        
        if (sound == null) {
            sound = Loader.load(soundId);
        }
        sound.setLocalTranslation(data.getLocation());
        sound.setLocalRotation(data.getRotation());
        sound.setLocalScale(data.getScale());
        
        if (debug) {
            debugNode = new Geometry("debugAudioEnvDistance", new Sphere(20, 20, sound.getData().getMaxDistance()));
            debugNode.setMaterial(createDebugMaterial(ColorRGBA.Green));
            sound.attachChild(debugNode);
            
            debugInnerNode = new Geometry("debugAudioEnvRefDistance", new Sphere(20, 20, sound.getData().getRefDistance()));
            debugInnerNode.setMaterial(createDebugMaterial(ColorRGBA.Red));
            sound.attachChild(debugInnerNode);
        }
        
        scene.addSpatial(sound);
        SoundManager.getInstance().addAndPlay(sound);
    }
    
    private Material createDebugMaterial(ColorRGBA color) {
        Material mat = new Material(Ly.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.setColor("Color",  color);
        return mat;
    }

    @Override
    public void cleanup() {
        // 停止声音
        SoundManager.getInstance().removeAndStopDirectly(sound);
        
        // 移除出场景
        scene.removeSpatial(sound);
        
        if (debugNode != null) {
            scene.removeSpatial(debugNode);
            debugNode = null;
        }
        if (debugInnerNode != null) {
            scene.removeSpatial(debugInnerNode);
            debugInnerNode = null;
        }
        super.cleanup(); 
    }

    @Override
    public Vector3f getLocation() {
        return initialized ? sound.getLocalTranslation() : data.getLocation();
    }

    @Override
    public void setLocation(Vector3f location) {
        if (initialized) {
            sound.setLocalTranslation(location);
            return;
        }
        data.setLocation(location);
    }

    @Override
    public Quaternion getRotation() {
        if (initialized) {
            return sound.getLocalRotation();
        }
        return data.getRotation();
    }

    @Override
    public void setRotation(Quaternion rotation) {
        if (initialized) {
            sound.setLocalRotation(rotation);
            return;
        }
        data.setRotation(rotation);
    }

    @Override
    public Vector3f getScale() {
        if (initialized) {
            return sound.getLocalScale();
        }
        return data.getScale();
    }

    @Override
    public void setScale(Vector3f scale) {
        if (initialized) {
            sound.setLocalScale(scale);
            return;
        }
        data.setScale(scale);
    }
    
}
