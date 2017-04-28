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

import com.jme3.post.ssao.SSAOFilter;
import name.huliqing.luoying.object.entity.NonModelEntity;
import name.huliqing.luoying.object.scene.Scene;

/**
 *
 * @author huliqing
 */
public class SSAOFilterEntity extends NonModelEntity {
    
    private final SSAOFilter filter = new SSAOFilter();

    @Override
    protected void initEntity() {
        filter.setName(data.getId());
        filter.setBias(data.getAsFloat("bias", 0.1f));
        filter.setIntensity(data.getAsFloat("intensity", 1.5f));
        filter.setSampleRadius(data.getAsFloat("sampleRadius", 5.1f));
        filter.setScale(data.getAsFloat("ssaoScale", 0.2f)); // 参数指定为ssaoScale，避免和Entity的固定参数scale冲突
        
        // debug only
//        filter.setUseAo(true);
//        filter.setUseOnlyAo(true);
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
