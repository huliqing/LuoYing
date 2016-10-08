/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.env;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.SceneProcessor;
import com.jme3.scene.Spatial;
import java.util.Collection;
import name.huliqing.ly.Ly;
import name.huliqing.ly.data.env.EnvData;
import name.huliqing.ly.object.SceneObject;
import name.huliqing.ly.object.scene.Scene;
import name.huliqing.ly.object.scene.SceneListener;
import name.huliqing.ly.utils.CollisionChaseCamera;

/**
 * 镜头跟随Env,这个Env需要游戏中主动从Scene中获取，并使用setChase(Spatial) 来让镜头跟随某个目标。
 * @author huliqing
 * @param <T>
 */
public class CameraChaseEnv <T extends EnvData> extends AbstractEnv <T> implements SceneListener {

    private CollisionChaseCamera ccc;
    // 关联的物理Env, ccc依赖于这个物理环境来检测镜头穿墙问题。
    private PhysicsEnv physicsEnv;
    
    @Override
    public void setData(T data) {
        super.setData(data);
    }

    @Override
    public void updateDatas() {
        if (initialized) {
            data.setLocation(ccc.getCamera().getLocation());
            data.setRotation(ccc.getCamera().getRotation());
//            data.setScale(xxx);// no scale for camera
        }
    }

//    private void updateData() {
//    }
    
    @Override
    public void initialize(Scene scene) {
        super.initialize(scene);
        ccc = new CollisionChaseCamera(Ly.getApp().getCamera(), Ly.getApp().getInputManager());
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
        
        // 从当前场景中查找PhysicsEnv,使用它来处理碰撞检测
        // 注意必须确保PhysicsEnv已经初始化，否则可能会获取不到物理空间。
        // 在无法确定目标PhysicsEnv已经初始化的情况下需要添加监听。从被监视的Env中获取PhysicsEnv
        Collection<SceneObject> envs = scene.getSceneObjects();
        for (SceneObject env : envs) {
            
            // remove20161008,不再需要，getSceneObjects获得的都是已经初始化过了的
//            if (!env.isInitialized()) {
//                continue;
//            }

            if (env instanceof PhysicsEnv) {
                updatePhysics((PhysicsEnv) env);
                break;
            }
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
    
    private void updatePhysics(PhysicsEnv pe) {
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
    
    @Override
    public Vector3f getLocation() {
        if (initialized) {
            return ccc.getCamera().getLocation();
        }
        return data.getLocation();
    }

    @Override
    public void setLocation(Vector3f location) {
        data.setLocation(location);
    }

    @Override
    public Quaternion getRotation() {
        if (initialized) {
            return ccc.getCamera().getRotation();
        }
        return data.getRotation();
    }

    @Override
    public void setRotation(Quaternion rotation) {
        data.setRotation(rotation);
    }

    @Override
    public Vector3f getScale() {
        return data.getScale();
    }

    @Override
    public void setScale(Vector3f scale) {
        data.setScale(scale);
    }

    // ---- Listener-------------------------------------------------------------------------------------------------------------------
    
    @Override
    public void onSceneInitialized(Scene scene) {
        // ignore
    }

    @Override
    public void onSceneObjectAdded(Scene scene, SceneObject entityAdded) {
        if (physicsEnv != null)
            return;
        if (entityAdded instanceof PhysicsEnv) {
            updatePhysics((PhysicsEnv) entityAdded);
        }
    }

    @Override
    public void onSceneObjectRemoved(Scene scene, SceneObject objectRemoved) {
        // 如果PhysicsEnv被移除，则镜头跟随要清除物理碰撞检测对象。
        if (physicsEnv != null && physicsEnv == objectRemoved) {
            ccc.setPhysicsSpace(null);
            physicsEnv = null;
        }
    }

    @Override
    public void onSpatialAdded(Scene scene, Spatial spatialAdded) {
    }

    @Override
    public void onSpatialRemoved(Scene scene, Spatial objectRemoved) {
    }

    @Override
    public void onProcessorAdded(Scene scene, SceneProcessor processorAdded) {
    }

    @Override
    public void onProcessorRemoved(Scene scene, SceneProcessor processorRemoved) {
    }

}
