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
import name.huliqing.luoying.object.skin.Skin;

/**
 * 监听角色的装备的穿戴
 * @author huliqing
 */
public interface SkinListener {
    
    /**
     * 当角色添加了装备之后该方法被调用。
     * @param actor
     * @param skinAdded 刚添加的装备
     */
    void onSkinAdded(Entity actor, Skin skinAdded);
    
    /**
     * 当角色装备被移除之后该方法被调用.
     * @param actor
     * @param skinRemoved 刚移除的装备
     */
    void onSkinRemoved(Entity actor, Skin skinRemoved);
    
    /**
     * 角色穿上装备后触发
     * @param actor
     * @param skin 被穿上的装备
     */
    void onSkinAttached(Entity actor, Skin skin);
    
    /**
     * 角色脱下装备后触发
     * @param actor
     * @param skin 被脱下的装备
     */
    void onSkinDetached(Entity actor, Skin skin);
}
