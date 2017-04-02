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
package name.huliqing.editor.edit.controls.entity;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 空的操作物体，相关的Entity不能被直接操作，该类主要用于所有那些不能直接可视化操作的实体Entity
 * @author huliqing
 * @param <T>
 */
public class EmptyEntityControlTile <T extends Entity> extends EntityControlTile<T> {

    @Override
    public Spatial getControlSpatial() {
        return null;
    }

    @Override
    protected void onLocationUpdated(Vector3f location) {
        // ignore
    }

    @Override
    protected void onRotationUpdated(Quaternion rotation) {
        // ignore
    }

    @Override
    protected void onScaleUpdated(Vector3f scale) {
        // ignore
    }
    
}
