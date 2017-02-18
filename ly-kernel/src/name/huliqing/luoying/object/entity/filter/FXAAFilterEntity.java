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
package name.huliqing.luoying.object.entity.filter;

import com.jme3.post.filters.FXAAFilter;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.entity.NonModelEntity;
import name.huliqing.luoying.object.scene.Scene;

/**
 *
 * @author huliqing
 */
public class FXAAFilterEntity extends NonModelEntity<EntityData> {

    private final FXAAFilter filter = new FXAAFilter();

    @Override
    protected void initEntity() {
        filter.setName(data.getId());
        filter.setReduceMul(data.getAsFloat("reduceMul", 0.125f)); // 1.0f/8.0f
        filter.setSpanMax(data.getAsFloat("spanMax", 8f));
        filter.setSubPixelShift(data.getAsFloat("subPixelShift", 0.25f)); // 1.0f / 4.0f;
        filter.setVxOffset(data.getAsFloat("vxOffset", 0f));
    }

    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene); 
        scene.addFilter(filter);
    }

    @Override
    public void cleanup() {
        if (scene != null) {
            scene.removeFilter(filter);
        }
        super.cleanup(); 
    }
    
}
