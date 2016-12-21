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
import com.jme3.shadow.CompareMode;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.EdgeFilteringMode;
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
 * 阴影环境(目前不支持移动设备), 给场景添加这个环境后，可以让场景中的物体有阴影效果
 * @author huliqing
 */
public class DirectionalLightFilterShadowEntity extends ShadowEntity {
//    private final ConfigService configService = Factory.get(ConfigService.class);

    private float shadowIntensity = 0.7f;
    private int shadowMapSize = 1024;
    private int shadowMaps = 1;
    
    // ---- inner
    private DirectionalLightShadowFilter filter;
    private final SceneListener sceneListener = new SceneListenerAdapter() {
        @Override
        public void onSceneLoaded(Scene scene) {
            if (enabled) {
                filter.setLight(findDirectionalLight());
            }
        }
    };
    private boolean filterAdded;
    
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
        data.setAttribute("shadowIntensity", filter.getShadowIntensity());
    }
    
    @Override
    public void initEntity() {
        filter = new DirectionalLightShadowFilter(LuoYing.getApp().getAssetManager(), shadowMapSize, shadowMaps);
        filter.setLambda(0.55f);
        filter.setShadowIntensity(shadowIntensity);
        filter.setShadowCompareMode(CompareMode.Hardware);
        filter.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
//        filter.setShadowZExtend(500);
    }
    
    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        if (scene.isInitialized()) {
            filter.setLight(findDirectionalLight());
        }
        scene.addSceneListener(sceneListener);
    }
    
    @Override
    protected void setShadowEnabled(boolean enabled) {
        if (enabled) {
            if (!filterAdded) {
                filter.setLight(findDirectionalLight());
                scene.addFilter(filter);
                filterAdded = true;
            }
        } else {
            if (filterAdded) {
                scene.removeFilter(filter);
                filterAdded = false;
            }
        }
    }
    
    @Override
    public void cleanup() {
        scene.removeFilter(filter);
        scene.removeSceneListener(sceneListener);
        // 注意：这里要把filter设置为null以让系统释放内存，否则即使cleanup后，该filter内部使用中的frameBuffer仍然会
        // 占用内存(从stateAppState的debug中可以看到FrameBuffers(M)一下在增加)。
        // 这是一个特殊的情况，在其它Filter还没有发现这个问题。
        filter = null;
        filterAdded = false;
        super.cleanup();
    }
    
    @Override
    public float getShadowIntensity() {
        return filter.getShadowIntensity();
    }
    
    @Override
    public void setShadowIntensity(float shadowIntensity) {
        filter.setShadowIntensity(MathUtils.clamp(shadowIntensity, 0f, 1.0f));
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
