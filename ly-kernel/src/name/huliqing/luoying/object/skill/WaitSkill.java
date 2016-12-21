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
package name.huliqing.luoying.object.skill;

import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ChannelModule;

/**
 * 让角色“等待”的技能
 * @author huliqing
 */
public class WaitSkill extends AbstractSkill {
    private ChannelModule channelModule;

    @Override
    public void setActor(Entity actor) {
        super.setActor(actor); 
        channelModule = actor.getModuleManager().getModule(ChannelModule.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        
        // 对于一些没有“Wait动画”的角色必须想办法让它静止下来
        if (animation == null) {
            channelModule.reset();
        }
    }
    
    @Override
    protected void doSkillUpdate(float tpf) {}

    
}
