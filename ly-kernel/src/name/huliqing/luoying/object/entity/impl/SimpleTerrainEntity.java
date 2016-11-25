/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity.impl;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.object.entity.ModelEntity;
import name.huliqing.luoying.object.entity.TerrainEntity;
import name.huliqing.luoying.utils.GeometryUtils;

/**
 * 地形环境
 * @author huliqing
 */
public class SimpleTerrainEntity extends ModelEntity implements TerrainEntity {
    
    // 射线用于获取地面高度
    private final Ray ray = new Ray();
    private final CollisionResults results = new CollisionResults();
    
    private Spatial terrain;
    
    @Override
    public Spatial loadModel() {
        terrain = LuoYing.getAssetManager().loadModel(data.getAsString("file"));
        return terrain;
    }

    @Override
    public Vector3f getHeight(float x, float z) {
        float maxHeight = GeometryUtils.getModelHeight(spatial);
        ray.origin.set(x, maxHeight + 1, z);
        ray.direction.set(0,-1,0);
        results.clear();
        spatial.collideWith(ray, results);
        if (results.size() <= 0) {
            return null;
        }
        return results.getClosestCollision().getContactPoint();
    }
    
}
