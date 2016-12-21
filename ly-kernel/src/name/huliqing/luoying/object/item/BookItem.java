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

import name.huliqing.luoying.message.StateCode;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 技能书, 使用后消耗技能书,并获得一个指定的技能
 * @author huliqing
 */
public class BookItem extends AbstractItem {
    // 要学习的技能ID
    private String skill;
    
    @Override
    public void setData(ItemData data) {
        super.setData(data);
        skill = data.getAsString("skill");
        if (skill == null) {
            throw new NullPointerException("Skill could not be null, itemId=" + data.getId());
        }
    }
    
    @Override
    public int checkStateCode(Entity actor) {
        if (actor.getData().getObjectData(skill) != null) {
            return StateCode.DATA_USE_FAILURE;
        }
        return super.checkStateCode(actor); 
    }
    
    @Override
    protected void doUse(Entity actor) {
        // 学习技能
        SkillData skillData = Loader.loadData(skill);
        actor.addObjectData(skillData, 1);
        actor.removeObjectData(data, 1);
    }
    
}
