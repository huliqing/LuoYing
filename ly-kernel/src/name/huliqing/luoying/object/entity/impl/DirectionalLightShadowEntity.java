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

import name.huliqing.luoying.object.entity.ShadowEntity;
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
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
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
    protected void initEntity() {
        shadowProcessor = new DirectionalLightShadowRenderer(LuoYing.getAssetManager(), shadowMapSize, shadowMaps);
        shadowProcessor.setShadowIntensity(shadowIntensity);
//        shadowProcessor.setShadowZExtend(500f);
    }
    
    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        if (isEnabled()) {
            shadowProcessor.setLight(findDirectionalLight());
            scene.addProcessor(shadowProcessor);
            processorAdded = true;
        }
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
