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
package name.huliqing.luoying.utils;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * 射线工具类
 * @author huliqing
 */
public class RayUtils {
    
    /**
     * 检测碰撞物
     * @param origin 射线原点
     * @param direction 射线方向
     * @param root 用于检测碰撞的根节点
     * @param store 存放结果
     * @deprecated 不再使用，可能存在BUG。
     * @return 
     */
    public static CollisionResults collideWith(Vector3f origin, Vector3f direction
            , Spatial root, CollisionResults store) {
        Temp temp = Temp.get();
        temp.vec1.set(direction);
        temp.ray.setOrigin(origin);
        temp.ray.setDirection(temp.vec1.normalizeLocal());
        root.collideWith(temp.ray, store);
        temp.release();
        return store;
    }
}
