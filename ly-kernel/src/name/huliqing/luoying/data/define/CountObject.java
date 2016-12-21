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
package name.huliqing.luoying.data.define;

/**
 * 可量化物体接口，这类物体可以计算数量，可以获取数量及设置数量。
 * 游戏中所有可进行量化的物体都应该实体这个接口，以便获得公共支持，如：普通杂物、装备等。只有实现这个接口，物品
 * 才可能在角色之间交换、发送、买卖等。
 * @author huliqing
 */
public interface CountObject {
    
    /**
     * 获取物体的总数
     * @return 
     */
    int getTotal();
    
    /**
     * 设置物体的总数
     * @param total 
     */
    void setTotal(int total);
    
}
