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

import com.jme3.scene.control.Control;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Config;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.message.EntitySkillUseMessage;
import name.huliqing.luoying.message.StateCode;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.attribute.BooleanAttribute;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.attribute.SimpleValueChangeListener;
import name.huliqing.luoying.object.attribute.ValueChangeListener;
import name.huliqing.luoying.object.skill.Skill;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 技能模块，让实体具有“技能”，这个模块主要管理 SkillData这类物体
 * @author huliqing 
 */
public class SkillModule extends AbstractModule implements ValueChangeListener, SimpleValueChangeListener<Number> {
    private static final Logger LOG = Logger.getLogger(SkillModule.class.getName());
    private final SkillService skillService = Factory.get(SkillService.class);
    
    // 被锁定的技能列表，二进制表示，其中每1个二进制位表示一个技能类型。
    // 如果指定的位为1，则表示这个技能类型被锁定，则这类技能将不能执行。
    // 默认值0表示没有任何锁定。
    private long lockedSkillTypes;
    
    // 默认的技能: “空闲”"等待"，“受伤”，“死亡”。。
    private long idleSkillTypes;
    private long waitSkillTypes;
    private long walkSkillTypes;
    private long runSkillTypes;
    private long hurtSkillTypes;
    private long deadSkillTypes;
    
    // 绑定角色的“死亡”属性，当角色在死亡的时候自动执行死亡技能
    private String bindDeadAttribute;
    // 绑定“会受伤”的属性，当角色的这些属性被降低时，角色会自动执行“受伤”技能,这些属性必须是NumberAttribute类型。
    // 一般为“生命值”属性，允许绑定多个不同的属性，格式： "attributeName1,attributeName2"
    private String[] bindHurtAttributes;
    
    // ---- inner
    private final Control updateControl = new AdapterControl() {
        @Override
        public void update(float tpf) {skillUpdate(tpf);}
    };
    
    // 所有可用的技能处理器
    private final List<Skill> skills = new ArrayList<Skill>();
    // 一个map,用于优化获取查找技能的速度，skillMap中的内容与skills中的是一样的。
    private final Map<String, Skill> skillMap = new HashMap<String, Skill>();
    private final SafeArrayList<SkillListener> skillListeners = new SafeArrayList<SkillListener>(SkillListener.class);
    
    // 当前正在执行的技能列表,
    // 如果runningSkills都执行完毕,则清空.但是lastSkill仍保持最近刚刚执行的技能的引用.
    private final SafeArrayList<Skill> playingSkills = new SafeArrayList<Skill>(Skill.class);
    // 当前正在运行的所有技能的类型，其中每一个二进制位表示一个技能类型。
    private long playingSkillTypes;
    // 当前正在执行中的技能中优先级最高的值。
    private int playingPriorMax = Integer.MIN_VALUE;
    
    // 最近一个执行的技能,这个技能可能正在执行，也可能已经停止。
    private Skill lastSkill;

    private BooleanAttribute deadAttribute;
    private NumberAttribute[] hurtAttributes;
    
