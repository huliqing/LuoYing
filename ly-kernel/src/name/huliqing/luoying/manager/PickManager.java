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

import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import name.huliqing.luoying.utils.TempPick;

/**
 * 鼠标选择工具类
 * @author huliqing
 */
public class PickManager {
    
    /**
     * 选择最近的物体
     * @param ray
     * @param pickRoot
     * @return 
     */
    public final static Spatial pickSpatial(Ray ray, Spatial pickRoot) {
        TempPick tp = TempPick.get();
        CollisionResults cr = tp.results;
        cr.clear();
        pickResults(ray, pickRoot, cr);
        Spatial result = null;
        if (cr.size() > 0) {
            result = cr.getClosestCollision().getGeometry();
        }
        tp.release();
        return result;
    }
    
    /**
     * 获取最近的选择点,如果不存在则返回null.
     * @param ray
     * @param pickRoot
     * @return 
     */
    public static Vector3f pickPoint(Ray ray, Spatial pickRoot) {
        TempPick tp = TempPick.get();
        CollisionResults cr = tp.results;
        cr.clear();
        pickResults(ray, pickRoot, cr);
        Vector3f result = null;
        if (cr.size() > 0) {
            result = cr.getClosestCollision().getContactPoint();
        }
        tp.release();
        return result;
    }
    
    /**
     * 通过射线方式获取所有相交的物体
     * @param ray
     * @param pickRoot
     * @param store
     * @return 
     */
    public final static CollisionResults pickResults(Ray ray, Spatial pickRoot, CollisionResults store) {
        if (store == null) {
            store = new CollisionResults();
        }
        pickRoot.collideWith(ray, store);
        return store;
    }
    
    /**
     * 获取射线与指定Spatial的碰撞的最近点，如果不存在碰撞则返回null.
     * @param ray
     * @param spatial
     * @return 
     */
    public final static Float distanceOfPick(Ray ray, Spatial spatial) {
        BoundingVolume bv = spatial.getWorldBound();
        if (bv == null || !bv.intersects(ray))
            return null;
        
        TempPick tp = TempPick.get();
        CollisionResults cr = tp.results;
        cr.clear();
        PickManager.pickResults(ray, spatial, cr);
        Float result = null;
        if (cr.size() > 0) {
            result = cr.getClosestCollision().getDistance();
        }
        tp.release();
        return result;
    }
    
    /**
     * 获取鼠标点击到物体的世界位置点, 如果没有点击到指定的物体，则返回null.
     * @param camera
     * @param screenLoc
     * @param pickRoot 
     * @return 
     */
    public static Vector3f pick(Camera camera, Vector2f screenLoc, Spatial pickRoot) {
        TempPick tp = TempPick.get();
        CollisionResults cr = tp.results;
        cr.clear();
        pick(camera, screenLoc, pickRoot, cr);
        Vector3f result = null;
        if (cr.size() > 0) {
            result = cr.getClosestCollision().getContactPoint();
        }
        tp.release();
        return result;
    }
    
    /**
     * 找出最接近的,可被选择的对象.如果最接近的对象不能被选择,则返回null.
     * @param camera (not null)
     * @param screenLoc
     * @param pickRoot (not null)
     * @param store
     * @return 
     */
    public static CollisionResults pick(Camera camera, Vector2f screenLoc, Spatial pickRoot, CollisionResults store) {
        if (store == null) {
            store = new CollisionResults();
        }
        TempPick tp = TempPick.get();
        Ray ray = getPickRay(camera, screenLoc, tp.ray);
        pickRoot.collideWith(ray, store);
        tp.release();
        return store;
    }
    
    /**
     * 获取一条鼠标点击屏幕的射线。
     * @param camera
     * @param screenLoc
     * @param store
     * @return 
     */
    public static Ray getPickRay(Camera camera, Vector2f screenLoc, Ray store) {
        if (store == null) {
            store = new Ray();
        }
        TempVars tv = TempVars.get();
        Vector3f origin = camera.getWorldCoordinates(screenLoc, 0, tv.vect1);
        Vector3f dir = camera.getWorldCoordinates(screenLoc, 1, tv.vect2).subtractLocal(origin).normalizeLocal(); // 注意归一化
        store.setOrigin(origin);
        store.setDirection(dir);
        tv.release();
        return store;
    }


}
