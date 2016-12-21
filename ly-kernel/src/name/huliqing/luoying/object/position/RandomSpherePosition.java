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
package name.huliqing.luoying.object.position;

import com.jme3.effect.shapes.EmitterShape;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.math.Vector3f;
import name.huliqing.luoying.data.PositionData;

/**
 * 圆球内的随机点
 * @author huliqing
 * @param <T>
 */
public final class RandomSpherePosition<T extends PositionData> extends AbstractPosition<T> {

    private EmitterShape shape;
    
    @Override
    public void setData(T data) {
        super.setData(data);
        shape = new EmitterSphereShape(
                  data.getAsVector3f("center")
                , data.getAsFloat("radius", 1));
    }

    @Override
    public Vector3f getPoint(Vector3f store) {
        shape.getRandomPoint(store);
        return store;
    }
    
}
