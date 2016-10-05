/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.env;

import com.jme3.app.Application;
import com.jme3.scene.Spatial;
import java.util.List;
import name.huliqing.ly.data.env.EnvData;
import name.huliqing.ly.object.scene.Scene;
import name.huliqing.ly.object.scene.Scene.SceneEnvListener;
import name.huliqing.ly.object.scene.Scene.SceneListener;
import name.huliqing.ly.utils.CollisionChaseCamera;

/**
 * 镜头跟随Env,这个Env需要游戏中主动从Scene中获取，并使用setChase(Spatial) 来让镜头跟随某个目标。
 * @author huliqing
 * @param <T>
 */
public class CameraChaseEnv <T extends EnvData> extends AbstractEnv <T> implements SceneEnvListener {

    private CollisionChaseCamera ccc;
    // 关联的物理Env, ccc依赖于这个物理环境来检测镜头穿墙问题。
    private PhysicsEnv physicsEnv;
    
    @Override
    public void setData(T data) {
        super.setData(data);
    }

    private void updateData() {
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
    public void initialize(Application app, Scene scene) {
        super.initialize(app, scene);
        ccc = new CollisionChaseCamera(app.getCamera(), app.getInputManager());
        
        // 更新数据
        updateData();
        
        // 从当前场景中查找PhysicsEnv,使用它来处理碰撞检测
        // 注意必须确保PhysicsEnv已经初始化，否则可能会获取不到物理空间。
        // 在无法确定目标PhysicsEnv已经初始化的情况下需要添加监听。从被监视的Env中获取PhysicsEnv
        List<Env> envs = scene.getEnvs();
        for (Env env : envs) {
            if (!env.isInitialized()) {
                continue;
            }
            if (env instanceof PhysicsEnv) {
                updatePhysics((PhysicsEnv) env);
                break;
            }
        }
        // 这里SceneEnvListener用于监听获得物理空间
        scene.addSceneEnvListener(this);
    }
    
    @Override
    public void cleanup() {
        scene.removeSceneEnvListener(this);
        ccc.cleanup();
        super.cleanup();
    }
    
    private void updatePhysics(PhysicsEnv pe) {
        physicsEnv = pe;
        ccc.setPhysicsSpace(physicsEnv.getBulletAppState().getPhysicsSpace());
    }

    @Override
    public void onSceneEnvInitialized(Scene scene, Env envInitialized) {
        if (physicsEnv != null)
            return;
        if (envInitialized instanceof PhysicsEnv) {
            updatePhysics((PhysicsEnv) envInitialized);
        }
    }

    @Override
    public void onSceneEnvAdded(Scene scene, Env envAdded) {
        if (physicsEnv != null)
            return;
        if (envAdded.isInitialized() && envAdded instanceof PhysicsEnv) {
            updatePhysics((PhysicsEnv) envAdded);
        }
    }

    @Override
    public void onSceneEnvRemoved(Scene scene, Env envRemoved) {
        // 如果PhysicsEnv被移除，则镜头跟随要清除物理碰撞检测对象。
        if (physicsEnv != null && physicsEnv == envRemoved) {
            ccc.setPhysicsSpace(null);
            physicsEnv = null;
        }
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

}
