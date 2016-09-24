/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actorlogic;

import com.jme3.math.FastMath;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ActorLogicData;
import name.huliqing.core.mvc.network.ActorNetwork;
import name.huliqing.core.mvc.network.SkillNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.ActorListener;
import name.huliqing.core.object.module.SkillListener;
import name.huliqing.core.object.module.SkillModule;
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
//    private static final Logger LOG = Logger.getLogger(DefendActorLogic.class.getName());
    
    private final ActorService actorService = Factory.get(ActorService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private SkillModule skillModule;
    
    // 使用哪一个属性作为防守概率及躲闪概率
    private String defendRateAttribute;
    private String duckRateAttribute;
    // 哪些属性会响应OnHit,即监听哪些属性被击中
    private List<String> listenAttributes;
    
    // 被当前侦听(skillListener)的其它角色
    private Set<Actor> listenersActors;
    // 指定要监听的目标角色所发出的技能,当目标角色发出这些技能时，当前角色会偿试进行防守或躲闪
    private long listenSkillTags;
    // 当前角色可以用来防守的技能类型
    private long defendSkillTags;
    // 当前角色可以用来躲闪的技能类型。
    private long duckSkillTags;
    
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
        listenSkillTags = skillService.convertSkillTags(data.getAsArray("listenSkillTags"));
        defendSkillTags = skillService.convertSkillTags(data.getAsArray("defendSkillTags"));
        duckSkillTags = skillService.convertSkillTags(data.getAsArray("duckSkillTags"));
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
    public void onActorLocked(long source, Actor other) {
        if (!hasUsableSkill) 
            return;
        
        if (source == other.getData().getUniqueId()) 
            return;
        
        if (!actorService.isEnemy(other, playService.findActor(source)))
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
    public void onActorReleased(long source, Actor other) {
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
        
        // 不是所侦听的技能(只有listenSkillTags所指定的技能类型才需要防守或躲闪 )
        if ((listenSkillTags & skill.getData().getTags()) == 0) 
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
        if (getSkillModule().isPlayingSkill(duckSkillTags)) {
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
            float defendRate = attributeService.getNumberAttributeValue(actor, defendRateAttribute, 0);
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
            float duckRate = attributeService.getNumberAttributeValue(actor, defendRateAttribute, 0);
            if (duckRate >= FastMath.nextRandomFloat()) {
                Skill duckSkill = duckSkills.get(FastMath.nextRandomInt(0, duckSkills.size() - 1));
                skillNetwork.playSkill(actor, duckSkill, false);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doLogic(float tpf) {
        // 技能重新缓存技能
        if (needRecacheSkill) {
            defendSkills = getSkillModule().getSkillByTags(defendSkillTags, defendSkills);
            duckSkills = getSkillModule().getSkillByTags(duckSkillTags, duckSkills);
            hasUsableSkill = (defendSkills != null && defendSkills.size() > 0) || (duckSkills != null && duckSkills.size() > 0);
            needRecacheSkill = false;
        }
    }

    private SkillModule getSkillModule() {
        if (skillModule == null) {
            skillModule = actor.getModule(SkillModule.class);
        }
        return skillModule;
    }

}
