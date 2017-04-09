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
package name.huliqing.luoying.object.entity;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * 非模型类型的场景实体，这类实体一般不需要有实际存在于场景中的可视模型，一般如各类环境效果：灯光、水体（Filter)、
 * 物理环境、天空、相机、阴影渲染及各类Filter\Scene Processor
 * @author huliqing
 */
public abstract class NonModelEntity  extends AbstractEntity {

    /** 
     * 这个物体作为所有不需要实际存在的Entity的Spatial用于
     * {@link #getSpatial() } 方法的调用返回, 避免在调用getSpatial的时候返回null.
     */
    private final Node NULL_ROOT = new Node(getClass().getName());

    @Override
    protected Spatial initSpatial() {
        return NULL_ROOT;
    }
    
}
