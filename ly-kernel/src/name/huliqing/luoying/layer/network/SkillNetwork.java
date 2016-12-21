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
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.skill.Skill;

/**
 * @author huliqing
 */
public interface SkillNetwork extends Inject {
    
    /**
     * 执行“步行”技能，步行的速度等受角色属性的影响
     * @param actor
     * @param walkSkill
     * @param dir 目标
     * @param faceToDir
     * @param force 是否强制执行,注：如果强制执行则忽略<strong>所有</strong>
     * 任何限制，直接执行技能即，该方法将保证返回true。
     * @return s
     */
    boolean playWalk(Entity actor, Skill walkSkill, Vector3f dir, boolean faceToDir, boolean force);
    
}
