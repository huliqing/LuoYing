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
package name.huliqing.luoying.object.skin;

import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.slot.Slot;

/**
 * 武器装备
 * @author huliqing
 */
public interface Weapon extends Skin {
    
    /**
     * 获取武器类型
     * @return 
     */
    String getWeaponType();
    
    /**
     * 获取当前武器占用的槽位,如果武器不支持任何槽位或者当前武器不占用任何槽位，则返回null.
     * @return 
     */
    Slot getUsingSlot();
    
    /**
     * 抽出武器
     * @param actor 
     */
    void takeOn(Entity actor);
    
    /**
     * 收起武器,
     * @param actor 
     */
    void takeOff(Entity actor);
    
}
