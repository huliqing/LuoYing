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

import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.layer.service.SystemService;
import name.huliqing.luoying.object.scene.Scene;

/**
 * ShadowEntity的抽象类，无任何方法。
 * @author huliqing
 */
public abstract class ShadowEntity extends NonModelEntity {
    private final SystemService systemService = Factory.get(SystemService.class);

    /** 在指定的平台上关闭阴影功能 */
    protected List<String> disabledOnPlatforms;
    
    // ---- 

    @Override
    public void setData(EntityData data) {
        super.setData(data);
        disabledOnPlatforms = data.getAsStringList("disabledOnPlatforms");
    }
    
    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        String platform = systemService.getPlatformName();
        if (disabledOnPlatforms != null && disabledOnPlatforms.contains(platform)) {
            setEnabled(false);
        }
    }
    
    /**
     * 获取阴影的强度,返回值为0.0~1.0f
     * @return 
     */
    public abstract float getShadowIntensity();
    
    /**
     * 设置阴影的强度，值[0.0, 1.0]
     * @param shadowIntensity 
     */
    public abstract void setShadowIntensity(float shadowIntensity);
    
}
