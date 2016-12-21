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
package name.huliqing.luoying.object.action;

import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.skill.Skill;

/**
 * 角色的战斗PK行为
 * @author huliqing
 */
public interface FightAction extends Action {
    
    /**
     * 设置使用的技能, 注：该技能必须满足以下条件：
     * 1.与当前角色的武器类型匹配。
     * 2.必须是攻击技能,可为普通攻击，技能攻击，魔法攻击
     * @param skill 
     */
    public void setSkill(Skill skill);
    
    /**
     * 设置目标敌人
     * @param actor 
     */
    public void setEnemy(Entity actor);
    
    
    
}
