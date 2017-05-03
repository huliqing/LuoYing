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

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.LinkedList;
import java.util.List;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.utils.CollisionChaseCamera;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.NonModelEntity;
import name.huliqing.luoying.object.scene.SceneListener;
import name.huliqing.luoying.object.scene.SceneListenerAdapter;

/**
 * 镜头跟随，并使用setChase(Spatial) 来让镜头跟随某个目标。
 * @author huliqing
 */
public class ChaseCameraEntity extends NonModelEntity {

    // 跟随的目标实体的唯 一id
    private Long chaseTarget;
    
    // ---- inner
    private CollisionChaseCamera chaseCamera;
    private final SceneListener listener = new LocalSceneListener();
    // 关联的物理实体, 依赖于这个物理环境来检测镜头穿墙问题。
    private PhysicsEntity physicsEntity;

    @Override
    public void setData(EntityData data) {
        super.setData(data);
        chaseTarget = data.getAsLong("chaseTarget");
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("chaseTarget", chaseTarget);
        if (chaseCamera != null) {
            data.setAttribute("rotationEnabled", chaseCamera.isRotationEnabled());
            data.setAttribute("physicsEnabled", chaseCamera.isPhysicsEnabled());
        }
    }

    @Override
    protected void initEntity() {
        chaseCamera = new CollisionChaseCamera(LuoYing.getApp().getCamera(), LuoYing.getApp().getInputManager());
         // 开启镜头跟随可能让部分人容易犯头晕
        chaseCamera.setSmoothMotion(data.getAsBoolean("smoothMotion", chaseCamera.isSmoothMotion()));
        chaseCamera.setTrailingEnabled(data.getAsBoolean("trailingEnabled", chaseCamera.isTrailingEnabled()));
        chaseCamera.setInvertVerticalAxis(data.getAsBoolean("invertVerticalAxis", false));
        chaseCamera.setLookAtOffset(data.getAsVector3f("lookAtOffset", chaseCamera.getLookAtOffset()));
        chaseCamera.setZoomSensitivity(data.getAsFloat("zoomSensitivity", chaseCamera.getZoomSensitivity()));
        chaseCamera.setRotationSpeed(data.getAsFloat("rotationSpeed", chaseCamera.getRotationSpeed()));
        chaseCamera.setRotationSensitivity(data.getAsFloat("rotationSensitivity", chaseCamera.getRotationSensitivity()));
        chaseCamera.setMaxDistance(data.getAsFloat("maxDistance", chaseCamera.getMaxDistance()));
        chaseCamera.setMinDistance(data.getAsFloat("minDistance", chaseCamera.getMinDistance()));
        chaseCamera.setDefaultDistance(data.getAsFloat("defaultDistance", chaseCamera.getDistanceToTarget()));
        chaseCamera.setChasingSensitivity(data.getAsFloat("chasingSensitivity", chaseCamera.getChasingSensitivity()));
        chaseCamera.setDownRotateOnCloseViewOnly(data.getAsBoolean("downRotateOnCloseViewOnly", chaseCamera.getDownRotateOnCloseViewOnly())); 
        chaseCamera.setUpVector(data.getAsVector3f("upVector", chaseCamera.getUpVector()));
        chaseCamera.setHideCursorOnRotate(data.getAsBoolean("hideCursorOnRotate", chaseCamera.isHideCursorOnRotate()));
        chaseCamera.setRotationEnabled(data.getAsBoolean("rotationEnabled", chaseCamera.isRotationEnabled()));
        chaseCamera.setPhysicsEnabled(data.getAsBoolean("physicsEnabled", chaseCamera.isPhysicsEnabled()));
        chaseCamera.setEnabled(isEnabled());
    }
    
    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene); 
        // 从当前场景中查找PhysicsEntity, 使用它来处理碰撞检测
        // 注意必须确保PhysicsEntity已经初始化，否则可能会获取不到物理空间。
        // 在无法确定目标PhysicsEntity已经初始化的情况下需要添加监听。
        List<PhysicsEntity> envs = scene.getEntities(PhysicsEntity.class, new LinkedList<PhysicsEntity>());
        for (PhysicsEntity env : envs) {
            updatePhysics(env); 
            // PhysicsEntity在场景中只应该存在一个，不能存在多个
            break;
        }
        
        // 查找被跟随的对象
        if (chaseTarget != null) {
            updateChaseTarget(scene.getEntity(chaseTarget));
        }
        
        // 这里SceneListener用于监听获得物理空间
        scene.addSceneListener(listener);
    }
    
    @Override
    public void cleanup() {
        scene.removeSceneListener(listener);
        if (chaseCamera != null) {
            chaseCamera.cleanup();
            chaseCamera = null;
        }
        super.cleanup();
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (chaseCamera != null) {
            chaseCamera.setEnabled(enabled);
        }
    }
    
    /**
     * 直接设置跟随目标
     * @param spatial 
     */
    public void setChase(Spatial spatial) {
        chaseCamera.setChase(spatial);
    }
    
    /**
     * 设置要跟随的目标实体
     * @param entity 
     */
    public void setChaseEntity(Entity entity) {
        updateChaseTarget(entity);
    }
    
    /**
     * 是否打开旋转镜头功能。
     * @param enabled 
     */
    public void setRotationEnabled(boolean enabled) {
        chaseCamera.setRotationEnabled(enabled);
    }
    
    /**
     * 判断是否开始相机跟随功能
     * @return 
     */
    public boolean isRotationEnabled() {
        return chaseCamera.isRotationEnabled();
    }
    
    /**
     * 设置是否开启物理特性,物理碰撞、防穿墙功能
     * @param enabled 
     */
    public void setPhysicsEnabled(boolean enabled) {
        chaseCamera.setPhysicsEnabled(enabled);
    }
    
    /**
     * 判断是否开启物理特性,物理碰撞、防穿墙功能
     * @return 
     */
    public boolean isPhysicsEnabled() {
        return chaseCamera.isPhysicsEnabled();
    }
    
    /**
     * 计算出相机的跟随位置，但是不立即改变相机的位置。
     * @param locStore
     * @param rotationStore 
     */
    public void getComputeTransform(Vector3f locStore, Quaternion rotationStore) {
        chaseCamera.getComputeTransform(locStore, rotationStore);
    }
    
    private void updatePhysics(PhysicsEntity physicsEntity) {
        this.physicsEntity = physicsEntity;
        chaseCamera.setPhysicsSpace(this.physicsEntity.getBulletAppState().getPhysicsSpace());
    }
    
    private void updateChaseTarget(Entity entity) {
        if (entity == null) {
            this.chaseTarget = null;
            return;
        }
        this.chaseTarget = entity.getEntityId();
        this.chaseCamera.setChase(entity.getSpatial());
    }
    
    private class LocalSceneListener extends SceneListenerAdapter {
        
        @Override
        public void onSceneEntityAdded(Scene scene, Entity entityAdded) {
            if (chaseTarget != null && chaseTarget == entityAdded.getEntityId()) {
                updateChaseTarget(entityAdded);
            }
            if (entityAdded instanceof PhysicsEntity) {
                updatePhysics((PhysicsEntity) entityAdded);
            }
        }

        @Override
        public void onSceneEntityRemoved(Scene scene, Entity objectRemoved) {
            // 如果PhysicsEnv被移除，则镜头跟随要清除物理碰撞检测对象。
            if (physicsEntity != null && physicsEntity == objectRemoved) {
                chaseCamera.setPhysicsSpace(null);
                physicsEntity = null;
            }
            if (chaseTarget != null && chaseTarget == objectRemoved.getEntityId()) {
                updateChaseTarget(null);
            }
        }

        @Override
        public void onSceneEntityStateChanged(Scene scene, Entity entity) {
            super.onSceneEntityStateChanged(scene, entity);
            if (chaseTarget != null && chaseTarget == entity.getEntityId()) {
                updateChaseTarget(entity);
            }
        }
        
    }

}
