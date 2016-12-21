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
package name.huliqing.luoying.layer.service;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.object.entity.impl.SimpleTerrainEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.utils.GeometryUtils;

/**
 * 场景服务类
 * @author huliqing
 */
public class SceneServiceImpl implements SceneService {
    private PlayService playService;

    private final ThreadLocal<TempRay> tempRay = new ThreadLocal<TempRay>();
    
    @Override
    public void inject() {
        playService = Factory.get(PlayService.class);
    }

    @Override
    public Vector3f getSceneHeight(float x, float z) {
        Scene scene = playService.getGame().getScene();
        return getSceneHeight(scene, x, z);
    }

    @Override
    public Vector3f getSceneHeight(float x, float z, Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        Vector3f result = getSceneHeight(playService.getGame().getScene(), x, z);
        if (result != null) {
            store.set(result);
        }
        return store;
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
