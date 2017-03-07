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

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Spatial;

/**
 * 动态碰撞网格(hull collision shape), 适合于质量大于0的可移动的角色或物体，如：人物、生物、坦克等.
 * @author huliqing
 */
public class DynamicMeshPhysicsShape extends AbstractPhysicsShape {

    @Override
    public CollisionShape getCollisionShape(Spatial spatial) {
        return CollisionShapeFactory.createDynamicMeshShape(spatial);
    }
    
}
