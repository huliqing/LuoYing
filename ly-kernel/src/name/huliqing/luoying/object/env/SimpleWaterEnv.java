/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.env;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.entity.AbstractEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.processor.VerySimpleWaterProcessor;
import name.huliqing.luoying.object.entity.WaterEntity;

/**
 * 轻量级的水体效果，可支持移动设置、手机等。特别针对Opengl es应用。
 * @author huliqing
 */
public class SimpleWaterEnv extends AbstractEntity implements WaterEntity {
//    private final PlayService playService = Factory.get(PlayService.class);
    
    private String waterModelFile;
    
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
    private Spatial waterModel;
    private VerySimpleWaterProcessor water;
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        waterModelFile = data.getAsString("waterModel");
        waterColor = data.getAsColor("waterColor");
        texScale = data.getAsFloat("texScale", texScale);
        waveSpeed = data.getAsFloat("waveSpeed", waveSpeed);
        distortionMix = data.getAsFloat("distortionMix", distortionMix);
        distortionScale = data.getAsFloat("distortionScale", distortionScale);
        
        foamMap = data.getAsString("foamMap");
        foamScale = data.getAsVector2f("foamScale");
        foamMaskMap = data.getAsString("foamMaskMap");
        foamMaskScale = data.getAsVector2f("foamMaskScale");
    }

    @Override
    public void updateDatas() {
        // ignore
    }
    
    @Override
    public void initialize() {
        waterModel = LuoYing.getApp().getAssetManager().loadModel(waterModelFile);
        Vector3f location = data.getAsVector3f("location");
        if (location != null) {
            waterModel.setLocalTranslation(location);
        }
        Quaternion rot = data.getAsQuaternion("rotation");
        if (rot != null) {
            waterModel.setLocalRotation(rot);
        }
        Vector3f scale = data.getAsVector3f("scale");
        if (scale != null) {
            waterModel.setLocalScale(scale);
        }
        
        water = new VerySimpleWaterProcessor(LuoYing.getApp().getAssetManager(), waterModel);
        water.addReflectionScene(scene.getRoot());
        water.setTexScale(texScale);
        water.setWaveSpeed(waveSpeed);
        water.setDistortionMix(distortionMix);
        water.setDistortionScale(distortionScale);
        if (waterColor != null) {
            water.setWaterColor(waterColor);
        }
        if (foamMap != null) {
            water.setFoamMap(foamMap);
        }
        if (foamScale != null) {
            water.setFoamScale(foamScale.x, foamScale.y);
        }
        if (foamMaskMap != null) {
            water.setFoamMaskMap(foamMaskMap);
        }
        if (foamMaskScale != null) {
            water.setFoamMaskScale(foamMaskScale.x, foamMaskScale.y);
        }
        scene.addProcessor(water);
    }
    
    @Override
    public void cleanup() {
        scene.removeProcessor(water);
        super.cleanup(); 
    }

    @Override
    public Spatial getSpatial() {
        return waterModel;
    }
    
    @Override
    public boolean isUnderWater(Vector3f point) {
        if (!initialized) {
            return false;
        }
        if (point.y < waterModel.getWorldTranslation().y) {
            BoundingBox bb = (BoundingBox) waterModel.getWorldBound();
            bb.setYExtent(Float.MAX_VALUE);
            return bb.contains(point);
        }
        return false;
    }
    
}
