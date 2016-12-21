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
package name.huliqing.luoying.layer.network;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.action.Action;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public interface ActionNetwork extends Inject  {

     /**
     * 让角色执行某个行为
     * @param actor
     * @param action 
     */
    void playAction(Entity actor, Action action);
    
    /**
     * 让角色执行跑路行为
     * @param actor
     * @param pos 目标位置，不是方向。
     */
    void playRun(Entity actor, Vector3f pos);
    
    /**
     * 让角色执行战斗行为
     * @param actor
     * @param target 战斗目标
     * @param skillId 要执行的技能，可为null.
     */
    void playFight(Entity actor, Entity target, String skillId);
    
}
