/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity.impl;

import com.jme3.light.DirectionalLight;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import java.util.List;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.LightEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.scene.SceneListener;
import name.huliqing.luoying.object.scene.SceneListenerAdapter;
import name.huliqing.luoying.utils.MathUtils;

/**
 *
 * @author huliqing
 */
public class DirectionalLightShadowEntity extends ShadowEntity {

    private float shadowIntensity = 0.7f;
    private int shadowMapSize = 1024;
    private int shadowMaps = 1;
    
    private DirectionalLightShadowRenderer shadowProcessor;
    private final SceneListener sceneListener = new SceneListenerAdapter() {
        @Override
        public void onSceneLoaded(Scene scene) {
            if (enabled) {
                shadowProcessor.setLight(findDirectionalLight());
            }
        }
    };
    private boolean processorAdded;
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        shadowIntensity = data.getAsFloat("shadowIntensity", shadowIntensity);
        shadowMapSize = data.getAsInteger("shadowMapSize", shadowMapSize);
        shadowMaps = data.getAsInteger("shadowMaps", shadowMaps);
    }

    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("shadowIntensity", shadowProcessor.getShadowIntensity());
    }
    
    @Override
    protected void initEntity() {
        shadowProcessor = new DirectionalLightShadowRenderer(LuoYing.getAssetManager(), shadowMapSize, shadowMaps);
        shadowProcessor.setShadowIntensity(shadowIntensity);
//        shadowProcessor.setShadowZExtend(500f);
    }
    
    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        scene.addSceneListener(sceneListener);
    }

    @Override
    public void cleanup() {
        scene.removeProcessor(shadowProcessor);
        scene.removeSceneListener(sceneListener);
        shadowProcessor = null;
        processorAdded = false;
        super.cleanup(); 
    }

    @Override
    protected void setShadowEnabled(boolean enabled) {
        if (enabled) {
            if (!processorAdded) {
                shadowProcessor.setLight(findDirectionalLight());
                scene.addProcessor(shadowProcessor);
                processorAdded = true;
            }
        } else {
            if (processorAdded) {
                scene.removeProcessor(shadowProcessor);
                processorAdded = false;
            }
        }
    }
    
    @Override
    public float getShadowIntensity() {
        return shadowProcessor.getShadowIntensity();
    }
    
    @Override
    public void setShadowIntensity(float shadowIntensity) {
        shadowProcessor.setShadowIntensity(MathUtils.clamp(shadowIntensity, 0f, 1.0f));
    }
    
    private DirectionalLight findDirectionalLight() {
        List<Entity> es = scene.getEntities();
        LightEntity le;
        for (Entity e : es) {
            if (e instanceof LightEntity) {
                le = (LightEntity) e;
                if (le.getLight() instanceof DirectionalLight) {
                    return (DirectionalLight) le.getLight();
                }
            }
        }
        // 如果找不到任何光源，则创建一个默认的
        return new DirectionalLight();
    }
    
}
