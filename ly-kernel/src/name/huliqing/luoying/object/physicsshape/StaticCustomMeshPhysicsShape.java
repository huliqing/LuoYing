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
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import name.huliqing.luoying.data.PhysicsShapeData;
import name.huliqing.luoying.data.ShapeData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.shape.Shape;

/**
 * 自定义的静态网格碰撞物体，这个物体会关联到一个Shape物体，由这个Shape的网格来定义碰撞，
 * 和{@link StaticMeshPhysicsShape }一样，只能用于质量为0的不可移动的物体，如，如地形，房屋，树木等。
 * 
 * @author huliqing
 */
public class StaticCustomMeshPhysicsShape extends AbstractPhysicsShape {

    private ShapeData shape;
    
    @Override
    public void setData(PhysicsShapeData data) {
        super.setData(data);
        shape = data.getAsObjectData("shape");
    }
    
    @Override
    public CollisionShape getCollisionShape(Spatial spatial) {
        Mesh mesh;
        if (shape != null) {
            Shape shapeObj = Loader.load(shape);
            mesh = shapeObj.getMesh();
        } else {
            mesh = new Box(0.5f, 0.5f, 0.5f); // 如果没有指定shape,则默认使用一个单位box.
        }
        MeshCollisionShape mcs = new MeshCollisionShape(mesh);
//        mcs.setScale(xxx);
        return mcs;
    }
    
}
