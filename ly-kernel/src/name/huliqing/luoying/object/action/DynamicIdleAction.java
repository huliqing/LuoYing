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

import com.jme3.math.FastMath;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ActionData;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.network.SkillNetwork;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.object.module.SkillModule;
import name.huliqing.luoying.object.skill.Skill;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.luoying.object.entity.DataListener;

/**
 * 适合于生物角色的空闲行为，角色每隔一段时间就会随机执行一个idle动作,在idle动作执行
 * 完毕的间隔期则执行wait循环动作。在此期间角色不会移动位置。
 * @author huliqing
 */
public class DynamicIdleAction extends AbstractAction implements IdleAction, DataListener {
//    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    private SkillModule skillModule;
    
    //  IDLE行为的最大时间间隔,单位秒
    private float intervalMax = 7.0f;
    private float intervalMin = 3.0f;
    
    // ---- 内部参数
    private final List<Skill> idleSkills = new ArrayList<Skill>();
    private float interval = 4.0f;
    private float intervalUsed;
    
    // 缓存技能id
    private Skill waitSkill;
    
    public DynamicIdleAction() {
        super();
    }

    @Override
    public void setData(ActionData data) {
        super.setData(data);
        intervalMax = data.getAsFloat("intervalMax", intervalMax);
        intervalMin = data.getAsFloat("intervalMin", intervalMin);
    }

    @Override
    public void initialize() {
        super.initialize();
        skillModule = actor.getModule(SkillModule.class);
        actor.addDataListener(this);
        if (waitSkill == null) {
            List<Skill> waitSkills = skillModule.getSkillWait(null);
            if (waitSkills != null && !waitSkills.isEmpty()) {
                waitSkill = waitSkills.get(0);
            }
        }
        
        recacheIdleSkills();
    }

    @Override
    public void cleanup() {
        actor.removeDataListener(this);
        super.cleanup();
    }
    
    @Override
    public void doLogic(float tpf) {
        intervalUsed += tpf;
        
        if (intervalUsed < interval) {
            // 在idle动作执行的间隔要执行一个wait动作，使角色看起来不会像是完全静止的。
            if (!skillService.isPlayingSkill(actor)) {
                // 注：wait可能不是循环的，所以需要判断
                if (!skillModule.isWaiting() && waitSkill != null) {
                    
//                    skillNetwork.playSkill(actor, waitSkill, false);
                    entityNetwork.useObjectData(actor, waitSkill.getData().getUniqueId());
                    
                }
            }
            return;
        }
        
        Skill idle = getIdleSkill();
        if (idle == null) {
            return;
        }
        
//        skillNetwork.playSkill(actor, idle, false);
        entityNetwork.useObjectData(actor, idle.getData().getUniqueId());
        
        intervalUsed = 0;
        interval = (intervalMax - intervalMin) * FastMath.nextRandomFloat() + intervalMin;
        interval += idle.getTrueUseTime();
    }
    
    private Skill getIdleSkill() {
        if (idleSkills.isEmpty()) {
            return null;
        }
        return idleSkills.get(FastMath.nextRandomInt(0, idleSkills.size() - 1));
    }

    @Override
    public void onDataAdded(ObjectData data, int amount) {
        if (data instanceof SkillData) {
            recacheIdleSkills();
        }
    }

    @Override
    public void onDataRemoved(ObjectData data, int amount) {
        if (data instanceof SkillData) {
            recacheIdleSkills();
        }
    }

    @Override
    public void onDataUsed(ObjectData data) {
        // ignore
    }

    // 重新缓存技能
    private void recacheIdleSkills() {
        idleSkills.clear();
        skillModule.getSkillIdle(idleSkills);
    }
    
}
