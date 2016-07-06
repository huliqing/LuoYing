/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.app.Application;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.data.SoundData;
import name.huliqing.fighter.game.service.SoundService;
import name.huliqing.fighter.object.scene.Scene;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class AudioEnv <T extends EnvData> extends Env<T> {
    private final SoundService soundService = Factory.get(SoundService.class);

    private boolean debug;
    private String sound;
    private Vector3f location;
    
    
    // ---- inner
    private AudioNode audio;
    private Spatial debugNode;
    private Spatial debugInnerNode;
    
    @Override
    public void initData(T data) {
        super.initData(data);
        debug = data.getAsBoolean("debug", false);
        sound = data.getAttribute("sound");
        location = data.getAsVector3f("location");
    }
    
    @Override
    public void initialize(Application app, Scene scene) {
        super.initialize(app, scene);
        SoundData sd = soundService.loadSoundData(sound);
        audio = new AudioNode(app.getAssetManager(), sd.getSoundFile(), AudioData.DataType.Stream);
        audio.setPositional(true);
        audio.setRefDistance(190);
        audio.setMaxDistance(200);
        audio.setVolume(0.2f);
        audio.setReverbEnabled(false);
        audio.setLooping(true);
        if (location != null) {
            audio.setLocalTranslation(location);
        }
        scene.addSceneObject(audio);
        audio.play();
        
        if (debug) {
            debugNode = new Geometry("debugAudioEnvDistance", new Sphere(10, 20, audio.getMaxDistance()));
            debugNode.setLocalTranslation(audio.getLocalTranslation());
            debugNode.setMaterial(createDebugMaterial(ColorRGBA.Green));
            scene.addSceneObject(debugNode);
            
            debugInnerNode = new Geometry("debugAudioEnvRefDistance", new Sphere(10, 20, audio.getRefDistance()));
            debugInnerNode.setLocalTranslation(audio.getLocalTranslation());
            debugInnerNode.setMaterial(createDebugMaterial(ColorRGBA.Red));
            scene.addSceneObject(debugInnerNode);
        }
    }
    
    private Material createDebugMaterial(ColorRGBA color) {
        Material mat = new Material(Common.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.setColor("Color",  color);
        return mat;
    }

    @Override
    public void cleanup() {
        if (audio != null) {
            audio.stop();
            scene.removeSceneObject(audio);
            audio = null;
        }
        if (debugNode != null) {
            scene.removeSceneObject(debugNode);
            debugNode = null;
        }
        if (debugInnerNode != null) {
            scene.removeSceneObject(debugInnerNode);
            debugInnerNode = null;
        }
        super.cleanup(); 
    }
    
}
