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

import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.network.SkillNetwork;
import name.huliqing.luoying.object.module.SkillModule;
import name.huliqing.luoying.object.skill.Skill;

/**
 * 静态的IDLE行为，对于不能动，静止的所有物体的行为。
 * @author huliqing
 */
public class StaticIdleAction extends AbstractAction implements IdleAction {
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    private SkillModule skillModule;
    
    // 缓存技能id
    private Skill waitSkill;
    
    @Override
    public void initialize() {
        super.initialize();
        skillModule = actor.getModule(SkillModule.class);
        
        List<Skill> waitSkills = skillModule.getSkillWait(null);
        if (waitSkills != null && !waitSkills.isEmpty()) {
            waitSkill = waitSkills.get(0);
        }
    }
    
    @Override
    protected void doLogic(float tpf) {
        if (!skillModule.isWaiting()) {
            if (waitSkill != null) {
                
//                skillNetwork.playSkill(actor, waitSkill, false);
                entityNetwork.useObjectData(actor, waitSkill.getData().getUniqueId());
                
            }
            end();
        }
    }
    
}
