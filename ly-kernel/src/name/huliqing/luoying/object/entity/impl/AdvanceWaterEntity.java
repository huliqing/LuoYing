/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity.impl;

import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.texture.Texture2D;
import com.jme3.water.WaterFilter;
import com.jme3.water.WaterFilter.AreaShape;
import java.util.List;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.LightEntity;
import name.huliqing.luoying.object.entity.NonModelEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.scene.SceneListener;
import name.huliqing.luoying.object.entity.WaterEntity;

/**
 * 高级水体环境
 * @author huliqing
 */
public class AdvanceWaterEntity extends NonModelEntity implements WaterEntity, SceneListener {

    private String causticsTexture;
    private String foamTexture;
    private String heightTexture;
    private String normalTexture;
    
    // 是否使用场景中的直射光源，打开这个选项后Water会从场景中找到第一个可用的直射光源作为水体渲染时使用的光源。
    // 否则使用Water默认设置的光源。默认true
    private boolean useSceneLight = true;
    
    // ---- inner
    private WaterFilter water;
    // 水体的半径平方,用于优化检测isUnderWater。
    private float radiusSquared;
    private final Vector3f tempCenter = new Vector3f();
    
    @Override
    public void setData(EntityData data) {
        super.setData(data); 
        this.causticsTexture = data.getAsString("causticsTexture");
        this.foamTexture = data.getAsString("foamTexture");
        this.heightTexture = data.getAsString("heightTexture");
        this.normalTexture = data.getAsString("normalTexture");
        // ---- custom 
        useSceneLight = data.getAsBoolean("useSceneLight", useSceneLight);
    }

    @Override
    public void updateDatas() {
        super.updateDatas();
    }

