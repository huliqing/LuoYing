/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skill;

import com.jme3.math.Vector3f;
import com.jme3.util.SafeArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import name.huliqing.core.Factory;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.object.actor.SkillListener;

/**
 * @author huliqing
 */
public class SkillProcessorImpl implements SkillProcessor {
    private final ActorService actorService = Factory.get(ActorService.class);
    private Actor actor;
    
    // 所有可用的技能处理器,缓存技能处理器,这样不需要每次都去重新载入技能处理器
    private final Map<String, Skill> skills = new HashMap<String, Skill>();
    
    // 当前正在执行的技能列表,
    // 如果runningSkills都执行完毕,则清空.但是lastSkill仍保持最近刚刚执行的技能的引用.
    private final SafeArrayList<Skill> runningSkills = new SafeArrayList<Skill>(Skill.class);
    // 当前正在运行的所有技能的类型，其中每一个二进制位表示一个技能类型。
    private long runningSkillStates;
    
    // 最近一个执行的技能,这个技能可能正在执行，也可能已经停止。
    private Skill lastSkill;
    
//    // 被锁定的技能列表，二进制表示，其中每1个二进制位表示一个技能类型。
//    // 如果指定的位为1，则表示这个技能被锁定，则该技能将不能执行。
//    private int lockedState;
    
    public SkillProcessorImpl(Actor actor) {
        this.actor = actor;
    }
    
    @Override
    public Skill findSkill(SkillData skillData) {
        Skill skill = skills.get(skillData.getId());
        if (skill == null) {
            skill = Loader.loadSkill(skillData);
            skill.setActor(actor);
            skill.setAnimChannelProcessor(actor.getChannelProcessor());
            skills.put(skillData.getId(), skill);
        }
        return skill;
    }
    
    @Override
    public long getRunningSkillStates() {
        return runningSkillStates;
    }
    
    @Override
    public void playSkill(Skill newSkill) {
        // 1.如果当前没有任何正在执行的技能则直接执行技能
        if (runningSkills.isEmpty()) {
            startNewSkill(newSkill);
            return;
        }
        
        // 2.如果新技能可以与正在执行的所有技能进行重叠则直接执行
        long overlaps = newSkill.getSkillData().getOverlaps();
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

    @Override 
    public boolean playReset() {
        // reset时需要把所有可能还在执行中的技能结束，并清除出列表．
        // 注意: 一定要清除出列表,这可避免在reset后由于角色的状态发生变化引起runningSkills中的skill
        // 触发onStateChange而导致skill做出一些重新运行的可能，如下(walkSkill)
        // skillProcessor.playReset　-> skillProcessor.notifyStateChange -> skill.onStateChange (walkSkill可能重启animation)
        if (!runningSkills.isEmpty()) {
            for (Skill skill : runningSkills.getArray()) {
                if (!skill.isEnd()) {
                    skill.cleanup();
                }
            }
            runningSkills.clear();
        }
        
        // 进行reset
        if (actor.getChannelProcessor() != null) {
            actor.getChannelProcessor().reset();
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean playFaceTo(Vector3f position) {
        Vector3f viewDir = actor.getViewDirection();
        position.subtract(actor.getModel().getWorldTranslation(), viewDir).normalizeLocal();
        actor.setViewDirection(viewDir);
        
        // 朝向的时候如果角色正在走动，则应该同时更新角色的跑步方向。
        // 或者通过walkDir.length()来判断角色是否在移动也可以
        if (isRunning() || isWalking()) {
            Vector3f walkDir = actor.getWalkDirection();
            float speed = walkDir.length();
            actor.setWalkDirection(viewDir.mult(speed, walkDir));
        }
        
        return true;
    }
    
    private void startNewSkill(Skill newSkill) {
        // 检查是否存在动画，如果没有则载入。
        actorService.checkAndLoadAnim(actor, newSkill.getSkillData().getAnimation());
        
        // 执行技能
        lastSkill = newSkill;
        lastSkill.setActor(actor);
        lastSkill.setAnimChannelProcessor(actor.getChannelProcessor());
        lastSkill.start();
        // 记录当前正在运行的所有技能类型
        if (!runningSkills.contains(lastSkill)) {
            runningSkills.add(lastSkill);
            runningSkillStates |= 1 << lastSkill.getSkillType().getValue();
        }
        
        // 执行侦听器
        List<SkillListener> sls = actor.getSkillListeners();
        if (!sls.isEmpty()) {
            for (SkillListener sl : sls) {
                sl.onSkillStart(actor, lastSkill);
            }
        }
    }
    
    @Override
    public void update(float tpf) {
        // 更新并尝试移除已经结束的技能
        if (!runningSkills.isEmpty()) {

            int oldSize = runningSkills.size();
            for (Skill skill : runningSkills.getArray()) {
                if (skill.isEnd()) {
                    runningSkills.remove(skill);
                    // 执行侦听器
                    List<SkillListener> sls = actor.getSkillListeners();
                    if (!sls.isEmpty()) {
                        for (SkillListener sl : sls) {
                            sl.onSkillEnd(actor, skill);
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
    public boolean isPlayingSkill() {
        return runningSkillStates != 0;
    }

    @Override
    public boolean isPlayingSkill(SkillType skillType) {
        return (runningSkillStates & (1 << skillType.getValue())) != 0;
    }
    
    @Override
    public boolean isWaiting() {
        // remove20160420,保持简单，少BUG，不要把isReset混进来
        // 部分角色可能没有wait状态，则需要使用该方法来判断。
//        if (actor.getChannelProcessor() != null && actor.getChannelProcessor().isReset()) {
//            return true;
//        }
        
        return lastSkill != null
                && lastSkill.getSkillType() == SkillType.wait;
    }
    
    @Override
    public boolean isWalking() {
        return (runningSkillStates & (1 << SkillType.walk.getValue())) != 0;
    }
    
    @Override
    public boolean isRunning() {
        return (runningSkillStates & (1 << SkillType.run.getValue())) != 0;
    }
  
    @Override
    public boolean isAttacking() {
        return (runningSkillStates & (1 << SkillType.attack.getValue())) != 0;
    }
    
    @Override
    public boolean isDefending() {
        return (runningSkillStates & (1 << SkillType.defend.getValue())) != 0;
    }
    
    @Override
    public boolean isDucking() {
        return (runningSkillStates & (1 << SkillType.duck.getValue())) != 0;
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
    }

    @Override
    public Skill getLastPlayingSkill() {
        if (lastSkill != null && !lastSkill.isEnd()) {
            return lastSkill;
        }
        return null;
    }

    @Override
    public Skill getLastSkill() {
        return lastSkill;
    }

    @Override
    public Skill getPlayingSkill(SkillType skillType) {
        for (Skill skill : runningSkills.getArray()) {
            if (skill.getSkillType() == skillType) {
                return skill;
            }
        }
        return null;
    }

    @Override
    public long getPlayingSkillStates() {
        return runningSkillStates;
    }

    
    
}
