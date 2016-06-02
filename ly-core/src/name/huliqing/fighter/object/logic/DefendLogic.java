/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.logic;

import com.jme3.math.FastMath;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.LogicData;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.enums.SkillType;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.network.SkillNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.AttributeService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.SkillService;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.actor.ActorListener;
import name.huliqing.fighter.object.actor.SkillListener;
import name.huliqing.fighter.object.skill.Skill;
import name.huliqing.fighter.object.skill.impl.AttackSkill;
import name.huliqing.fighter.object.skill.impl.ShotSkill;

/**
 * 防守逻辑
 * @author huliqing
 */
public class DefendLogic extends ActorLogic implements SkillListener, ActorListener {
    private static final Logger LOG = Logger.getLogger(DefendLogic.class.getName());
    
    private final PlayService playService = Factory.get(PlayService.class);
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
    private long lastCheckTime = -1;
    // 判断是否有可用的技能进行防守
    private boolean hasUsableSkill = true;
    
    public DefendLogic(LogicData data) {
        super(data);
        interval = 10; // 逻辑不需要频繁
        defendRateAttribute = data.getAttribute("defendRateAttribute");
        duckRateAttribute = data.getAttribute("duckRateAttribute");
        listenAttributes = Arrays.asList(data.getAsArray("listenAttributes"));
    }
    
    @Override
    public void initialize() {
        super.initialize();
        actorService.addActorListener(self, this);
    }

    @Override
    public void onActorLocked(Actor source, Actor other) {
        if (!hasUsableSkill) 
            return;
        if (source == other) 
            return;
        if (!other.isEnemy(source))
            return;
        // 当被other锁定时给other添加侦听器，以侦察other的攻击。以便进行防守和躲闪
        actorService.addSkillListener(other, this);
        
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
        actorService.removeSkillListener(other, this);
        
        // 清理
        if (listenersActors != null) {
            listenersActors.remove(other);
        }
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
        if (self.isDead()) 
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
        if (self.isDucking()) {
            return;
        }
        doDuck();
    }
    
    @Override
    public void onSkillEnd(Actor source, Skill skill) {
        // ignore
    }
    
    @Override
    public void onActorKill(Actor source, Actor target) {
        // ignore
    }

    @Override
    public void onActorKilled(Actor source, Actor target) {
        // ignore
    }

    // 受到攻击时将目标设为首要敌人
    @Override
    public void onActorHit(Actor source, Actor attacker, String hitAttribute, float hitValue) {
        // hitValue>0为增益效果，不处理
        if (source.isDead() || self.isPlayer() || hitValue > 0)
            return;
        
        // 被击中的属性不在监听范围内则不处理。
        if (!listenAttributes.contains(hitAttribute)) {
            return;
        }
        
        if (attacker != null) {
            actorNetwork.setTarget(source, attacker);
        }
    }
    
    private boolean doDefend() {
        if (defendRateAttribute != null) {
            float defendRate = attributeService.getDynamicValue(self, defendRateAttribute);
            if(defendRate >= FastMath.nextRandomFloat()) {
                SkillData defendSkill = skillService.getSkillRandomDefend(self);
                if (defendSkill != null) {
                    skillNetwork.playSkill(self, defendSkill.getId(), false);
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean doDuck() {
        if (duckRateAttribute != null) {
            float duckRate = attributeService.getDynamicValue(self, defendRateAttribute);
            if (duckRate >= FastMath.nextRandomFloat()) {
                SkillData duckSkill = skillService.getSkillRandomDuck(self);
                if (duckSkill != null) {
                    skillNetwork.playSkill(self, duckSkill.getId(), false);
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void cleanup() {
        // 清理当前角色的侦听器
        actorService.removeActorListener(self, this);
        
        // 清理其它被当前逻辑侦听的角色
        if (listenersActors != null) {
            for (Actor other : listenersActors) {
                actorService.removeSkillListener(other, this);
            }
        }
        super.cleanup();
    }

    @Override
    protected void doLogic(float tpf) {
        // 技能无更新则不处理
        if (!actorService.isSkillUpdated(self, lastCheckTime)) {
            return;
        }
        // 判断是否有可用技能
        List<SkillData> skills = skillService.getSkill(self);
        if (skills == null) {
            hasUsableSkill = false;
            lastCheckTime = self.getData().getSkillStore().getLastModifyTime();
            return;
        }
        // 如果存在defend或duck技能则认为可用
        for (SkillData sd : skills) {
            if (sd.getSkillType() == SkillType.defend || sd.getSkillType() == SkillType.duck) {
                hasUsableSkill = true;
                lastCheckTime = self.getData().getSkillStore().getLastModifyTime();
                return;
            }
        }
    }



}
