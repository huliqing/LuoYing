/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import com.jme3.scene.control.Control;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.skill.Skill;

/**
 * @author huliqing
 */
public class SkillModule extends AbstractModule {
    private Actor actor;
    
    // 所有可用的技能处理器
    private final List<Skill> skills = new ArrayList<Skill>();
    // 一个map,用于优化获取查找技能的速度，skillMap中的内容与skills中的是一样的。
    private final Map<String, Skill> skillMap = new HashMap<String, Skill>();
    
    // 当前正在执行的技能列表,
    // 如果runningSkills都执行完毕,则清空.但是lastSkill仍保持最近刚刚执行的技能的引用.
    private final SafeArrayList<Skill> runningSkills = new SafeArrayList<Skill>(Skill.class);
    // 当前正在运行的所有技能的类型，其中每一个二进制位表示一个技能类型。
    private long runningSkillStates;
    
    // 最近一个执行的技能,这个技能可能正在执行，也可能已经停止。
    private Skill lastSkill;
    
    // 临听角色技能的执行和结束 
    private List<SkillListener> skillListeners;
    private List<SkillPlayListener> skillPlayListeners;
    
    // 用于支持技能更新逻辑的control.
    private Control updateControl;

    @Override
    public void initialize(Actor actor) {
        super.initialize(actor);
        this.actor = actor;
        
        // 载入技能
        List<SkillData> skillDatas = actor.getData().getObjectDatas(SkillData.class, null);
        if (skillDatas != null && !skillDatas.isEmpty()) {
            for (SkillData sd : skillDatas) {
                addSkill((Skill) Loader.load(sd));
            }
        }
        
        // 技能的更新支持
        updateControl = new AdapterControl() {
            @Override
            public void update(float tpf) {skillUpdate(tpf);}
        };
        this.actor.getSpatial().addControl(updateControl);
    }
    
    private void skillUpdate(float tpf) {
        // 更新并尝试移除已经结束的技能
        if (!runningSkills.isEmpty()) {

            int oldSize = runningSkills.size();
            for (Skill skill : runningSkills.getArray()) {
                if (skill.isEnd()) {
                    runningSkills.remove(skill);
                    // 执行侦听器
                    if (skillPlayListeners != null && !skillPlayListeners.isEmpty()) {
                        for (int i = 0; i < skillPlayListeners.size(); i++) {
                            skillPlayListeners.get(i).onSkillEnd(actor, skill);
                        }
                    }
                } else {
                    skill.update(tpf);
                }
            }
            
            // 如果有技能执行完了，并被移除了.
            // 1.重新缓存runningSkillTypes
            // 2.修复、重启部分被覆盖的动画通道的动画，比如在走动时取武器后双手应该重新回到走动时的协调运动。
            if (runningSkills.size() != oldSize) {
                runningSkillStates = 0;
                for (Skill skill : runningSkills.getArray()) {
                    runningSkillStates |= 1 << skill.getSkillType().getValue();
                    skill.restoreAnimation();
                }
            }
            
//            if (Config.debug) {
//                Log.get(getClass()).log(Level.INFO, "skillProcessor runningSkills.size={0}", runningSkills.size());
//            }
        }
    }
    
    @Override
    public void cleanup() {
        for (Skill skill : runningSkills.getArray()) {
            if (!skill.isEnd()) {
                skill.cleanup();
            }
        }
        runningSkills.clear();
        runningSkillStates = 0;
        skills.clear();
        skillMap.clear();
        if (updateControl != null) {
            actor.getSpatial().removeControl(updateControl);
        }
        super.cleanup();
    }
    
    /**
     * 添加一个新技能给角色,如果相同ID的技能已经存在，则该方法什么也不会处理。
     * @param skill 
     */
    public void addSkill(Skill skill) {
        if (skills.contains(skill))
            return;
        
        skill.setActor(actor);
        skill.setSkillControl(this);
        skills.add(skill);
        skillMap.put(skill.getData().getId(), skill);
        actor.getData().addObjectData(skill.getData());
        
        // 通知侦听器
        if (skillListeners != null) {
            for (int i = 0; i < skillListeners.size(); i++) {
                skillListeners.get(i).onSkillAdded(actor, skill);
            }
        }
    }
    
