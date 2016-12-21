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
import name.huliqing.luoying.object.talent.Talent;

/**
 * 监听角色的天赋点数变化
 * @author huliqing
 */
public interface TalentListener {
    
    /**
     * 角色增加或减少了天赋点数后触发该方法。
     * @param actor
     * @param talent 发生了点数变动的天赋的data.
     * @param pointsAmount 实际增加或减少的天赋点数
     */
    void onTalentPointsChanged(Entity actor, Talent talent, int pointsAmount);
}
