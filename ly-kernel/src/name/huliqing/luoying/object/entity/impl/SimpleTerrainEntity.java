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
        String file = data.getAsString("file");
        terrain = LuoYing.getAssetManager().loadModel(file);
        return terrain;
    }

    @Override
    public Vector3f getHeight(float x, float z) {
        float maxHeight = GeometryUtils.getModelHeight(terrain);
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