    /**
     * 从角色身上移除一个技能，注：被移除的技能必须是已经存在于角色身上的技能实例，
     * 否则该方法什么也不做，并返回false.<BR>
     * 使用{@link #getSkill(java.lang.String) } 来确保从当前角色身上获得一个存在的技能实例。
     * @param skill
     * @return 
     */
    public boolean removeSkill(Skill skill) {
        if (!skills.contains(skill)) 
            return false;
        
        skills.remove(skill);
        skillMap.remove(skill.getData().getId());
        actor.getData().removeObjectData(skill.getData());
        skill.cleanup();
        
        // 通知侦听器
        if (skillListeners != null) {
            for (int i = 0; i < skillListeners.size(); i++) {
                skillListeners.get(i).onSkillRemoved(actor, skill);
            }
        }
        return true;
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
     * 添加技能侦听器
     * @param skillListener 
     */
    public void addSkillListener(SkillListener skillListener) {
        if (skillListeners == null) {
            skillListeners = new ArrayList<SkillListener>();
        }
        if (!skillListeners.contains(skillListener)) {
            skillListeners.add(skillListener);
        }
    }
    
    /**
     * 移除技能侦听器
     * @param skillListener
     * @return 
     */
    public boolean removeSkillListener(SkillListener skillListener) {
        return skillListeners != null && skillListeners.remove(skillListener);
    }
    
    /**
     * 添加技能"执行“侦听器
     * @param skillPlayListener 
     */
    public void addSkillPlayListener(SkillPlayListener skillPlayListener) {
        if (skillPlayListeners == null) {
            skillPlayListeners = new ArrayList<SkillPlayListener>();
        }
        if (!skillPlayListeners.contains(skillPlayListener)) {
            skillPlayListeners.add(skillPlayListener);
        }
    }
    
    /**
     * 移除技能"执行"侦听器
     * @param skillPlayListener
     * @return 
     */
    public boolean removeSkillPlayListener(SkillPlayListener skillPlayListener) {
        return skillPlayListeners != null && skillPlayListeners.remove(skillPlayListener);
    }
    
    /**
     * 获取技能侦听器，如果没有添加过技能侦听器则返回null.
     * @return 
     */
    public List<SkillPlayListener> getSkillPlayListeners() {
        return skillPlayListeners;
    }
    
    /**
     * 获取当前正在执行的所有技能。
     * @return 
     */
    public List<Skill> getRunningSkills() {
        return runningSkills;
    }
    
    /**
     * 获取当前正在执行的技能状态，返回值中每个二进制位表示一个技能(SkillType),
     * 如果没有正在执行的任何技能则该值会返回 0
     * @return 
     */
    public long getRunningSkillStates() {
        return runningSkillStates;
    }
    
    /**
     * 获取最后一个执行的技能，如果没有执行过任何技能则返回null.<br>
     * 注：返回的技能有可能正在执行，也有可能已经结束。
     * @return 
     */
    public Skill getLastSkill() {
        return lastSkill;
    }
    
    /**
     * 执行一个技能,执行逻辑是这样的：<br>
     * 1.如果当前没有任何正在执行的技能则直接执行新技能并返回,不再执行后续判断，否则继续。<br>
     * 2.如果当前有正在执行的技能，并且新技能可以与这些技能重叠执行，则执行新技能并返回，否则继续。<br>
     * 3.打断当前正在执行的所有不能与新技能重叠执行的技能,然后执行新技能并返回。<br>
     * @param newSkill 
     */
    public void playSkill(Skill newSkill) {
        // 1.如果当前没有任何正在执行的技能则直接执行技能
        if (runningSkills.isEmpty()) {
            startNewSkill(newSkill);
            return;
        }
        
        // 2.如果新技能可以与正在执行的所有技能进行重叠则直接执行
        long overlaps = newSkill.getData().getOverlaps();
        if ((overlaps & runningSkillStates) == runningSkillStates) {
            startNewSkill(newSkill);
            return;
        }
        
        // 3.打断当前所有不能overlaps执行的技能,然后执行新技能,即只保留所有可重叠执行的技能
        for (Skill skill : runningSkills.getArray()) {
            if ((overlaps & (1 << skill.getSkillType().getValue())) == 0) {
                skill.cleanup();
            }
        }
        startNewSkill(newSkill);
    }
    
    private void startNewSkill(Skill newSkill) {
        // 执行技能
        lastSkill = newSkill;
        lastSkill.setActor(actor);
        lastSkill.setSkillControl(this);
        lastSkill.start();
        // 记录当前正在运行的所有技能类型
        if (!runningSkills.contains(lastSkill)) {
            runningSkills.add(lastSkill);
            runningSkillStates |= 1 << lastSkill.getSkillType().getValue();
        }
        
        // 执行侦听器
        if (skillPlayListeners != null && !skillPlayListeners.isEmpty()) {
            for (int i = 0; i < skillPlayListeners.size(); i++) {
                skillPlayListeners.get(i).onSkillStart(actor, lastSkill);
            }
        }
        
    }

}
