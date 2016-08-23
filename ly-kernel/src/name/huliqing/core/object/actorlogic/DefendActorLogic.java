/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actorlogic;

import com.jme3.math.FastMath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ActorLogicData;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.mvc.network.ActorNetwork;
import name.huliqing.core.mvc.network.SkillNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.ActorListener;
import name.huliqing.core.object.module.SkillListener;
import name.huliqing.core.object.module.SkillPlayListener;
import name.huliqing.core.object.skill.Skill;
import name.huliqing.core.object.skill.AttackSkill;
import name.huliqing.core.object.skill.ShotSkill;

/**
 * 防守逻辑
 * @author huliqing
 * @param <T>
 */
public class DefendActorLogic<T extends ActorLogicData> extends ActorLogic<T> implements SkillListener, SkillPlayListener, ActorListener {
    private static final Logger LOG = Logger.getLogger(DefendActorLogic.class.getName());
    
    private final ActorService actorService = Factory.get(ActorService.class);
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    // 使用哪一个属性作为防守概率及躲闪概率
    private String defendRateAttribute;
    private String duckRateAttribute;
    // 哪些属性会响应OnHit,即监听哪些属性被击中
    private List<String> listenAttributes;
    
    // 被当前侦听(skillListener)的其它角色
    private Set<Actor> listenersActors;
    
    // ---- 节能
    // 判断是否有可用的技能进行防守
    private boolean hasUsableSkill = false;
    
    private boolean needRecacheSkill = true;
    
    private List<Skill> defendSkills;
    private List<Skill> duckSkills;
    
    @Override
    public void setData(T data) {
        super.setData(data); 
        interval = 10; 
        defendRateAttribute = data.getAsString("defendRateAttribute");
        duckRateAttribute = data.getAsString("duckRateAttribute");
        listenAttributes = Arrays.asList(data.getAsArray("listenAttributes"));
    }
    
    @Override
    public void initialize() {
        super.initialize();
        actorService.addActorListener(actor, this);
        skillService.addSkillListener(actor, this);
    }

    @Override
    public void cleanup() {
        // 清理当前角色的侦听器
        actorService.removeActorListener(actor, this);
        skillService.removeSkillListener(actor, this);
        
        // 清理其它被当前逻辑侦听的角色
        if (listenersActors != null) {
            for (Actor other : listenersActors) {
                skillService.removeSkillPlayListener(other, this);
            }
            listenersActors.clear();
        }
        super.cleanup();
    }

    @Override
    public void onActorLocked(Actor source, Actor other) {
        if (!hasUsableSkill) 
            return;
        
        if (source == other) 
            return;
        if (!actorService.isEnemy(other, source))
            return;
        
        // 当被other锁定时给other添加侦听器，以侦察other的攻击。以便进行防守和躲闪
        skillService.addSkillPlayListener(other, this);
        
        // 记录被侦听的对象，以便在当前角色销毁或退出时清理
        if (listenersActors == null) {
            listenersActors = new HashSet<Actor>();
        }
        listenersActors.add(other);
    }

    @Override
    public void onActorReleased(Actor source, Actor other) {
        if (!hasUsableSkill) 
            return;
        
        // 当other不再把source当前目标时就不再需要侦听了。
        skillService.removeSkillPlayListener(other, this);
        
        // 清理
        if (listenersActors != null) {
            listenersActors.remove(other);
        }
    }
    
    // ignore
    @Override
    public void onActorKill(Actor source, Actor target) {}

    // ignore
    @Override
    public void onActorKilled(Actor source, Actor target) {}

    // 受到攻击时将目标设为首要敌人
    @Override
    public void onActorHit(Actor source, Actor attacker, String hitAttribute, float hitValue) {
        // hitValue>0为增益效果，不处理
        if (actorService.isDead(source) || actorService.isPlayer(actor) || hitValue > 0)
            return;
        
        // 被击中的属性不在监听范围内则不处理。
        if (!listenAttributes.contains(hitAttribute)) {
            return;
        }
        
        if (attacker != null) {
            actorNetwork.setTarget(source, attacker);
        }
    }

    @Override
    public void onSkillAdded(Actor actor, Skill skill) {
        needRecacheSkill = true;
    }

    @Override
    public void onSkillRemoved(Actor actor, Skill skill) {
        needRecacheSkill = true;
    }

    @Override
    public boolean onSkillHookCheck(Actor source, Skill skill) {
        return true;
    }
    
    @Override
    public void onSkillStart(Actor source, Skill skill) {
        if (!hasUsableSkill) 
            return;
        
        // 如果已经死亡就不需要处理防守了
        if (actorService.isDead(actor)) 
            return;
        
        // 暂不支持shot类技能的防守
        if (skill instanceof ShotSkill)
            return;
        
        // 只有attack和trick才需要防守
        if (skill.getSkillType() != SkillType.attack && skill.getSkillType() != SkillType.trick)
            return;
        
        // 2.正常防守,只有普通攻击技能才允许防守
        if (skill instanceof AttackSkill) {
            AttackSkill aSkill = (AttackSkill) skill;
            if (aSkill.isDefendable()) {
                // 在可防守的技能下给一个机会使用躲闪技能,以避免可能出现的一直使用防守技能的问题。
                if (FastMath.nextRandomFloat() < 0.75f) {
                    if (doDefend()) {
                        return;
                    }
                }
            }
        }
        
        // 3.闪避防守
        if (skillService.isDucking(actor)) {
            return;
        }
        doDuck();
    }
    
    @Override
    public void onSkillEnd(Actor source, Skill skill) {
        // ignore
    }
    
    
    private boolean doDefend() {
        if (defendRateAttribute != null && defendSkills.size() > 0) {
            float defendRate = attributeService.getDynamicValue(actor, defendRateAttribute);
            if(defendRate >= FastMath.nextRandomFloat()) {
                Skill defendSkill = defendSkills.get(FastMath.nextRandomInt(0, defendSkills.size() - 1));
                skillNetwork.playSkill(actor, defendSkill.getData().getId(), false);
                return true;
            }
        }
        return false;
    }
    
    private boolean doDuck() {
        if (duckRateAttribute != null && duckSkills.size() > 0) {
            float duckRate = attributeService.getDynamicValue(actor, defendRateAttribute);
            if (duckRate >= FastMath.nextRandomFloat()) {
                Skill duckSkill = duckSkills.get(FastMath.nextRandomInt(0, duckSkills.size() - 1));
                skillNetwork.playSkill(actor, duckSkill.getData().getId(), false);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doLogic(float tpf) {
        // 技能无更新则不处理
        if (needRecacheSkill) {
            // 判断是否有可用技能
            List<Skill> skills = skillService.getSkills(actor);
            if (skills != null) {
                defendSkills = new ArrayList<Skill>();
                duckSkills = new ArrayList<Skill>();
                // 如果存在defend或duck技能则认为可用
                for (Skill skill : skills) {
                    if (skill.getSkillType() == SkillType.defend) {
                        defendSkills.add(skill);
                    }
                    if (skill.getSkillType() == SkillType.duck) {
                        duckSkills.add(skill);
                    }
                }
                hasUsableSkill = defendSkills.size() > 0 || duckSkills.size() > 0;
            }
            needRecacheSkill = false;
        }
    }



}
