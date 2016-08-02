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
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource.Status;
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
import name.huliqing.core.game.service.ConfigService;
import name.huliqing.core.game.service.ConfigService.ConfigListener;
import name.huliqing.core.game.service.SoundService;
import name.huliqing.core.manager.SoundManager;
import name.huliqing.core.object.scene.Scene;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class AudioEnv <T extends EnvData> extends AbstractEnv<T> implements ConfigListener {
    private final SoundService soundService = Factory.get(SoundService.class);
    private final ConfigService configService = Factory.get(ConfigService.class);

    private boolean debug;
    private String sound;
    /**
     * sea com.jme3.audio.AudioData.DataType :   Buffer,Stream
     */
    private String type; // Buffer,Stream
    private Vector3f location;
    
    // ---- inner
    private final AudioNode audio = new AudioNode();
    private Spatial debugNode;
    private Spatial debugInnerNode;
    
    @Override
    public void setData(T data) {
        super.setData(data);
        audio.setDirection(data.getAsVector3f("direction", audio.getDirection()));
        audio.setDirectional(data.getAsBoolean("directional", audio.isDirectional()));
//        audio.setDryFilter(); // 使用反射，由class创建dryFilter，暂不支持。
        audio.setInnerAngle(data.getAsFloat("innerAngle", audio.getInnerAngle()));
        audio.setLooping(data.getAsBoolean("looping", audio.isLooping()));
        audio.setMaxDistance(data.getAsFloat("maxDistance", audio.getMaxDistance()));
        audio.setOuterAngle(data.getAsFloat("outerAngle", audio.getOuterAngle()));
        audio.setPitch(data.getAsFloat("pitch", audio.getPitch()));
        audio.setPositional(data.getAsBoolean("positional", audio.isPositional()));
        audio.setRefDistance(data.getAsFloat("refDistance", audio.getRefDistance()));
        audio.setReverbEnabled(data.getAsBoolean("reverbEnabled", audio.isReverbEnabled()));
//        audio.setReverbFilter();
        audio.setTimeOffset(data.getAsFloat("timeOffset", audio.getTimeOffset()));
        audio.setVelocity(data.getAsVector3f("velocity", audio.getVelocity()));
        audio.setVelocityFromTranslation(data.getAsBoolean("velocityFromTranslation", audio.isVelocityFromTranslation()));
        audio.setVolume(data.getAsFloat("volume", audio.getVolume()));
                
        debug = data.getAsBoolean("debug", false);
        sound = data.getAttribute("sound");
        type = data.getAttribute("type", DataType.Buffer.name());
        location = data.getAsVector3f("location");
    }
    
    @Override
    public void initialize(Application app, Scene scene) {
        super.initialize(app, scene);
        // 添加侦听器，以便当系统声音关闭或打开时要确定是否打开或关闭audio
        configService.addConfigListener(this);
        
        SoundData sd = soundService.loadSoundData(sound);
        AudioKey audioKey = new AudioKey(sd.getSoundFile(), DataType.Stream.name().equals(type), true);
        AudioData audioData = (AudioData) app.getAssetManager().loadAsset(audioKey);
        audio.setAudioData(audioData, audioKey);
        
        if (location != null) {
            audio.setLocalTranslation(location);
        }
        if (debug) {
            debugNode = new Geometry("debugAudioEnvDistance", new Sphere(20, 20, audio.getMaxDistance()));
            debugNode.setMaterial(createDebugMaterial(ColorRGBA.Green));
            audio.attachChild(debugNode);
            
            debugInnerNode = new Geometry("debugAudioEnvRefDistance", new Sphere(20, 20, audio.getRefDistance()));
            debugInnerNode.setMaterial(createDebugMaterial(ColorRGBA.Red));
            audio.attachChild(debugInnerNode);
        }
        
        scene.addSceneObject(audio);
        
        SoundManager.getInstance().checkToPlayAudio(audio);
    }
    
    private Material createDebugMaterial(ColorRGBA color) {
        Material mat = new Material(LY.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.setColor("Color",  color);
        return mat;
    }

    @Override
    public void onConfigChanged() {
        SoundManager.getInstance().checkToPlayAudio(audio);
    }

    @Override
    public void cleanup() {
        // 移除侦听器
        configService.removeConfigListener(this);
        audio.stop();
        scene.removeSceneObject(audio);
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
