/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.action;

import com.jme3.math.FastMath;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.LY;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ActionData;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.mvc.network.ActorNetwork;
import name.huliqing.core.mvc.network.SkillNetwork;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.SkillListener;
import name.huliqing.core.object.skill.Skill;

/**
 * 适合于生物角色的空闲行为，角色每隔一段时间就会随机执行一个idle动作,在idle动作执行
 * 完毕的间隔期则执行wait循环动作。在此期间角色不会移动位置。
 * @author huliqing
 */
public class IdleDynamicAction extends AbstractAction implements IdleAction, SkillListener{
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    
    //  IDLE行为的最大时间间隔,单位秒
    private float intervalMax = 7.0f;
    private float intervalMin = 3.0f;
    
    // ---- 内部参数
    private final List<Skill> skills = new ArrayList<Skill>();
    private float interval = 4.0f;
    private float intervalUsed;
    
    // 缓存技能id
    private Skill waitSkill;
    
    public IdleDynamicAction() {
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
        recacheIdleSkills();
        waitSkill = skillService.getSkill(actor, SkillType.wait);
        skillService.addSkillListener(actor, this);
    }

    @Override
    public void cleanup() {
        skillService.removeSkillListener(actor, this);
        super.cleanup();
    }
    
    @Override
    public void doLogic(float tpf) {
        intervalUsed += tpf;
        
        if (intervalUsed < interval) {
            // 在idle动作执行的间隔要执行一个wait动作，使角色看起来不会像是完全静止的。
            if (!skillService.isPlayingSkill(actor)) {
                // 注：wait可能不是循环的，所以需要判断
                if (!skillService.isWaiting(actor) && waitSkill != null) {
                    skillNetwork.playSkill(actor, waitSkill, false);
                }
            }
            return;
        }
        
        Skill idle = getIdleSkill();
        if (idle == null) {
            return;
        }
        skillNetwork.playSkill(actor, idle, false);
        
        intervalUsed = 0;
        interval = (intervalMax - intervalMin) * FastMath.nextRandomFloat() + intervalMin;
        interval += idle.getTrueUseTime();
    }
    
    private Skill getIdleSkill() {
        if (skills.isEmpty()) {
            return null;
        }
        return skills.get(FastMath.nextRandomInt(0, skills.size() - 1));
    }

    @Override
    public void onSkillAdded(Actor actor, Skill skill) {
        recacheIdleSkills();
    }

    @Override
    public void onSkillRemoved(Actor actor, Skill skill) {
        recacheIdleSkills();
    }

    // 重新缓存技能
    private void recacheIdleSkills() {
        List<Skill> all = skillService.getSkills(actor);
        skills.clear();
        for (Skill sd : all) {
            if (sd.getSkillType() == SkillType.idle) {
                skills.add(sd);
            }
        }
    }
    
}
