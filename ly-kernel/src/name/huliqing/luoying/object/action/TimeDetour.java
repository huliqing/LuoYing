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
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.utils.MathUtils;

/**
 * 通过时间检测来判断及避障
 * @author huliqing
 */
public class TimeDetour extends Detour{
    private final ActorService actorService = Factory.get(ActorService.class);
    
    // 遇到障碍物时的调转方向 =》　1:left; -1:right; 0: none;
    private int direction;
    
    // 角色的最近一次的位置
    private final Vector3f lastPos = new Vector3f();
    private final float minDistanceSquared = 0.5f * 0.5f;
    
    public TimeDetour() {}
    
    public TimeDetour(Action action) {
        super(action);
    }

    @Override
    public void setActor(Entity actor) {
        super.setActor(actor);
        lastPos.set(actor.getSpatial().getWorldTranslation());
    }

    @Override
    protected boolean isNeedDetour() {
//        float lastDistance = actor.getModel().getWorldTranslation().distance(lastPos);
        float distanceSquared = actor.getSpatial().getWorldTranslation().distanceSquared(lastPos);
        if (distanceSquared <= minDistanceSquared) {
            return true;
        } else {
//            Logger.get(getClass()).log(Level.INFO, "TimeDetour ---- Not need detour ----");
            lastPos.set(actor.getSpatial().getWorldTranslation());
            return false;
        }
    }

    @Override
    protected void tryDetour(int count) {
        if (count == 0) {
            direction = MathUtils.randomPON();
        }
        
        TempVars tv = TempVars.get();
        float angle = 30 * (count + 1) * FastMath.DEG_TO_RAD * direction;
        tv.vect1.set(actorService.getWalkDirection(actor));
        MathUtils.rotate(tv.vect1.normalizeLocal(), angle, Vector3f.UNIT_Y, tv.vect2);
        
        detour(tv.vect2);
        
        tv.release();
        
        float lockTime = count * 0.25f + 0.25f;
        action.lock(lockTime);
    }
    
}
