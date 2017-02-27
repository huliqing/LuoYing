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

import com.jme3.scene.Spatial;
import name.huliqing.luoying.data.EffectData;
import name.huliqing.luoying.data.EffectEntityData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.effect.Effect;
import name.huliqing.luoying.object.scene.Scene;

/**
 *
 * @author huliqing
 */
public class EffectEntity extends ModelEntity<EffectEntityData> {
    
    private Effect effect;
    
    public EffectEntity() {}
    
    @Override
    public void setData(EffectEntityData data) {
        super.setData(data);
        if (effect == null) {
            EffectData ed = data.getEffectData();
            effect = Loader.load(ed);
        }
    }

    @Override
    public void updateDatas() {
        super.updateDatas();
        if (effect != null) {
            effect.updateDatas();
            data.setEffectData(effect.getData());
        }
    }
    
    @Override
    protected Spatial loadModel() {
        return effect;
    }

    @Override
    public void initEntity() {
        super.initEntity();
    }

    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        throw new UnsupportedOperationException();
    }
    
}
