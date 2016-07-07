/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.app.Application;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.object.scene.Scene;
import name.huliqing.fighter.processor.MySimpleWaterProcessor;

/**
 * 轻量级的水体效果，可支持移动设置、手机等。特别针对Opengl es应用。
 * @author huliqing
 * @param <T>
 */
public class WaterSimpleEnv<T extends EnvData>  extends Env<T> {
//    private final PlayService playService = Factory.get(PlayService.class);
    
    private String waterModelFile;
    private Vector3f location;
    private Vector3f rotation;
    private Vector3f scale;
    
    private ColorRGBA waterColor;
    private float texScale = 1;
    private float waveSpeed = 0.05f;
    private float distortionMix = 0.5f;
    private float distortionScale = 0.2f;
    
    private String foamMap;
    private Vector2f foamScale;
    private String foamMaskMap;
    private Vector2f foamMaskScale;
    
    // ----
    private Application app;
    private Spatial waterModel;
    private MySimpleWaterProcessor swp;
    
    @Override
    public void initData(T data) {
        super.initData(data);
        waterModelFile = data.getAttribute("waterModel");
        location = data.getAsVector3f("location");
        rotation = data.getAsVector3f("rotation");
        scale = data.getAsVector3f("scale");
        waterColor = data.getAsColor("waterColor");
        texScale = data.getAsFloat("texScale", texScale);
        waveSpeed = data.getAsFloat("waveSpeed", waveSpeed);
        distortionMix = data.getAsFloat("distortionMix", distortionMix);
        distortionScale = data.getAsFloat("distortionScale", distortionScale);
        
        foamMap = data.getAttribute("foamMap");
        foamScale = data.getAsVector2f("foamScale");
        foamMaskMap = data.getAttribute("foamMaskMap");
        foamMaskScale = data.getAsVector2f("foamMaskScale");
    }
    
    @Override
    public void initialize(Application app, Scene scene) {
        super.initialize(app, scene);
        this.app = app;
        
        waterModel = app.getAssetManager().loadModel(waterModelFile);
        if (location != null) {
            waterModel.setLocalTranslation(location);
        }
        if (rotation != null) {
            Quaternion rot = waterModel.getLocalRotation();
            rot.fromAngles(rotation.x, rotation.y, rotation.z);
            waterModel.setLocalRotation(rot);
        }
        if (scale != null) {
            waterModel.setLocalScale(scale);
        }
        
        swp = new MySimpleWaterProcessor(app.getAssetManager(), waterModel);
        swp.addReflectionScene(scene.getSceneRoot());
        swp.setTexScale(texScale);
        swp.setWaveSpeed(waveSpeed);
        swp.setDistortionMix(distortionMix);
        swp.setDistortionScale(distortionScale);
        if (waterColor != null) {
            swp.setWaterColor(waterColor);
        }
        if (foamMap != null) {
            swp.setFoamMap(foamMap);
        }
        if (foamScale != null) {
            swp.setFoamScale(foamScale.x, foamScale.y);
        }
        if (foamMaskMap != null) {
            swp.setFoamMaskMap(foamMaskMap);
        }
        if (foamMaskScale != null) {
            swp.setFoamMaskScale(foamMaskScale.x, foamMaskScale.y);
        }
        // remove20160704，这会造成死循环，需要想其它办法来支持多个ViewPort
//        waterModel.addControl(new WaterControl(app.getViewPort(), swp)); 
        app.getViewPort().addProcessor(swp);
        
        scene.addSceneObject(waterModel);
    }
    
    @Override
    public void cleanup() {
        if (waterModel != null) {
            scene.removeSceneObject(waterModel);
        }
        if (swp != null && app != null) {
            if (app.getViewPort().getProcessors().contains(swp)) {
                app.getViewPort().removeProcessor(swp);
            }
        }
        super.cleanup(); 
    }
    

    // remove20160704，这会造成死循环，因为vp可能是simpleWaterProcessor中的特殊viewPort.
    // 必须排除这种情况。
//    private class WaterControl extends AbstractControl {
//        
//        private final SimpleWaterProcessor swp;
//        private final ViewPort defViewPort;
//
//        public WaterControl(ViewPort defViewPort, SimpleWaterProcessor swp) {
//            this.defViewPort = defViewPort;
//            this.swp = swp;
//            this.defViewPort.addProcessor(this.swp);
//        }
//        
//        @Override
//        protected void controlUpdate(float tpf) {}
//
//        @Override
//        protected void controlRender(RenderManager rm, ViewPort vp) {
////            // 这里用于支持多个viewPort的情况
////            if (vp != defViewPort) {
////                if (!vp.getProcessors().contains(swp)) {
////                    vp.addProcessor(swp);
////                }
////            }
//        }
//    }


    
    
}
