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
package name.huliqing.luoying.object.action;

import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ActorModule;
import name.huliqing.luoying.utils.GeometryUtils;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.luoying.utils.Temp;

/**
 * 用射线检测方式避开障碍物
 * @author huliqing
 */
public class RayDetour extends Detour {
    // 遇到障碍物时的调转方向 =》　1:left; -1:right; 0: none;
    private int direction;
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    
    private final Vector3f tempDir = new Vector3f();
    
    public RayDetour() {}
    
    public RayDetour(Action action) {
        super(action);
    }

    @Override
    protected boolean isNeedDetour() {
        if (hasObstacleActor(actor, playService.getEntities(Actor.class, null))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void tryDetour(int count) {
        if (count == 0) {
            direction = MathUtils.randomPON();
        }
        
        float angle = 30 * (count + 1) * FastMath.DEG_TO_RAD * direction;
        tempDir.set(actorService.getWalkDirection(actor)).normalizeLocal();
        MathUtils.rotate(tempDir, angle, Vector3f.UNIT_Y, tempDir);
        detour(tempDir);
        
        float lockTime = count * 0.25f + 0.25f;
        action.lock(lockTime); 
    }
    
    private boolean hasObstacleActor(Entity self, List<Actor> actors) {
        TempVars tv = TempVars.get();
        Temp temp = Temp.get();
        
        ActorModule selfActorModule = self.getModuleManager().getModule(ActorModule.class);
        Vector3f viewDirection;
        if (selfActorModule != null) {
            viewDirection = selfActorModule.getViewDirection();
        } else {
            viewDirection = self.getSpatial().getWorldRotation().multLocal(new Vector3f(0,0,1));
        }
        
        Vector3f origin = tv.vect1.set(self.getSpatial().getWorldBound().getCenter());
        Vector3f dir = tv.vect2.set(viewDirection).normalizeLocal();
        float zExtent = GeometryUtils.getBoundingVolumeZExtent(self.getSpatial());
        origin.addLocal(dir.mult(zExtent, tv.vect3));
        
//        DebugDynamicUtils.debugArrow(self.toString(), origin, dir, zExtent);
        
        // 检查碰撞
//        float checkDistance = zExtent;
        float checkDistance = zExtent * 1.5f; // modify20160504, * 1.5f,稍微加大了一点距离
        float checkDistanceSquare = checkDistance * checkDistance;
        Vector3f targetOrigin = tv.vect4;
        
        boolean obstacle = false;
        Ray ray = temp.ray;
        ray.setOrigin(origin);
        ray.setDirection(dir);
        ray.setLimit(checkDistance);
        for (Entity a : actors) {
            if (a == self) {
                continue;
            }
            
            // 如果距离跳过checkDistance,则不视为障碍（该判断用于优化性能）
            // 减少ray检测
            targetOrigin.set(a.getSpatial().getWorldBound().getCenter());
            if (targetOrigin.distanceSquared(origin) > checkDistanceSquare) {
                continue;
            }
            
            // 使用ray也可以，但是使用WorldBound可能性能更好一些。
            if (a.getSpatial().getWorldBound().intersects(ray)) {
                obstacle = true;
                break;
            }
        }
        
        // 释放缓存
        tv.release();
        temp.release();
        return obstacle;
    }
}
