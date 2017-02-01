/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.object.entity.impl;

import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.texture.Texture2D;
import com.jme3.water.WaterFilter;
import com.jme3.water.WaterFilter.AreaShape;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static final Logger LOG = Logger.getLogger(AdvanceWaterEntity.class.getName());

//    // 是否使用场景中的直射光源，打开这个选项后Water会从场景中找到第一个可用的直射光源作为水体渲染时使用的光源。
//    // 否则使用Water默认设置的光源。默认true
//    private boolean useSceneLight = true;
    
    // ---- inner
    private WaterFilter water;
    // 水体的半径平方,用于优化检测isUnderWater。
    private float radiusSquared;
    private final Vector3f tempCenter = new Vector3f();
    
    @Override
    public void setData(EntityData data) {
        super.setData(data); 
//        useSceneLight = data.getAsBoolean("useSceneLight", useSceneLight);
    }

    @Override
    public void updateDatas() {
        super.updateDatas();
        if (isInitialized()) {
            data.setAttribute("center", water.getCenter());
            data.setAttribute("radius", water.getRadius());
            data.setAttribute("waterHeight", water.getWaterHeight());
        }
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
        
        String causticsTexture = data.getAsString("causticsTexture");
        if (causticsTexture != null) {
            water.setCausticsTexture((Texture2D) LuoYing.getApp().getAssetManager().loadTexture(causticsTexture));
        }
        
        String foamTexture = data.getAsString("foamTexture");
        if (foamTexture != null) {
            water.setFoamTexture((Texture2D) LuoYing.getApp().getAssetManager().loadTexture(foamTexture));
        }
        
        String heightTexture = data.getAsString("heightTexture");
        if (heightTexture != null) {
            water.setHeightTexture((Texture2D) LuoYing.getApp().getAssetManager().loadTexture(heightTexture));
        }
        
        String normalTexture = data.getAsString("normalTexture");
        if (normalTexture != null) {
            water.setNormalTexture((Texture2D) LuoYing.getApp().getAssetManager().loadTexture(normalTexture));
        }
        
//        LOG.log(Level.INFO, "---->waterColor={0}", water.getWaterColor());
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
        if (scene != null) {
            scene.removeSceneListener(this);
            // 清理Filter,注意：清理后尽量把Filter设置为null,避免让FrameBuffer存在于内存中。
            scene.removeFilter(water);
        }
        water = null;
        super.cleanup();
    }

    public float getWaterHeight() {
        return water.getWaterHeight();
    }
    
    public void setWaterHeight(float waterHeight) {
        water.setWaterHeight(waterHeight);
    }
    
    public Vector3f getCenter() {
        return water.getCenter();
    }
    
    public void setCenter(Vector3f center) {
        water.setCenter(center);
    }
    
    public float getRadius() {
        return water.getRadius();
    }
    
    public void setRadius(float radius) {
        water.setRadius(radius);
        radiusSquared = radius * radius;
    }
    
    public Vector2f getWindDirection() {
        return water.getWindDirection();
    }
    
    public void setWindDirection(Vector2f windDirection) {
        water.setWindDirection(windDirection);
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
