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
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.layer.network.SkinNetwork;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.SkinListener;

/**
 *
 * @author huliqing
 */
public interface SkinService extends SkinNetwork {
    
    /**
     * 判断目标角色的武器是否拿在手上。
     * @param actor
     * @return 
     */
    boolean isWeaponTakeOn(Entity actor);
    
    /**
     * 获取角色当前正在使用的武器类型状态
     * @param actor
     * @return 
     */
    long getWeaponState(Entity actor);
    
    /**
     * 添加皮肤侦听器
     * @param actor
     * @param skinListener 
     */
    void addSkinListener(Entity actor, SkinListener skinListener);
    
    /**
     * 移除皮肤侦听器
     * @param actor
     * @param skinListener
     * @return 
     */
    boolean removeSkinListener(Entity actor, SkinListener skinListener);
}
