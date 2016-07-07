/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.audio.LowPassFilter;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.texture.Texture2D;
import com.jme3.water.WaterFilter;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.object.scene.Scene;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class WaterAdvanceEnv <T extends EnvData> extends Env<T> implements Scene.Listener {

    private Application app;
    private WaterFilter water;
    private final LowPassFilter underWaterAudioFilter = new LowPassFilter(0.5f, 0.1f);
//    private final LowPassFilter underWaterReverbFilter = new LowPassFilter(0.5f, 0.1f);
    private final LowPassFilter aboveWaterAudioFilter = new LowPassFilter(1, 1);
    private Node controlNode;
    private AudioNode waveAudio;
    
    @Override
    public void initData(T data) {
        super.initData(data); 
    }

    @Override
    public void initialize(Application app, Scene scene) {
        super.initialize(app, scene); 
        this.app = app;
        scene.addListener(this);
        
         //Water Filter
        water = new WaterFilter();
        water.setWaterColor(new ColorRGBA(0.6f, 0.8f, 0.9f, 1));
        water.setDeepWaterColor(new ColorRGBA(0.6f, 0.8f, 0.9f, 1));
        water.setUnderWaterFogDistance(80);
        water.setWaterTransparency(0.12f);
        water.setFoamIntensity(0.4f);        
        water.setFoamHardness(0.3f);
        water.setFoamExistence(new Vector3f(0.8f, 8f, 1f));
        water.setReflectionDisplace(50);
        water.setRefractionConstant(0.25f);
        water.setColorExtinction(new Vector3f(30, 50, 70));
        water.setCausticsIntensity(0.4f);        
        water.setWaveScale(0.003f);
        water.setMaxAmplitude(2f);
        water.setFoamTexture((Texture2D) app.getAssetManager().loadTexture("Common/MatDefs/Water/Textures/foam2.jpg"));
        water.setRefractionStrength(0.2f);
        water.setReflectionScene(scene.getSceneRoot());
        
        controlNode = new Node();
        controlNode.addControl(new WaterControl());
        
        waveAudio = createWaveAudio(app.getAssetManager());
        
        // 控制水体渲染
        scene.addFilter(water);
        // 控制潮涨潮落
        scene.addSceneObject(controlNode);
        // 控制潮水声效
        scene.addSceneObject(waveAudio);
        waveAudio.play();
    }

    @Override
    public void cleanup() {
        if (scene != null) {
            waveAudio.stop();
            scene.removeListener(this);
            scene.removeSceneObject(controlNode);
            scene.removeSceneObject(waveAudio);
            scene.removeFilter(water);
            water = null;
            controlNode = null;
            waveAudio = null;
        }
        super.cleanup();
    }
    
    private AudioNode createWaveAudio(AssetManager am) {
        AudioNode audio = new AudioNode(am, "Sounds/environment/ocean.ogg", DataType.Buffer);
        audio.setLooping(true);
        audio.setReverbEnabled(true);
        return audio;
    }

    @Override
    public void onAdded(Scene scene, Spatial objectAdded) {}

    @Override
    public void onRemoved(Scene scene, Spatial objectRemoved) {}

    @Override
    public void onInitialized(Scene scene) {
        LightList lights = scene.getSceneRoot().getLocalLightList();
        if (lights.size() > 0) {
            for (int i = 0; i < lights.size(); i++) {
                Light light = lights.get(i);
                if (light instanceof DirectionalLight) {
                    DirectionalLight dl = (DirectionalLight) light;
                    water.setLightDirection(dl.getDirection());
                    water.setLightColor(dl.getColor());
                    break;
                }
            }
        }
    }
    
    private class WaterControl extends AbstractControl {
        //This part is to emulate tides, slightly varrying the height of the water plane
        private float time = 0.0f;
        private float waterHeight = 0.0f;
        private float initialWaterHeight = -6.5f;//0.8f;
        private boolean underWater = false;
        
        @Override
        protected void controlUpdate(float tpf) {
            time += tpf;
            waterHeight = (float) Math.cos(((time * 0.6f) % FastMath.TWO_PI)) * 1.5f;
            water.setWaterHeight(initialWaterHeight + waterHeight);
            if (water.isUnderWater() && !underWater) {
                waveAudio.setReverbEnabled(true);
                waveAudio.setDryFilter(underWaterAudioFilter);
                underWater = true;
            }
            if (!water.isUnderWater() && underWater) {
                underWater = false;
                waveAudio.setReverbEnabled(false);
                waveAudio.setDryFilter(aboveWaterAudioFilter);
            }
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {}
        
    }
    
    
}