    @Override
    public void initEntity() {
        water = new WaterFilter();
        water.setCausticsIntensity(data.getAsFloat("causticsIntensity", water.getCausticsIntensity()));
        Vector3f center = data.getAsVector3f("center");
        if (center != null) {
            water.setCenter(center);
        }
        water.setColorExtinction(data.getAsVector3f("colorExtinction", water.getColorExtinction()));
        water.setDeepWaterColor(data.getAsColor("deepWaterColor", water.getDeepWaterColor()));
        water.setFoamExistence(data.getAsVector3f("foamExistence", water.getFoamExistence()));
        water.setFoamHardness(data.getAsFloat("foamHardness", water.getFoamHardness()));
        water.setFoamIntensity(data.getAsFloat("foamIntensity", water.getFoamIntensity()));
        water.setLightColor(data.getAsColor("lightColor", water.getLightColor()));
        water.setLightDirection(data.getAsVector3f("lightDirection", water.getLightDirection()));
        water.setMaxAmplitude(data.getAsFloat("maxAmplitude", water.getMaxAmplitude()));
        water.setNormalScale(data.getAsFloat("normalScale", water.getNormalScale()));
        water.setRadius(data.getAsFloat("radius", water.getRadius()));
        water.setReflectionDisplace(data.getAsFloat("reflectionDisplace", water.getReflectionDisplace()));
        water.setReflectionMapSize(data.getAsInteger("reflectionMapSize", water.getReflectionMapSize()));
        water.setRefractionConstant(data.getAsFloat("refractionConstant", water.getRefractionConstant()));
        water.setRefractionStrength(data.getAsFloat("refractionStrength", water.getRefractionStrength()));
        
        String shapeType = data.getAsString("shapeType");
        if (AreaShape.Square.name().equals(shapeType)) {
            water.setShapeType(AreaShape.Square);
        } else if (AreaShape.Circular.name().equals(shapeType)) {
            water.setShapeType(AreaShape.Circular);
        }
        
        water.setShininess(data.getAsFloat("shininess", water.getShininess()));
        water.setShoreHardness(data.getAsFloat("shoreHardness", water.getShoreHardness()));
        water.setSpeed(data.getAsFloat("speed", water.getSpeed()));
        water.setSunScale(data.getAsFloat("sunScale", water.getSunScale()));
        water.setUnderWaterFogDistance(data.getAsFloat("underWaterFogDistance", water.getUnderWaterFogDistance()));
        water.setUseCaustics(data.getAsBoolean("useCaustics", water.isUseCaustics()));
        water.setUseFoam(data.getAsBoolean("useFoam", water.isUseFoam()));
        water.setUseHQShoreline(data.getAsBoolean("useHQShoreline", water.isUseHQShoreline()));
        water.setUseRefraction(data.getAsBoolean("useRefraction", water.isUseRefraction()));
        water.setUseRipples(data.getAsBoolean("useRipples", water.isUseRipples()));
        water.setUseSpecular(data.getAsBoolean("useSpecular", water.isUseSpecular()));
        water.setWaterColor(data.getAsColor("waterColor", water.getWaterColor()));
        water.setWaterHeight(data.getAsFloat("waterHeight", water.getWaterHeight()));
        water.setWaterTransparency(data.getAsFloat("waterTransparency", water.getWaterTransparency()));
        water.setWaveScale(data.getAsFloat("waveScale", water.getWaveScale()));
        water.setWindDirection(data.getAsVector2f("windDirection", water.getWindDirection()));
        radiusSquared = water.getRadius() * water.getRadius();
        
        if (causticsTexture != null) {
            water.setCausticsTexture((Texture2D) LuoYing.getApp().getAssetManager().loadTexture(causticsTexture));
        }
        if (foamTexture != null) {
            water.setFoamTexture((Texture2D) LuoYing.getApp().getAssetManager().loadTexture(foamTexture));
        }
        if (heightTexture != null) {
            water.setHeightTexture((Texture2D) LuoYing.getApp().getAssetManager().loadTexture(heightTexture));
        }
        if (normalTexture != null) {
            water.setNormalTexture((Texture2D) LuoYing.getApp().getAssetManager().loadTexture(normalTexture));
        }
    }

    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        water.setReflectionScene(scene.getRoot());
        scene.addSceneListener(this);
        // 控制水体渲染
        scene.addFilter(water);
        if (scene.isInitialized()) {
            findAndSetLight();
        }
    }

    @Override
    public void cleanup() {
        scene.removeSceneListener(this);
        // 清理Filter,注意：清理后尽量把Filter设置为null,避免让FrameBuffer存在于内存中。
        scene.removeFilter(water);
        water = null;
        super.cleanup();
    }
    
    @Override
    public boolean isUnderWater(Vector3f point) {
        if (!isInitialized())
            return false;
        
        // 在水面上,则后面不需要再检测
        float wh = water.getCenter().y + water.getWaterHeight();
        if (point.y > wh) 
            return false;
        
        // 无界限的水面(在水面下),则一定是在水里。注：center为null是无界限水体
        if (water.getCenter() == null) {
            return true;
        } 
        
        // 有界限水体
        if (water.getShapeType() == AreaShape.Circular) {
            tempCenter.set(water.getCenter()).setY(point.y);
            return point.distanceSquared(tempCenter) < radiusSquared;
            
        } else if (water.getShapeType() == AreaShape.Square) {
            
            float minX = water.getCenter().x - water.getRadius();
            float maxX = water.getCenter().x + water.getRadius();
            float minZ = water.getCenter().z - water.getRadius();
            float maxZ = water.getCenter().z + water.getRadius();
            return point.x > minX && point.x < maxX && point.z > minZ && point.z < maxZ;
        }
        return false;
    }
    
    @Override
    public void onSceneLoaded(Scene scene) {
        findAndSetLight();
    }

    @Override
    public void onSceneEntityAdded(Scene scene, Entity entityAdded) {
        if (entityAdded instanceof LightEntity) {
            LightEntity le = (LightEntity) entityAdded;
            if (le.getLight() instanceof DirectionalLight) {
                setWaterLight((DirectionalLight) le.getLight());
            }
        }
    }

    @Override
    public void onSceneEntityRemoved(Scene scene, Entity entityRemoved) {
        // 当场景中移除了灯光时重新查找新的灯光。
        if (entityRemoved instanceof LightEntity) {
            findAndSetLight();
        }
    }
    
    private void findAndSetLight() {
        List<Entity> es = scene.getEntities();
        LightEntity le;
        for (Entity e : es) {
            if (e instanceof LightEntity) {
                le = (LightEntity) e;
                if (le.getLight() instanceof DirectionalLight) {
                    setWaterLight((DirectionalLight) le.getLight());
                    return;
                }
            }
        }
    }
    
    private void setWaterLight(DirectionalLight light) {
        water.setLightDirection(light.getDirection());
        water.setLightColor(light.getColor());
    }
    
    // 控制潮涨潮落
//    private class WaterControl extends AbstractControl {
//        //This part is to emulate tides, slightly varrying the height of the water plane
//        private float time = 0.0f;
//        private float waterHeight = 0.0f;
//        private float initialWaterHeight = -6.5f;//0.8f;
//        private boolean underWater = false;
//        
//        @Override
//        protected void controlUpdate(float tpf) {
//            time += tpf;
//            waterHeight = (float) Math.cos(((time * 0.6f) % FastMath.TWO_PI)) * 1.5f;
//            water.setWaterHeight(initialWaterHeight + waterHeight);
//            if (water.isUnderWater() && !underWater) {
//                waveAudio.setReverbEnabled(true);
//                waveAudio.setDryFilter(underWaterAudioFilter);
//                underWater = true;
//            }
//            if (!water.isUnderWater() && underWater) {
//                underWater = false;
//                waveAudio.setReverbEnabled(false);
//                waveAudio.setDryFilter(aboveWaterAudioFilter);
//            }
//        }
//
//        @Override
//        protected void controlRender(RenderManager rm, ViewPort vp) {}
//        
//    }





    
    
}
