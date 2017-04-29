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
package name.huliqing.luoying.object.entity.impl;

/**
 * 环境物体树，这个处理器为树模型定制一个更简单的物理包围盒。来简化物体碰撞
 * 计算。
 * @author huliqing
 */
public class TreeEntity extends PlantEntity {

    // remove20170429，不再硬编码给树实体设置RigidBodyControl
//    @Override
//    public void onInitScene(Scene scene) {
//        super.onInitScene(scene);
//        // 树体用了一个定制的MeshCollisionShape, 注：MeshCollisionShape类型必须要求mass=0
//        RigidBodyControl rbc = spatial.getControl(RigidBodyControl.class);
//        if (rbc != null) {
//            rbc.setMass(0);
//            rbc.setCollisionShape(new MeshCollisionShape(new TreeCollisionMesh()));
//        }
//    }
    
}
