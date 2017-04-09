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
package name.huliqing.luoying.object.state;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.service.DefineService;
import name.huliqing.luoying.object.module.ActorModule;
import name.huliqing.luoying.object.module.ChannelModule;
import name.huliqing.luoying.object.module.SkillModule;
import name.huliqing.luoying.object.skill.Skill;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.luoying.object.module.SkillListener;

/**
 * @author huliqing
 */
public class SkillLockedState extends AbstractState implements SkillListener {
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    private final DefineService defineService = Factory.get(DefineService.class);
    private ActorModule actorModule;
    private SkillModule skillModule;
    private ChannelModule channelModule;
    
    // 把角色锁定在特定类型的技能上
    private long lockAtSkillTypes;
    // 把角色锁定在当前动画帧上。
    private boolean lockAtFrame;
    
    // 是否锁定全部技能，如果为true,则忽略lockSkills.
    private boolean lockAllSkillTypes;
    // 要锁定的技能tags
    private long lockSkillTypes;
    // 要锁定的特定的技能列表.
    private List<String> lockSkillIds;
    // 要锁定的技能通道
    private String[] lockChannels;
    // 这个选项可以防止角色被锁定后仍然可被其它角色“推动”的bug.(由于物理特性的原因)
    private boolean lockLocation;
    
    // ---- inner
    private final long SKILL_ALL = 0xFFFFFFFF;
    // 被锁定的位置
    private Vector3f lockedLocation;
    
    @Override
    public void setData(StateData data) {
        super.setData(data); 
        lockAtSkillTypes = defineService.getSkillTypeDefine().convert(data.getAsArray("lockAtSkillTypes"));
        lockAtFrame = data.getAsBoolean("lockAtFrame", false);
        lockAllSkillTypes = data.getAsBoolean("lockAllSkillTypes", false);
        lockSkillTypes = defineService.getSkillTypeDefine().convert(data.getAsArray("lockSkillTypes"));
        lockSkillIds = data.getAsStringList("lockSkillIds");
        lockChannels = data.getAsArray("lockChannels");
        lockLocation = data.getAsBoolean("lockLocation", false);
    }

    @Override
    public void initialize() {
        super.initialize();
        actorModule = entity.getModule(ActorModule.class);
        channelModule = entity.getModule(ChannelModule.class);
        skillModule = entity.getModule(SkillModule.class);
        
        // 锁定在特定标记的技能上
        if (lockAtSkillTypes > 0) {
            List<Skill> lockedSkills = skillModule.getSkillByTypes(lockAtSkillTypes, null);
            if (lockedSkills != null && !lockedSkills.isEmpty()) {
                Skill skill = lockedSkills.get(FastMath.nextRandomInt(0, lockedSkills.size() - 1));
                entityNetwork.useObjectData(entity, skill.getData().getUniqueId());
            }
        }
        
        // 锁定在当前动画侦上
        if (lockAtFrame) {
            channelModule.setSpeed(0);
            actorModule.setWalkDirection(Vector3f.ZERO);
        }
        
        // 锁定所有技能或特定技能。
        if (lockAllSkillTypes) {
            skillModule.lockSkillTypes(SKILL_ALL);
        } else {
            // 锁定特殊技能类型
            skillModule.lockSkillTypes(lockSkillTypes);
            // 对于特定的技能ID需要通过技能侦听器来监听,在状态消失时要移除侦听器
            if (lockSkillIds != null) {
                skillModule.addListener(this);
            }
        }
        
        // 锁定动画通道
        if (lockChannels != null && channelModule != null) {
            channelModule.setChannelLock(true, lockChannels);
        }
        
        // 如果锁定位置，则记住初始位置
        if (lockLocation) {
            lockedLocation = new Vector3f();
            lockedLocation.set(actorModule.getLocation());
        }
        
        // 削弱时间
        useTime = useTime - useTime * data.getResist();
        
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        // 锁定位置，防止位置移动
        if (lockLocation) {
            if (moved(lockedLocation, actorModule.getLocation())) {
                actorModule.setLocation(lockedLocation);
            }
        }
    }
    
    private boolean moved(Vector3f loc1, Vector3f loc2) {
        return MathUtils.abs(loc1.x - loc2.x) > 0.0001f 
                ||  MathUtils.abs(loc1.y - loc2.y) > 0.0001f
                ||  MathUtils.abs(loc1.z - loc2.z) > 0.0001f;
    }

    @Override
    public boolean onSkillHookCheck(Skill skill) {
        // 如果要执行的技能刚才是被锁定的特定技能，则返回false,表示不能执行。
        return !(lockSkillIds != null && lockSkillIds.contains(skill.getData().getId()));
    }

    @Override
    public void onSkillStart(Skill skill) {}

    @Override
    public void onSkillEnd(Skill skill) {}

    @Override
    public void cleanup() {
        if (lockAllSkillTypes) {
            skillModule.unlockSkillTypes(SKILL_ALL);
        } else {
            skillModule.unlockSkillTypes(lockSkillTypes);
            skillModule.removeListener(this);
        }
        if (lockChannels != null && channelModule != null) {
            channelModule.setChannelLock(false, lockChannels);
        }
        // 如果状态结束，目标已经死亡，而角色最后一个技能执行的不是”死亡“技能，则强制执行死亡技能。
        // 这可以避免一些情况如：当角色在跑步时被”冰冻“状态冻住而死但却没有执行死亡技能（死亡技能被锁定），
        // 在状态结束后角色仍然在一直移动的情况发生。
        if (deadAttribute != null && deadAttribute.getValue()) {
            Skill lastSkill = skillModule.getLastSkill();
            List<Skill> deadSkills = skillModule.getSkillDead(null);
            if (deadSkills != null && !deadSkills.isEmpty() && !deadSkills.contains(lastSkill)) {
                skillModule.playSkill(deadSkills.get(0), false);
            }
        }
        super.cleanup();
    }
    
}
