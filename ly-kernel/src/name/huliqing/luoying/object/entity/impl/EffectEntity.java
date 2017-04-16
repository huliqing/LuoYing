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

import com.jme3.animation.Bone;
import com.jme3.animation.SkeletonControl;
import com.jme3.scene.Spatial;
import name.huliqing.luoying.data.EffectData;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.effect.Effect;
import name.huliqing.luoying.object.effect.Effect.EffectListener;
import name.huliqing.luoying.object.effect.TraceEffect;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.NonModelEntity;
import name.huliqing.luoying.object.scene.Scene;

/**
 * 特效实体，主要用于将特效功能封装为一个可直接放到场景中的实体。
 * @author huliqing
 */
public class EffectEntity extends NonModelEntity {
    
    // 设置要跟随的目标
    private long traceEntity;
    // 设置要跟随的目标实体的某个骨骼的名称
    private String traceBone;
    // 实际的效果
    private EffectData effect;
    
    // ---- 
    // 效果实例
    private Effect effectInstance;
    // 效果侦听器，用于在特效结束时将效果移除出场景
    private final EffectListener effectListener = new EffectListener() {
        @Override
        public void onEffectEnd(Effect eff) {
            if (scene != null) {
                scene.removeEntity(EffectEntity.this);
            }
        }
    };
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        traceEntity = data.getAsLong("traceEntity", -1);
        traceBone = data.getAsString("traceBone");
        effect = data.getAsObjectData("effect");
    }

    @Override
    public void updateDatas() {
        if (effectInstance != null) {
            effectInstance.updateDatas();
            NULL_ROOT.setLocalTranslation(effectInstance.getWorldTranslation());
            NULL_ROOT.setLocalRotation(effectInstance.getWorldRotation());
            NULL_ROOT.setLocalScale(effectInstance.getWorldScale());
            data.setAttribute("effect", effectInstance.getData());
        }
        super.updateDatas();
        data.setAttribute("traceEntityId", traceEntity);
        data.setAttribute("traceBone", traceBone);
    }
    
    public void setEffect(Effect effect) {
        this.effect = effect.getData();
        this.effectInstance = effect;
    }
    
    public void setEffect(EffectData effectData) {
        this.effect = effectData;
    }
    
    public void setTraceEntity(long entityId) {
        this.traceEntity = entityId;
    }
    
    public void setTraceEntity(Entity entity) {
        this.traceEntity = entity.getEntityId();
    }
    
    public void setTraceBone(String traceBone) {
        this.traceBone = traceBone;
    }
    
    public void requestEnd() {
        if (effectInstance != null) {
            effectInstance.requestEnd();
        }
    }
    
    @Override
    protected void initEntity() {
        // ignore
    }
    
    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        if (effectInstance == null && effect != null) {
            effectInstance = Loader.load(effect);
            effectInstance.addListener(effectListener); // 在特效结束时，同时将EffectEntity移除出场景
        }
        effectInstance.setLocalTranslation(NULL_ROOT.getWorldTranslation());
        effectInstance.setLocalRotation(NULL_ROOT.getWorldRotation());
        scene.getRoot().attachChild(effectInstance);
        
        if (!effectInstance.isInitialized()) {
            effectInstance.initialize();
        }
        if (traceEntity >= 0) {
            Entity target = scene.getEntity(traceEntity);
            if (target != null) {
                Spatial targetSpatial = target.getSpatial();
                Spatial targetBoneSpatial = null;
                if (traceBone != null) {
                    SkeletonControl sc = targetSpatial.getControl(SkeletonControl.class);
                    if (sc != null) {
                        Bone bone = sc.getSkeleton().getBone(traceBone);
                        if (bone != null) {
                            targetBoneSpatial = sc.getAttachmentsNode(traceBone);
                        }
                    }
                }
                Spatial traceSpatial = targetBoneSpatial != null ? targetBoneSpatial : targetSpatial;
                effectInstance.setLocalTranslation(traceSpatial.getWorldTranslation());
                effectInstance.setLocalRotation(traceSpatial.getWorldRotation());
                if (effectInstance instanceof TraceEffect) {
                    ((TraceEffect) effectInstance).setTraceObject(traceSpatial);
                }
            }
        }
    }

    @Override
    public void cleanup() {
        if (effectInstance != null && effectInstance.isInitialized()) {
            effectInstance.cleanup();
            effectInstance.removeFromParent();
        }
        super.cleanup();
    }


    
}
