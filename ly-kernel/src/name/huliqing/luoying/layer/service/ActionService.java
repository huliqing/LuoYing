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

import name.huliqing.luoying.layer.network.ActionNetwork;
import name.huliqing.luoying.object.action.Action;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 
 * @author huliqing
 */
public interface ActionService extends ActionNetwork{
    
    /**
     * 判断角色当前是否正在执行战斗行为。
     * @param actor
     * @return 
     */
    boolean isPlayingFight(Entity actor);
    
    /**
     * 是否正在执行跑路行为
     * @param actor
     * @return 
     */
    boolean isPlayingRun(Entity actor);
    
    /**
     * 判断目标角色是否正在跟随
     * @param actor
     * @return 
     */
    boolean isPlayingFollow(Entity actor);
    
    /**
     * 获取当前角色正在执行的行为,如果没有任何行为则返回null.
     * @param actor
     * @return 
     */
    Action getPlayingAction(Entity actor);

}
