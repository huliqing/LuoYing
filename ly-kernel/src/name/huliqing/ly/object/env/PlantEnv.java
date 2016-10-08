/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.env;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.post.SceneProcessor;
import name.huliqing.ly.data.env.ModelEnvData;
import com.jme3.scene.Spatial;
import java.util.Collection;
import name.huliqing.ly.object.SceneObject;
import name.huliqing.ly.object.scene.Scene;
import name.huliqing.ly.object.scene.SceneListener;
import name.huliqing.ly.utils.GeometryUtils;

/**
 * 植被环境物体，如：花草等物体
 * @author huliqing
 * @param <T>
 */
public class PlantEnv<T extends ModelEnvData> extends ModelEnv<T> implements SceneListener {

    @Override
    public void initialize(Scene scene) {
        super.initialize(scene);
        scene.addSceneListener(this);
    }

    @Override
    public void cleanup() {
        scene.removeSceneListener(this);
        super.cleanup(); 
    }

    @Override
    public void onSceneInitialized(Scene scene) {
        
        // 在场景载入完毕之后将植皮位置移到terrain节点的上面。
        
        Collection<SceneObject> sos = scene.getSceneObjects();
        Vector3f location = null;
        for (SceneObject so : sos) {
            if (!(so instanceof TerrainEnv)) {
                continue;
            }
            Spatial terrain = ((TerrainEnv) so).getSpatial();
            Vector3f terrainPoint = GeometryUtils.getModelHeight(terrain, data.getLocation().x, data.getLocation().z);
            if (terrainPoint != null) {
                if (location == null || terrainPoint.y > location.y) {
                    location = terrainPoint;
                }
            }
        }
        
        if (location != null) {
            // 下移一点
            location.subtractLocal(0, 0.5f, 0);

            RigidBodyControl rbc = model.getControl(RigidBodyControl.class);
            if (rbc != null) {
                rbc.setPhysicsLocation(location);
            } else {
                model.setLocalTranslation(location);
            }
        } 
        
        // 在处理完位置点之后就可以不再需要侦听了
        scene.removeSceneListener(this);
    }

    @Override
    public void onSceneObjectAdded(Scene scene, SceneObject objectAdded) {
    }

    @Override
    public void onSceneObjectRemoved(Scene scene, SceneObject objectRemoved) {
    }

    @Override
    public void onSpatialAdded(Scene scene, Spatial spatialAdded) {
    }

    @Override
    public void onSpatialRemoved(Scene scene, Spatial spatialRemoved) {
    }

    @Override
    public void onProcessorAdded(Scene scene, SceneProcessor processorAdded) {
    }

    @Override
    public void onProcessorRemoved(Scene scene, SceneProcessor processorRemoved) {
    }
    
}
