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
package name.huliqing.luoying.object.module;

import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.item.Item;

/**
 * 监听角色物品的增加及删除
 * @author huliqing
 * @deprecated Use EntityDataListener instead
 */
public interface ItemListener {
    
    /**
     * 监听角色物品添加，当角色包裹获得物品时该方法被调用。
     * @param source 源角色
     * @param item 新添加的物品
     * @param trueAdded 实际的添加数量 
     */
    void onItemAdded(Entity source, Item item, int trueAdded);
    
    /**
     * 监听角色的物品删除,当角色包裹中的物品被移除时该方法被调用。
     * @param source 源角色
     * @param item 被删除的物品
     * @param trueRemoved 实际的删除数量
     */
    void onItemRemoved(Entity source, Item item, int trueRemoved);
    
    /**
     * 当角色使用了物品之后该方法被调用
     * @param source
     * @param item 被使用的物品
     */
    void onItemUsed(Entity source, Item item);
}