    @Override
    public void setData(ModuleData data) {
        super.setData(data);
        lockedSkillTypes = data.getAsLong("lockedSkillTypes", 0);
        idleSkillTypes = skillService.convertSkillTypes(data.getAsArray("idleSkillTypes"));
        waitSkillTypes = skillService.convertSkillTypes(data.getAsArray("waitSkillTypes"));
        walkSkillTypes = skillService.convertSkillTypes(data.getAsArray("walkSkillTypes"));
        runSkillTypes = skillService.convertSkillTypes(data.getAsArray("runSkillTypes"));
        hurtSkillTypes = skillService.convertSkillTypes(data.getAsArray("hurtSkillTypes"));
        deadSkillTypes = skillService.convertSkillTypes(data.getAsArray("deadSkillTypes"));
        bindDeadAttribute = data.getAsString("bindDeadAttribute");
        bindHurtAttributes = data.getAsArray("bindHurtAttributes");
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("lockedSkillTypes", lockedSkillTypes);
        for (Skill s : playingSkills.getArray()) {
            s.updateDatas();
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        // 技能的更新支持
        entity.getSpatial().addControl(updateControl);
        
        // 载入技能
        List<SkillData> skillDatas = entity.getData().getObjectDatas(SkillData.class, new ArrayList<SkillData>());
        if (skillDatas != null && !skillDatas.isEmpty()) {
            for (SkillData sd : skillDatas) {
                addSkillInner((Skill) Loader.load(sd));
            }
        }
        
        // 监听角色是否死亡或复活
        if (bindDeadAttribute != null) {
            deadAttribute = entity.getAttribute(bindDeadAttribute, BooleanAttribute.class);
            if (deadAttribute != null) {
                deadAttribute.addListener(this);
            }
        }
        
        if (bindHurtAttributes != null) {
            List<NumberAttribute> temp = new ArrayList<NumberAttribute>(bindHurtAttributes.length);
            for (String attr : bindHurtAttributes) {
                NumberAttribute hurtAttr = getAttribute(attr, NumberAttribute.class);
                if (hurtAttr != null) {
                    temp.add(hurtAttr);
                }
            }
            hurtAttributes = temp.toArray(new NumberAttribute[0]);
            for (NumberAttribute hurtAttr : hurtAttributes) {
                hurtAttr.addSimpleValueChangeListener(this);
            }
        }
    }
    
    private void skillUpdate(float tpf) {
        if (!isEnabled())
            return;
        
        // 更新并尝试移除已经结束的技能
        if (!playingSkills.isEmpty()) {

            int oldSize = playingSkills.size();
            for (Skill skill : playingSkills.getArray()) {
                if (skill.isEnd()) {
                    playingSkills.remove(skill);
                    skill.cleanup();
                    // 执行侦听器
                    if (!skillListeners.isEmpty()) {
                        for (SkillListener sl : skillListeners.getArray()) {
                            sl.onSkillEnd(skill);
                        }
                    }
                } else {
                    skill.update(tpf);
                }
            }
            
            // 如果有技能执行完了，并被移除了.
            // 1.重新缓存runningSkillTypes
            // 2.重新缓存技能中最高优先级的值。
            // 3.修复、重启部分被覆盖的动画通道的动画，比如在走动时取武器后双手应该重新回到走动时的协调运动。
            if (playingSkills.size() != oldSize) {
                playingSkillTypes = 0;
                playingPriorMax = Integer.MIN_VALUE;
                for (Skill playSkill : playingSkills.getArray()) {
                    playingSkillTypes |= playSkill.getTypes();
                    if (playSkill.getPrior() > playingPriorMax) {
                        playingPriorMax = playSkill.getPrior();
                    }
                    playSkill.restoreAnimation();
                }
            }
        }
    }
    
    @Override
    public void cleanup() {
        for (Skill skill : playingSkills.getArray()) {
            if (!skill.isEnd()) {
                skill.cleanup();
            }
        }
        playingSkills.clear();
        skills.clear();
        skillMap.clear();
        playingSkillTypes = 0;
        entity.getSpatial().removeControl(updateControl);
        if (deadAttribute != null) {
            deadAttribute.removeListener(this);
        }
        super.cleanup();
    }

    @Override
    public void onValueChanged(Attribute attribute) {
        if (attribute == deadAttribute) {
            Skill playSkill;
            if (deadAttribute.getValue()) {
                playSkill = getSkillByTypes(deadSkillTypes);
            } else {
                playSkill = getSkillByTypes(waitSkillTypes);
            }
            if (playSkill != null) {
                playSkill(playSkill, true);
            }
            return;
        }
    }

    @Override
    public void onSimpleValueChanged(Attribute attribute, Number oldValue) {
        if (hurtAttributes == null || hurtAttributes.length <= 0) 
            return;
        for (NumberAttribute na : hurtAttributes) {
            if (attribute == na) {
                if (na.floatValue() < oldValue.floatValue()) {
                    Skill hurtSkill = getSkillByTypes(hurtSkillTypes);
                    if (Config.debug) {
                        LOG.log(Level.INFO, "Entity hurt, entity={0}, attributeName={1}, oldValue={2}, newValue={3}", 
                                new Object[] {entity.getData().getId(), na.getName(), oldValue, na.getValue()});
                    }
                    if (hurtSkill != null) {
                        playSkill(hurtSkill, false);
                    }
                    return;
                }
            }
        }
    }
    
    /**
     * 检查技能在当前状态下是否可以执行，如果返回值为 {@link SkillConstants#STATE_OK} 则表示可以执行，
     * 否则不能执行。
     * @param skill
     * @return 
     */
    public int checkStateCode(Skill skill) {
        if (skill == null) {
            return StateCode.SKILL_USE_FAILURE_NOT_FOUND;
        }
        
        // 角色自己已经死亡。
        if (deadAttribute != null && deadAttribute.getValue()) {
            return StateCode.SKILL_USE_FAILURE_ACTOR_DEAD;
        }

        if (skill.getActor() == null) {
            skill.setActor(entity);
        }
        
        // 如果技能被锁定中，则不能执行
        if (isLockedSkillTypes(skill.getTypes())) {
            return StateCode.SKILL_USE_FAILURE_LOCKED;
        }
        
        // 如果新技能自身判断不能执行，例如加血技能或许就不可能给敌军执行。
        // 有很多特殊技能是不能对一些特定目标执行的，所以这里需要包含技能自身的判断
        int stateCode = skill.checkState();
        if (stateCode != StateCode.SKILL_USE_OK) {
            return stateCode;
        }
        
        // 通过钩子来判断是否可以执行, 如果有一个钩子返回不允许执行则后面不再需要判断。
        if (skillListeners != null && !skillListeners.isEmpty()) {
            for (SkillListener sl : skillListeners) {
                if (!sl.onSkillHookCheck(skill)) {
                    return StateCode.SKILL_USE_FAILURE_BY_HOOK;
                }
            }
        }
        
        // 技能优先级较高可以直接运行
        if (skill.getPrior() > playingPriorMax) {
            return StateCode.SKILL_USE_OK;
        }
        
        // 判断正在执行中的所有技能，如果“正在执行”中的所有技能都可以被覆盖或打断后执行，
        // 则不需要再判断技能优先级如果其中有任何一个即不能被覆盖，并且也不能被打断，
        // 则需要判断技能优先级
        boolean allCanOverlapOrInterrupt = true;
        long overlaps = skill.getOverlapTypes();
        long interrupts = skill.getInterruptTypes();
        for (Skill runSkill : playingSkills.getArray()) {
            if ((overlaps & runSkill.getTypes()) == 0 && (interrupts & runSkill.getTypes()) == 0) {
                allCanOverlapOrInterrupt = false;
                break;
            }
        }
        if (allCanOverlapOrInterrupt) {
            return StateCode.SKILL_USE_OK;
        }
        
        return StateCode.SKILL_USE_FAILURE_CAN_NOT_INTERRUPT;
    }
    
    /**
     * 执行技能，如果成功执行则返回true,否则返回false, <br>
     * 在执行技能之前可以通过 {@link #checkStateCode(Skill) }来查询当前状态下技能是否可以执行。<br>
     * 如果需要强制执行技能，则可以将参数force设置为true,这可以保证技能始终执行。
     * @param newSkill
     * @param force
     * @return 
     */
    public boolean playSkill(Skill newSkill, boolean force) {
        // 强制执行则不需要检查状态
        if (force) {
            playSkillInner(newSkill);
            return true;
        }
        
        // 如果状态不允许
        int stateCode = checkStateCode(newSkill);
        if (stateCode != StateCode.SKILL_USE_OK) {
            if (isMessageEnabled()) {
                String message = entity.getData().getId() + " use skill " + newSkill.getData().getId();
                addEntityMessage(new EntitySkillUseMessage(stateCode, message, entity, newSkill.getData()));
            }
            return false;
        }
        
        // 可以执行
        playSkillInner(newSkill);
        return true;
    }
    
    /**
     * 执行一个技能,这个方法是强制执行的，如果需要判断技能是否可以合理执行,可以调用 
     * {@link #checkStateCode(Skill, boolean) }来进行判断。
     * @param newSkill 
     */
    private void playSkillInner(Skill newSkill) {
        // 1.如果当前没有任何正在执行的技能则直接执行技能
        if (playingSkills.isEmpty()) {
            startNewSkill(newSkill);
            return;
        }
        
        // 这个方法是强制执行的
        long overlaps = newSkill.getOverlapTypes();
        long interrupts = newSkill.getInterruptTypes();
        for (Skill playSkill : playingSkills.getArray()) {
            // 1.由newSkill指定的要覆盖的，则优先使用覆盖，即不要中断。
            if ((overlaps & playSkill.getTypes()) != 0) {
                continue;
            }
            // 2.由newSkill指定的要强制中断的，一定要中断
            if ((interrupts & playSkill.getTypes()) != 0) {
                playSkill.cleanup();
                continue;
            }
            // 除了"要求强制中断(上面)"的技能之外，交由playSkill当前正在执行的技能进行判断，如果当前的技能状态不允许中断，
            // 则这些技能不应该中断
            if (!playSkill.canInterruptBySkill(newSkill)) {
                continue;
            }
            // 其余一切中断
            playSkill.cleanup();
        }
        startNewSkill(newSkill);
    }
    
    private void startNewSkill(Skill newSkill) {
        // 执行技能
        lastSkill = newSkill;
        if (lastSkill.getActor() == null) {
            lastSkill.setActor(entity);
        }
        lastSkill.initialize();
        // 记录当前正在运行的所有技能类型
        if (!playingSkills.contains(lastSkill)) {
            playingSkills.add(lastSkill);
            playingSkillTypes |= lastSkill.getTypes();
            // 更新当前playing中所有技能的最高优先级的值。
            if (newSkill.getPrior() > playingPriorMax) {
                playingPriorMax = newSkill.getPrior();
            }
        }
        
        // 执行侦听器
        if (!skillListeners.isEmpty()) {
            for (SkillListener sl : skillListeners.getArray()) {
                sl.onSkillStart(lastSkill);
            }
        }
        
        // 消息通知
        if (isMessageEnabled()) {
            String message = entity.getData().getId() + " use skill " + lastSkill.getData().getId();
            addEntityMessage(new EntitySkillUseMessage(StateCode.SKILL_USE_OK, message, entity, lastSkill.getData()));
        }
    }
    
    /**
     * 从角色身上获取一个技能，如果角色没有指定ID的技能则返回null.
     * @param skillId
     * @return 
     */
    public Skill getSkill(String skillId) {
        return skillMap.get(skillId);
    }
    
    /**
     * 获取角色当前所有的技能，注意：返回的技能列表只能作为<b>只读</b>使用.
     * key=skillId, value=Skill
     * @return 
     */
    public List<Skill> getSkills() {
        return Collections.unmodifiableList(skills);
    }
    
    /**
     * 找出指定的类型的技能，第一个找到的将被直接返回。
     * @param skillTypes
     * @return 
     */
    private Skill getSkillByTypes(long skillTypes) {
        for (Skill s : skills) {
            if ((s.getTypes() & skillTypes) != 0) {
                return s;
            }
        }
        return null;
    }
    
    /**
     * 查找拥有指定types的所有技能。
     * @param skillTypes
     * @param store 存放结果
     * @return 
     */
    public List<Skill> getSkillByTypes(long skillTypes, List<Skill> store) {
        if (store == null) {
            store = new ArrayList<Skill>();
        }
        if (skills == null) {
            return store;
        }
        for (Skill s : skills) {
            if ((s.getTypes() & skillTypes) != 0) {
                store.add(s);
            }
        }
        return store;
    }
    
    /**
     * 获取角色的“空闲”技能
     * @param store
     * @return 
     */
    public List<Skill> getSkillIdle(List<Skill> store) {
        return getSkillByTypes(idleSkillTypes, store);
    }
    
    /**
     * 获取“等待”的技能
     * @param store
     * @return 
     */
    public List<Skill> getSkillWait(List<Skill> store) {
        return getSkillByTypes(waitSkillTypes, store);
    }
    
    /**
     * 获取“步行”技能
     * @param store
     * @return 
     */
    public List<Skill> getSkillWalk(List<Skill> store) {
        return getSkillByTypes(walkSkillTypes, store);
    }
    
    /**
     * 获取“跑步”技能
     * @param store
     * @return 
     */
    public List<Skill> getSkillRun(List<Skill> store) {
        return getSkillByTypes(runSkillTypes, store);
    }
    
    /**
     * 获取“受伤”的技能，
     * @param store
     * @return 
     */
    public List<Skill> getSkillHurt(List<Skill> store) {
        return getSkillByTypes(hurtSkillTypes, store);
    }
    
    /**
     * 获取“死亡”的技能
     * @param store
     * @return 
     */
    public List<Skill> getSkillDead(List<Skill> store) {
        return getSkillByTypes(deadSkillTypes, store);
    }
    
    /**
     * 添加技能"执行“侦听器
     * @param skillListener 
     */
    public void addListener(SkillListener skillListener) {
        if (!skillListeners.contains(skillListener)) {
            skillListeners.add(skillListener);
        }
    }
    
    /**
     * 移除技能"执行"侦听器
     * @param skillListener
     * @return 
     */
    public boolean removeListener(SkillListener skillListener) {
        return skillListeners.remove(skillListener);
    }
    
    /**
     * 获取最近一个执行的技能，如果没有执行过任何技能则返回null.<br>
     * 注：返回的技能有可能正在执行，也有可能已经结束。
     * @return 
     */
    public Skill getLastSkill() {
        return lastSkill;
    }
    
    /**
     * 获取当前正在执行的所有技能。
     * @return 
     */
    public List<Skill> getPlayingSkills() {
        return playingSkills;
    }
    
    /**
     * 获取当前正在执行的技能类型(type)，返回值中每个二进制位表示一个技能(类型),<br>
     * 如果没有正在执行的任何技能则该值会返回 0. <br>
     * @return 
     */
    public long getPlayingSkillTypes() {
        return playingSkillTypes;
    }

    /**
     * 获取技能的锁定状态，返回的整数中每一个二进制位表示一个被锁定的技能类型
     * @return 
     */
    public long getLockedSkillTypes() {
        return lockedSkillTypes;
    }
    
    /**
     * 判断技能类型是否被锁定, 如果skillTypes中存在<b>任何一个</b>类型被锁定则该方法将返回true.
     * @param skillTypes
     * @return 
     */
    public boolean isLockedSkillTypes(long skillTypes) {
        return (lockedSkillTypes & skillTypes) != 0;
    }

    /**
     * 锁定指定技能类型的技能, 被锁定后的技能将不能执行。
     * @param skillTypes 
     * @see #getLockedSkillTypes() 
     */
    public void lockSkillTypes(long skillTypes) {
        this.lockedSkillTypes |= skillTypes;
        updateDatas();
    }
    
    /**
     * 解锁指定技能类型的技能。 
     * @param skillTypes 
     */
    public void unlockSkillTypes(long skillTypes) {
        lockedSkillTypes &= ~skillTypes;
        updateDatas();
    }
        
    /**
     * 判断skillTypes类型的技能是否正在执行。如果skillTypes类型的技能中有<b>任何一个</b>正在执行则该方法返回true.
     * @param skillTypes
     * @return 
     */
    public boolean isPlayingSkill(long skillTypes) {
        return (playingSkillTypes & skillTypes) != 0;
    }
    
    /**
     * 判断角色是否处于等待状态
     * @return 
     */
    public boolean isWaiting() {
        return lastSkill == null
                || (lastSkill.getTypes() & waitSkillTypes) != 0
                || (playingSkillTypes & waitSkillTypes) != 0;
    }
    
    /**
     * 判断角色是否处于“步行 ”状态
     * @return 
     */
    public boolean isWalking() {
        return (playingSkillTypes & walkSkillTypes) != 0;
    }
    
    /**
     * 判断角色是否处于“跑步”状态
     * @return 
     */
    public boolean isRunning() {
        return (playingSkillTypes & runSkillTypes) != 0;
    }

    @Override
    public boolean handleDataAdd(ObjectData hData, int amount) {
        if (!(hData instanceof SkillData)) 
            return false;
            
        // 技能不能重复
        if (getSkill(hData.getId()) != null) {
            addEntityDataAddMessage(StateCode.DATA_ADD_FAILURE_DATA_EXISTS, hData, amount);
            return false; 
        }
        addSkillInner((Skill) Loader.load(hData));
        addEntityDataAddMessage(StateCode.DATA_ADD, hData, 1);
        return true;
    }
    
    @Override
    public boolean handleDataRemove(ObjectData hData, int amount) {
        if (!(hData instanceof SkillData)) 
            return false;
            
        Skill skill = getSkill(hData.getId());
        if (skill == null || skill.getData() != hData) {
            addEntityDataRemoveMessage(StateCode.DATA_REMOVE_FAILURE_NOT_FOUND, hData, amount);
            return false;
        }
        skills.remove(skill);
        skillMap.remove(skill.getData().getId());
        entity.getData().removeObjectData(skill.getData());
        skill.cleanup();
        addEntityDataRemoveMessage(StateCode.DATA_REMOVE, skill.getData(), 1);
        return true;
    }
    
    @Override
    public boolean handleDataUse(ObjectData hData) {
        if (!(hData instanceof SkillData)) 
            return false;
            
        Skill skill = getSkill(hData.getId());
        if (skill == null || skill.getData() != hData) {
            skill = Loader.load(hData);
        }
        return playSkill(skill, false);
    }
    
    /**
     * 添加一个新技能给角色,如果相同ID的技能已经存在，则该方法什么也不会处理。
     * @param skill 
     * @return  true如果成功添加
     */
    private boolean addSkillInner(Skill skill) {
        if (skills.contains(skill)) {
            return false;
        }
        
        skill.setActor(entity);
        skills.add(skill);
        skillMap.put(skill.getData().getId(), skill);
        entity.getData().addObjectData(skill.getData());
        addEntityDataAddMessage(StateCode.DATA_ADD, skill.getData(), 1);
        return true;
    }
    
}
