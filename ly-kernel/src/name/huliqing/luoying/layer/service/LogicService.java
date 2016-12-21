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

import name.huliqing.luoying.Inject;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.logic.Logic;

/**
 *
 * @author huliqing
 */
public interface LogicService extends Inject {
    
    /**
     * 给角色添加一个逻辑
     * @param actor
     * @param logicId
     */
    void addLogic(Entity actor, String logicId);
    
    /**
     * 给角色添加一个逻辑
     * @param actor
     * @param logicData 
     */
    void addLogic(Entity actor, LogicData logicData);
        
    /**
     * 给角色添加逻辑
     * @param actor
     * @param logic 
     */
    void addLogic(Entity actor, Logic logic);
    
    /**
     * 移除指定的逻辑，如果移除成功则返回true.
     * @param actor
     * @param logic
     * @return 
     */
    boolean removeLogic(Entity actor, Logic logic);
    
    /**
     * 清除角色身上的所有逻辑
     * @param actor 
     */
    void clearLogics(Entity actor);
    
}
