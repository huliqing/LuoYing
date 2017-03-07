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
package name.huliqing.luoying.object.physicsshape;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.scene.Spatial;
import name.huliqing.luoying.data.PhysicsShapeData;

/**
 * 胶囊型的碰撞网格，适合质量大于0的可移动物体，特别是直立行走的生物角色，如人物.
 * @author huliqing
 */
public class CapsulePhysicsShape extends AbstractPhysicsShape {

    private float radius = 1.0f;
    private float height = 1.0f;
    private int axis = 1; // axis (0=X,1=Y,2=Z)
    
    @Override
    public void setData(PhysicsShapeData data) {
        super.setData(data);
        radius = data.getAsFloat("radius", radius);
        height = data.getAsFloat("height", height);
        axis = data.getAsInteger("axis", axis);
        if (axis < 0 || axis > 2) {
            axis = 1;
        }
    }
    
    @Override
    public CollisionShape getCollisionShape(Spatial spatial) {
        CapsuleCollisionShape ccs = new CapsuleCollisionShape(radius, height, axis);
        return ccs;
    }
    
}
