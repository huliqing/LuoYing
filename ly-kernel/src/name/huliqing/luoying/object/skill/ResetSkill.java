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

import com.jme3.math.FastMath;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ChannelModule;

/**
 * 这个技能可以允许使用角色动画中的任何一侦作为角色的reset状态．当某些角色
 * 没有可用的reset动画时可以使用这个技能来代替．
 * @author huliqing
 */
public class ResetSkill extends SimpleAnimationSkill {
    private ChannelModule channelModule;
    
    // 指定要把角色动画定格在animation动画的哪一帧上， 这是一个插值点。
    private float timePoint;

    @Override
    public void setData(SkillData data) {
        super.setData(data); 
        timePoint = FastMath.clamp(data.getAsFloat("timePoint", timePoint), 0, 1.0f);
    }

    @Override
    public void setActor(Entity actor) {
        super.setActor(actor); 
        channelModule = actor.getModule(ChannelModule.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        
        // 如果没有设置动画名称，则把动画定格在角色的当前帧上
        if (animation == null) {
            channelModule.reset();
        }
    }

    @Override
    protected void doUpdateAnimation(String animation, boolean loop, float animFullTime, float animStartTime) {
        // 直接reset到指定动画的指定时间帧
        channelModule.resetToAnimationTime(animation, timePoint);
    }
    
    @Override
    protected void doSkillUpdate(float tpf) {}
    
}
