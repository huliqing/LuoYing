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
package name.huliqing.luoying.object.entity;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Vector3f;

/**
 * 定义地面物体。
 * @author huliqing
 */
public interface TerrainEntity extends Entity {
    
    /**
     * 获取地面的高度, 注：如果指定的位置超出地面边界范围，则返回null, 否则返回该位置处地面的最高点。
     * @param x
     * @param z
     * @return 
     */
    Vector3f getHeight(float x, float z);
    
    /**
     * 通过射线方式获取地面x,z处的位置点。
     * @param x
     * @param z
     * @return 
     */
    CollisionResults getHeightPoint(float x, float z);
}
