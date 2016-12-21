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
package name.huliqing.luoying.object.resist;

import name.huliqing.luoying.data.ResistData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.utils.MathUtils;

/**
 *
 * @author huliqing
 */
public abstract class AbstractResist implements Resist {
    
    protected ResistData data;
    protected boolean initialized;
    protected Entity entity;

    @Override
    public void setData(ResistData data) {
        this.data = data;
    }
    
    @Override
    public ResistData getData() {
        return data;
    }
    
    @Override
    public void initialize() {
        if (initialized) {
            throw new UnsupportedOperationException("Resist already initialized! resist=" + data.getId());
        }
        initialized = true;
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public void cleanup() {
        initialized = false;
    }
    
    @Override
    public float getResist() {
        return data.getValue();
    }
    
    @Override
    public void addResist(float resist) {
        data.setValue(MathUtils.clamp(data.getValue() + resist, 0.0f, 1.0f));
    }
    
    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
    
}
