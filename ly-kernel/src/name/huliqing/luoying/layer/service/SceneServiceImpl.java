/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.object.entity.impl.SimpleTerrainEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.utils.GeometryUtils;

/**
 * 场景服务类
 * @author huliqing
 */
public class SceneServiceImpl implements SceneService {

    private final ThreadLocal<TempRay> tempRay = new ThreadLocal<TempRay>();
    
    @Override
    public void inject() {
        // ignore
    }

    @Override
    public Vector3f getSceneHeight(Scene scene, float x, float z) {
        List<SimpleTerrainEntity> sos = scene.getEntities(SimpleTerrainEntity.class, new ArrayList<SimpleTerrainEntity>());
        Vector3f terrainHeight = null;
        for (SimpleTerrainEntity so : sos) {
            Spatial terrain = so.getSpatial();
            Vector3f terrainPoint = getHeight(terrain, x, z);
            if (terrainPoint != null) {
                if (terrainHeight == null || terrainPoint.y > terrainHeight.y) {
                    terrainHeight = terrainPoint;
                }
            }
        }
        return terrainHeight;
    }
    
    private Vector3f getHeight(Spatial spatial, float x, float z) {
        TempRay tr = tempRay.get();
        if (tr == null) {
            tr = new TempRay();
            tempRay.set(tr);
        }
        float maxHeight = GeometryUtils.getModelHeight(spatial);
        tr.ray.origin.set(x, maxHeight + 1, z);
        tr.ray.direction.set(0,-1,0);
        tr.collisionResults.clear(); // notice clear
        spatial.collideWith(tr.ray, tr.collisionResults);
        if (tr.collisionResults.size() <= 0) {
            return null;
        }
        return tr.collisionResults.getClosestCollision().getContactPoint();
    }

    private class TempRay {
        final Ray ray = new Ray();
        final CollisionResults collisionResults = new CollisionResults();
    }
}
