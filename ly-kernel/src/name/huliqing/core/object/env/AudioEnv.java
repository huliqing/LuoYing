/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.env;

import com.jme3.app.Application;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import name.huliqing.core.LY;
import name.huliqing.core.Factory;
import name.huliqing.core.data.EnvData;
import name.huliqing.core.data.SoundData;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.mvc.service.ConfigService;
import name.huliqing.core.mvc.service.ConfigService.ConfigListener;
import name.huliqing.core.mvc.service.SoundService;
import name.huliqing.core.object.scene.Scene;
import name.huliqing.core.object.sound.Sound;
import name.huliqing.core.object.sound.SoundManager;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class AudioEnv <T extends EnvData> extends AbstractEnv<T> {

    private boolean debug;
    private String soundId;
    
    /**
     * sea com.jme3.audio.AudioData.DataType :   Buffer,Stream
     */
    private Vector3f location;
    
    // ---- inner
    private Sound sound;
    private Spatial debugNode;
    private Spatial debugInnerNode;
    
    @Override
    public void setData(T data) {
        super.setData(data);
        
        debug = data.getAsBoolean("debug", false);
        soundId = data.getAttribute("sound");
//        type = data.getAttribute("type", DataType.Buffer.name());
        location = data.getAsVector3f("location");
    }
    
    @Override
    public void initialize(Application app, Scene scene) {
        super.initialize(app, scene);
        
        if (sound == null) {
            sound = Loader.load(soundId);
        }
        if (location != null) {
            sound.setLocalTranslation(location);
        }
        if (debug) {
            debugNode = new Geometry("debugAudioEnvDistance", new Sphere(20, 20, sound.getData().getMaxDistance()));
            debugNode.setMaterial(createDebugMaterial(ColorRGBA.Green));
            sound.attachChild(debugNode);
            
            debugInnerNode = new Geometry("debugAudioEnvRefDistance", new Sphere(20, 20, sound.getData().getRefDistance()));
            debugInnerNode.setMaterial(createDebugMaterial(ColorRGBA.Red));
            sound.attachChild(debugInnerNode);
        }
        
        scene.addSceneObject(sound);
        SoundManager.getInstance().addAndPlay(sound);
    }
    
    private Material createDebugMaterial(ColorRGBA color) {
        Material mat = new Material(LY.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
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
        scene.removeSceneObject(sound);
        
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
