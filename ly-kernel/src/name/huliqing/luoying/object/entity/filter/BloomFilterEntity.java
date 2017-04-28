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

import com.jme3.post.filters.BloomFilter;
import name.huliqing.luoying.object.entity.NonModelEntity;
import name.huliqing.luoying.object.scene.Scene;

/**
 *
 * @author huliqing
 */
public class BloomFilterEntity extends NonModelEntity {
    
    private final BloomFilter filter = new BloomFilter();       

    @Override
    protected void initEntity() {
        filter.setName(data.getId());
        filter.setBloomIntensity(data.getAsFloat("bloomIntensity", 2.0f));
        filter.setBlurScale(data.getAsFloat("blurScale", 1.5f));
        filter.setDownSamplingFactor(data.getAsFloat("downSamplingFactor", 1f));
        filter.setExposureCutOff(data.getAsFloat("exposureCutOff", 0));
        filter.setExposurePower(data.getAsFloat("exposurePower", 5.0f));
    }
    
    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene); 
        scene.addFilter(filter);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        filter.setEnabled(enabled);
    }
    
    @Override
    public void cleanup() {
        if (scene != null) {
            scene.removeFilter(filter);
        }
        super.cleanup();
    }
    
}
