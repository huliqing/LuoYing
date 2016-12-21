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

import com.jme3.math.Vector3f;
import name.huliqing.luoying.data.PositionData;

/**
 * 固定点
 * @author huliqing
 * @param <T>
 */
public class FixedPosition<T extends PositionData> extends AbstractPosition<T> {

    private Vector3f pos;

    @Override
    public void setData(T data) {
        super.setData(data); 
        this.pos = data.getAsVector3f("point");
    }

    @Override
    public Vector3f getPoint(Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        store.set(pos);
        return store;
    }
    
}
