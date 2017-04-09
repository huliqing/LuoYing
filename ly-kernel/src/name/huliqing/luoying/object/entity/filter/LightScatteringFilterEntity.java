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

import com.jme3.math.Vector3f;
import com.jme3.post.filters.LightScatteringFilter;
import name.huliqing.luoying.object.entity.NonModelEntity;
import name.huliqing.luoying.object.scene.Scene;

/**
 *
 * @author huliqing
 */
public class LightScatteringFilterEntity extends NonModelEntity {

    private final LightScatteringFilter filter = new LightScatteringFilter();
    
    @Override
    protected void initEntity() {
        filter.setName(data.getId());
        filter.setBlurStart(data.getAsFloat("blurStart", 0.02f));
        filter.setBlurWidth(data.getAsFloat("blurWidth", 0.9f));
        filter.setLightDensity(data.getAsFloat("lightDensity", 1.4f));
        filter.setLightPosition(data.getAsVector3f("lightPosition", Vector3f.ZERO.clone()));
        filter.setNbSamples(data.getAsInteger("nbSamples", 50)); // int
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
