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

import name.huliqing.luoying.layer.network.TalentNetwork;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.TalentListener;

/**
 *
 * @author huliqing
 */
public interface TalentService extends TalentNetwork {
    
    /**
     * 获取角色当前可用的天赋点数
     * @param actor
     * @return 
     */
    int getTalentPoints(Entity actor);
    
    /**
     * 添加天赋侦听器
     * @param actor
     * @param talentListener 
     */
    void addTalentListener(Entity actor, TalentListener talentListener);
    
    /**
     * 移除天赋侦听器
     * @param actor
     * @param talentListener 
     */
    void removeTalentListener(Entity actor, TalentListener talentListener);
}
