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

import com.jme3.scene.Spatial;
import name.huliqing.luoying.data.EffectData;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.effect.Effect;
import name.huliqing.luoying.object.entity.ModelEntity;

/**
 * @author huliqing
 */
public class EffectEntity extends ModelEntity {
    
    private Effect effect;
    private EffectData ed;
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        ed = data.getAsObjectData("effect");
    }

    @Override
    public void updateDatas() {
        super.updateDatas();
        if (effect != null) {
            effect.updateDatas();
            data.setAttribute("effect", effect.getData());
        }
    }
    
    @Override
    protected Spatial loadModel() {
        if (ed != null) {
            effect = Loader.load(ed);
        }
        return effect;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        if (!effect.isInitialized()) {
            effect.initialize();
        }
    }

    @Override
    public void cleanup() {
        if (effect != null && effect.isInitialized()) {
            effect.cleanup();
        }
        super.cleanup(); 
    }
    
}
