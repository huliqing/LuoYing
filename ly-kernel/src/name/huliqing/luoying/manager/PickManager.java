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
package name.huliqing.luoying.manager;

import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

/**
 * 鼠标选择工具类
 * @author huliqing
 */
public class PickManager {
    
    /**
     * 找出最接近的,可被选择的对象.如果最接近的对象不能被选择,则返回null.
     * @param inputManager (not null)
     * @param camera (not null)
     * @param root (not null)
     * @param resultStore
     * @return 
     */
    public static CollisionResults pick(InputManager inputManager, Camera camera, Spatial root, CollisionResults resultStore) {
        if (resultStore == null) {
            resultStore = new CollisionResults();
        }
        Vector2f v2d = inputManager.getCursorPosition();
        Vector3f click3d = camera.getWorldCoordinates(v2d, 0);
        // 注意DIR方向向量必须归一化，否则可能在collideWith的时候获取不到结果。
        // 实测：没归一化时与ray产生不到碰撞结果。
        Vector3f dir = camera.getWorldCoordinates(v2d, 1).subtract(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);
        root.collideWith(ray, resultStore);
        return resultStore;
    }
   
}
