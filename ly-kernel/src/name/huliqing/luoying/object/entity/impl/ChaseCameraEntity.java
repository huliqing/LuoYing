/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity.impl;

import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.scene.SceneListener;
import name.huliqing.luoying.utils.CollisionChaseCamera;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.NonModelEntity;

/**
 * 镜头跟随Env,这个Env需要游戏中主动从Scene中获取，并使用setChase(Spatial) 来让镜头跟随某个目标。
 * @author huliqing
 */
public class ChaseCameraEntity extends NonModelEntity  implements SceneListener {

    private CollisionChaseCamera ccc;
    // 关联的物理Env, ccc依赖于这个物理环境来检测镜头穿墙问题。
    private PhysicsEntity physicsEnv;
    
    @Override
    public void updateDatas() {
        super.updateDatas();
    }
    
    @Override
    public void initEntity() {
        ccc = new CollisionChaseCamera(LuoYing.getApp().getCamera(), LuoYing.getApp().getInputManager());
         // 开启镜头跟随可能让部分人容易犯头晕
        ccc.setSmoothMotion(data.getAsBoolean("smoothMotion", ccc.isSmoothMotion()));
        ccc.setTrailingEnabled(data.getAsBoolean("trailingEnabled", ccc.isTrailingEnabled()));
        ccc.setInvertVerticalAxis(data.getAsBoolean("invertVerticalAxis", false));
        ccc.setLookAtOffset(data.getAsVector3f("lookAtOffset", ccc.getLookAtOffset()));
        ccc.setZoomSensitivity(data.getAsFloat("zoomSensitivity", ccc.getZoomSensitivity()));
        ccc.setRotationSpeed(data.getAsFloat("rotationSpeed", ccc.getRotationSpeed()));
        ccc.setRotationSensitivity(data.getAsFloat("rotationSensitivity", ccc.getRotationSensitivity()));
        ccc.setMaxDistance(data.getAsFloat("maxDistance", ccc.getMaxDistance()));
        ccc.setMinDistance(data.getAsFloat("minDistance", ccc.getMinDistance()));
        ccc.setDefaultDistance(data.getAsFloat("defaultDistance", ccc.getDistanceToTarget()));
        ccc.setChasingSensitivity(data.getAsFloat("chasingSensitivity", ccc.getChasingSensitivity()));
        ccc.setDownRotateOnCloseViewOnly(data.getAsBoolean("downRotateOnCloseViewOnly", ccc.getDownRotateOnCloseViewOnly())); 
        ccc.setUpVector(data.getAsVector3f("upVector", ccc.getUpVector()));
        ccc.setHideCursorOnRotate(data.getAsBoolean("hideCursorOnRotate", ccc.isHideCursorOnRotate()));
    }

    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene); 
        // 从当前场景中查找PhysicsEnv,使用它来处理碰撞检测
        // 注意必须确保PhysicsEnv已经初始化，否则可能会获取不到物理空间。
        // 在无法确定目标PhysicsEnv已经初始化的情况下需要添加监听。从被监视的Env中获取PhysicsEnv
        List<PhysicsEntity> envs = scene.getEntities(PhysicsEntity.class, new ArrayList<PhysicsEntity>());
        for (PhysicsEntity env : envs) {
            updatePhysics(env);
            break;
        }
        // 这里SceneListener用于监听获得物理空间
        scene.addSceneListener(this);
    }
    
    @Override
    public void cleanup() {
        scene.removeSceneListener(this);
        ccc.cleanup();
        super.cleanup();
    }
    
    private void updatePhysics(PhysicsEntity pe) {
        physicsEnv = pe;
        ccc.setPhysicsSpace(physicsEnv.getBulletAppState().getPhysicsSpace());
    }

    /**
     * 设置跟随目标
     * @param spatial 
     */
    public void setChase(Spatial spatial) {
        ccc.setChase(spatial);
    }
    
    /**
     * 是否打开旋转镜头功能。
     * @param bool 
     */
    public void setEnabledRotation(boolean bool) {
        ccc.setEnabledRotation(bool);
    }
    
    // ---- Listener-------------------------------------------------------------------------------------------------------------------
    
    @Override
    public void onSceneLoaded(Scene scene) {
        // ignore
    }

    @Override
    public void onSceneEntityAdded(Scene scene, Entity entityAdded) {
        if (physicsEnv != null)
            return;
        if (entityAdded instanceof PhysicsEntity) {
            updatePhysics((PhysicsEntity) entityAdded);
        }
    }

    @Override
    public void onSceneEntityRemoved(Scene scene, Entity objectRemoved) {
        // 如果PhysicsEnv被移除，则镜头跟随要清除物理碰撞检测对象。
        if (physicsEnv != null && physicsEnv == objectRemoved) {
            ccc.setPhysicsSpace(null);
            physicsEnv = null;
        }
    }


}
