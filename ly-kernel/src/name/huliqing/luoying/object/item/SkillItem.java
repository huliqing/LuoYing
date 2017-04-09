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
package name.huliqing.luoying.object.item;

import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.SkillModule;
import name.huliqing.luoying.object.skill.Skill;

/**
 * 消耗物品来使用一个技能，示例：如使用复活卷轴,在使用这个物品的时候会调用一
 * 个技能来让角色执行。
 * @author huliqing
 */
public class SkillItem extends AbstractItem {
    private String skillId;
    
    @Override
    public void setData(ItemData data) {
        super.setData(data); 
        skillId = data.getAsString("skill");
    }
    
    @Override
    protected void doUse(Entity actor) {
        Skill skill = Loader.load(skillId);
        if (skill != null) {
            SkillModule skillModule = actor.getModule(SkillModule.class);
            if (skillModule != null) {
                skill.setActor(actor);
                if (skillModule.playSkill(skill, false)) {
                    actor.removeObjectData(data, 1);
                }
            }
        }
    }
    
    
}
