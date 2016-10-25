/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.env;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.object.entity.ModelEntity;
import name.huliqing.luoying.object.entity.TerrainEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.scene.SceneListener;
import name.huliqing.luoying.object.scene.SceneListenerAdapter;

/**
 * 植被环境物体，如：花草等物体
 * @author huliqing
 */
public abstract class PlantEnv extends ModelEntity {

    private SceneListener sceneListener;

    @Override
    protected Spatial loadModel() {
        return LuoYing.getAssetManager().loadModel(data.getAsString("file"));
    }
    
    @Override
    public void initEntity() {
        super.initEntity();
    }

    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        if (scene.isInitialized()) {
            // 把植皮移到地形上面
            makePlantOnTerrain(scene);
        } else {
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
    }

    @Override
    public void cleanup() {
        if (sceneListener != null) {
            scene.removeSceneListener(sceneListener);
        }
        super.cleanup();
    }

    private void makePlantOnTerrain(Scene scene) {
        // 在场景载入完毕之后将植皮位置移到terrain节点的上面。
        List<TerrainEntity> sos = scene.getEntities(TerrainEntity.class, new ArrayList<TerrainEntity>());
        Vector3f location = null;
        for (TerrainEntity terrain : sos) {
            Vector3f terrainPoint = terrain.getHeight(spatial.getWorldTranslation().x, spatial.getWorldTranslation().z);
            if (terrainPoint != null) {
                if (location == null || terrainPoint.y > location.y) {
                    location = terrainPoint;
                }
            }
        }
        
        if (location != null) {
            location.addLocal(0, -0.1f, 0); // 下移一点
            RigidBodyControl rbc = spatial.getControl(RigidBodyControl.class);
            if (rbc != null) {
                rbc.setPhysicsLocation(location);
            } else {
                spatial.setLocalTranslation(location);
            }
        } 
    }

    
}
