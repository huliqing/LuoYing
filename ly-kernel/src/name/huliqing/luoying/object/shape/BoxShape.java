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
package name.huliqing.luoying.object.shape;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import name.huliqing.luoying.data.ShapeData;

/**
 * @author huliqing
 * @param <T>
 */
public class BoxShape<T extends ShapeData> extends AbstractShape<T> {

    private Vector3f extents;
    
    // ---- inner
    private Box box;

    @Override
    public void setData(T data) {
        super.setData(data);
        extents = data.getAsVector3f("extents");
    }
    
    @Override
    public Mesh getMesh() {
        if (box == null) {
            box = new Box(extents.x, extents.y, extents.z);
        }
        return box;
    }

    
}
