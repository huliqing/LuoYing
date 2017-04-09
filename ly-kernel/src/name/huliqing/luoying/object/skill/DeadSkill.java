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

import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ChannelModule;

/**
 * 执行死亡技能,简单的死亡效果
 * @author huliqing
 */
public class DeadSkill extends SimpleAnimationSkill {
    private ChannelModule channelModule;
    
    // 是否死亡后立即移出场景
    private boolean remove;
    
    @Override
    public void setData(SkillData data) {
        super.setData(data); 
        remove = data.getAsBoolean("remove", remove);
    }

    @Override
    public void setActor(Entity actor) {
        super.setActor(actor); 
        channelModule = actor.getModule(ChannelModule.class);
    }

    @Override
    public void initialize() {
        super.initialize(); 

        // Reset，对于没有“死亡”动画的角色，在死亡时必须让它“静止”
        // 让角色的其它动画停止播放，以防止角色在死亡之后仍然在做其它动作的奇怪现象。
        if (animation == null) {
            channelModule.reset();
        }
    }
    
    @Override
    protected void doSkillUpdate(float tpf) {
        // ignore
    }

    @Override
    protected void doSkillEnd() {
        super.doSkillEnd();
        // 不要放在cleanup中移除角色,因为这可能会在场景清理(场景cleanup)的时候冲突，
        // 可能造成无限递归异常(StackOverflow)
        if (remove) {
            actor.removeFromScene();
        }
    }
    
}
