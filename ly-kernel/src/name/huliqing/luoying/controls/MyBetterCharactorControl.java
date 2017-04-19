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
package name.huliqing.luoying.controls;

import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.List;

/**
 * MyBetterCharactorControl用于继承并处理一些BetterCharacterControl的BUG。
 * @author huliqing
 */
public class MyBetterCharactorControl extends BetterCharacterControl {
    
    private float checkOnGroundOffset = 0.2f;
    
    public MyBetterCharactorControl(float radius, float height, float mass) {
        super(radius, height, mass);
    }
    
    public void setCheckOnGroundOffset(float checkOnGroundOffset) {
        this.checkOnGroundOffset = checkOnGroundOffset;
    }

    // BetterCharacterControl使用这个方法来判断是否角色正处于地面上，使用rayTest的方式，但由于射线的长度不够，默认
    // 父类情况是height + 0.1, 但是这个长度稍微有一些不够，特别是在一些地面比较崎岖的情况时经常会出现一些明明是在地面，
    // 但是却判断为不是在地面的情况，这导致明明是在地面上，却无法进行正常跳跃的情况， 所以这里稍微增加了一点长度。
    @Override
    protected void checkOnGround() {
        TempVars vars = TempVars.get();
        Vector3f location = vars.vect1;
        Vector3f rayVector = vars.vect2;
        float height = getFinalHeight();
        location.set(localUp).multLocal(height).addLocal(this.location);
        rayVector.set(localUp).multLocal(-height - checkOnGroundOffset).addLocal(location);
        List<PhysicsRayTestResult> results = space.rayTest(location, rayVector);
        vars.release();
        for (PhysicsRayTestResult physicsRayTestResult : results) {
            if (!physicsRayTestResult.getCollisionObject().equals(rigidBody)) {
                onGround = true;
                return;
            }
        }
        onGround = false;
    }
}
