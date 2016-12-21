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
import name.huliqing.luoying.object.state.State;

/**
 * 监听角色状态的添加、删除等
 * @author huliqing
 */
public interface StateListener {
    
    /**
     * 该方法在状态添加到角色身上时触发,也就是在角色source被成功添加了状态后
     * 调用。
     * @param source 被添加了状态的角色
     * @param stateAdded 添加的状态
     */
    void onStateAdded(Entity source, State stateAdded);
    
    /**
     * 该方法在状态被成功从角色source身上移除时触发,即成功从角色成上移除后
     * 才调用。
     * @param source
     * @param stateRemoved 
     */
    void onStateRemoved(Entity source, State stateRemoved);
}
