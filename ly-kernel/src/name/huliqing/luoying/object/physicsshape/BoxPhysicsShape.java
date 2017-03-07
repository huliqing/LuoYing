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

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.luoying.data.PhysicsShapeData;

/**
 * 使用物体的包围盒来创建碰撞网格。
 * @author huliqing
 */
public class BoxPhysicsShape extends AbstractPhysicsShape {
    
    private Vector3f extents = new Vector3f(0.5f,0.5f,0.5f);

    @Override
    public void setData(PhysicsShapeData data) {
        super.setData(data);
        extents = data.getAsVector3f("extents", extents);
    }

    @Override
    public CollisionShape getCollisionShape(Spatial spatial) {
        return new BoxCollisionShape(extents);
        
        // remove20170308
//        CollisionShape collisionShape;
//        if (spatial instanceof Geometry) {
//            Mesh mesh = ((Geometry) spatial).getMesh();
//            if (mesh instanceof Box) {
//                collisionShape = new BoxCollisionShape(new Vector3f(((Box) mesh).getXExtent(), ((Box) mesh).getYExtent(), ((Box) mesh).getZExtent()));
//                return collisionShape;
//            } 
//        }
//        return CollisionShapeFactory.createBoxShape(spatial);
    }
    
}
