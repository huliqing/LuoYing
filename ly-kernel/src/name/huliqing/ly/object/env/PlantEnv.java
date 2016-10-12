/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.env;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.Ly;
import name.huliqing.ly.data.PlantEntityData;
import name.huliqing.ly.object.entity.ModelEntity;
import name.huliqing.ly.object.entity.TerrainEntity;
import name.huliqing.ly.object.scene.Scene;
import name.huliqing.ly.object.scene.SceneListener;
import name.huliqing.ly.object.scene.SceneListenerAdapter;

/**
 * 植被环境物体，如：花草等物体
 * @author huliqing
 * @param <T>
 */
public class PlantEnv<T extends PlantEntityData> extends ModelEntity<T> {

    private SceneListener sceneListener;

    @Override
    protected Spatial loadModel() {
        return Ly.getAssetManager().loadModel(data.getAsString("file"));
    }
    
    @Override
    public void initialize(Scene scene) {
        super.initialize(scene);
        sceneListener = new SceneListenerAdapter() {
            @Override
            public void onSceneInitialized(Scene scene) {
                // 把植皮移到地形上面
                makePlantOnTerrain(scene);
                // 在处理完位置点之后就可以不再需要侦听了
                scene.removeSceneListener(this);
            }
        };
        scene.addSceneListener(sceneListener);
    }

    @Override
    public void cleanup() {
        scene.removeSceneListener(sceneListener);
        super.cleanup();
    }

    private void makePlantOnTerrain(Scene scene) {
        // 在场景载入完毕之后将植皮位置移到terrain节点的上面。
        List<TerrainEntity> sos = scene.getEntities(TerrainEntity.class, new ArrayList<TerrainEntity>());
        Vector3f location = null;
        for (TerrainEntity terrain : sos) {
            Vector3f terrainPoint = terrain.getHeight(model.getWorldTranslation().x, model.getWorldTranslation().z);
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
    }

    
}
