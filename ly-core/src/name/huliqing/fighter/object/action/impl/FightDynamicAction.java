/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.action.impl;

import com.jme3.math.FastMath;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.SkillConstants;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.action.FightAction;
import name.huliqing.fighter.data.ActionData;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.enums.SkillType;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.network.SkillNetwork;
import name.huliqing.fighter.game.network.SkinNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.AttributeService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.SkillService;
import name.huliqing.fighter.game.service.SkinService;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.object.skill.HitSkill;
import name.huliqing.fighter.object.skill.Skill;
import name.huliqing.fighter.utils.MathUtils;

/**
 * 动态的角色战斗行为，该战斗行为下角色能够移动，转向跟随等。
 * @author huliqing
 */
public class FightDynamicAction extends FollowPathAction implements FightAction {
    private static final Logger LOG = Logger.getLogger(FightDynamicAction.class.getName());
    
    private final StateService stateService = Factory.get(StateService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkinService skinService = Factory.get(SkinService.class);
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final SkinNetwork skinNetwork = Factory.get(SkinNetwork.class);
    
    // 是否允许跟随敌人
    protected boolean allowFollow = true;
    // 允许跟随的时长限制,在该跟随时间内如果无法攻击目标,则停止跟随,这可以防止
    // monster一下跟随攻击目标.(单位秒)
    protected float followTimeMax = 8;
    protected float followTimeUsed;
    
    // 能够自动使用的攻击技能,该参数指定战斗过程中允许使用的攻击技能类型：包含：
    // attack.common/trick/shot/magic, 如果该值为null,则允许所有类型的战斗技能.
    // 否则只使用给定的攻击技能。该参数主要是为了让玩家角色在战斗过程中能够自行
    // 控制一些技能，如魔法技能，可能玩家角色不希望AI自动使用这些技能以消耗魔法。
    private Set<SkillType> attackSkillTypes;
    
    // 战斗结束后自动摘下武器
    private boolean autoTakeOffWeapon = true;
    
    // 用于限制攻击时间间隔的角色属性的名称,这个属性的值的有效范围在[0.0~1.0]之间，
    // 超过该范围则被截取。
    private String attackIntervalAttribute;
    
    // 攻击间隔的最高时间限制，单位秒。
    // 攻击时间间隔受属性影响。
    private float attackIntervalMax = 1f;
    
    // --- inner
    
    // 当前准备使用于攻击的技能
    protected SkillData skill;
    // 性能优化
    protected Skill tempSkillInstance;
    protected Actor enemy;
    
    // 上一次使用技能后到当前所使用的时间，如果该值小于interval则不允许NPC发技能，
    // 避免连续发技能。
    protected float timeUsed;
    // 出招延迟限制,避免NPC角色出招间隔太短
    protected float interval;
  
    // 最近一次切换武器后的武器状态
    private int lastWeaponState = -1;
    
    // 最近获取技能的时间。
    protected long lastGetCommonSkillTime;
    // 最近获取到的技能缓存,注：当武器类型发生变化或者角色的技能增或减少
    // 时都应该重新获取。
    protected List<SkillData> attackSkills = new ArrayList<SkillData>();
    
    // 缓存技能id
    private String waitSkillId;

    public FightDynamicAction() {
        super();
    }
    
    public FightDynamicAction(ActionData data) {
        super(data);
        followTimeMax = data.getAsFloat("followTimeMax", followTimeMax);
        allowFollow = data.getAsBoolean("allowFollow", allowFollow);
        autoTakeOffWeapon = data.getAsBoolean("autoTakeOffWeapon", autoTakeOffWeapon);
        attackIntervalAttribute = data.getAttribute("attackIntervalAttribute");
        attackIntervalMax = data.getAsFloat("attackIntervalMax", attackIntervalMax);
        String[] tempSkillTypes = data.getAsArray("attackSkillTypes");
        if (tempSkillTypes != null && tempSkillTypes.length > 0) {
            attackSkillTypes = EnumSet.noneOf(SkillType.class);
            for (String st : tempSkillTypes) {
                attackSkillTypes.add(SkillType.identifyByName(st));
            }
        }
    }

    @Override
    public void setEnemy(Actor enemy) {
        this.enemy = enemy;
        super.setFollow(enemy.getModel());
        
//      // TODO 20150612,timeUsed=interval主要是让player能够在执行action后立即
        // 响应行为，但是Ai逻辑中可能会直接调用setEnemy和setSkill,所以这里可能
        // 需要优化，拆分一个API出来直接让player调用。
//        timeUsed = interval;
    }
    
    @Override
    public void setSkill(SkillData skill) {
        if (skill != null) {
            this.skill = skill;
            this.tempSkillInstance = skillService.getSkillInstance(actor, skill.getId());
        }
        
        // add20150611,处理player在点了“攻击”图标后会不定时长的延迟一段时间的问题
        timeUsed = interval;
    }
    
    @Override
    protected void doInit() {
        super.doInit();
        // 如果角色切换到战斗状态，则强制取出武器
        if (!skinService.isWeaponTakeOn(actor)) {
            skinNetwork.takeOnWeapon(actor, true);
        }
        
        SkillData waitSkill = skillService.getSkill(actor, SkillType.wait);
        if (waitSkill != null) {
            waitSkillId = waitSkill.getId();
        }
    }
    
    @Override
    public void doLogic(float tpf) {
        timeUsed += tpf;
        
        // remove20151229
//        // 很重要:自动AI需要进行攻击时间限制
//        if (actorService.isAutoAi(actor) && timeUsed < interval) {
//            return;
//        }

        // 攻击间隔时间限制
        if (timeUsed < interval) {
            return;
        }
        
        if (enemy == null 
                || enemy.isDead() 
                || !playService.isInScene(enemy) 
                
                // remove20160217,不再判断是否为敌人，是否可攻击目标以后交由hitChecker判断
                // 放开这个判断可允许玩家控制角色攻击同伴，只要技能的hitChecker设置即可。
//                || !enemy.isEnemy(actor) 
                
                ) {
            
            // 刻偿试为当前角色查找一次敌人，以避免SearchEnemyLogic的延迟
            Actor newTarget = actorService.findNearestEnemyExcept(actor, actorService.getViewDistance(actor), enemy);
            if (newTarget != null) {
                actorNetwork.setTarget(actor, newTarget);
                setEnemy(newTarget);
            } else {
                // 如果找不到下一个目标，则这里要释放目标。
                actorNetwork.setTarget(actor, null);
//                SkillData waitSkill = skillService.getSkill(actor, SkillType.wait);
                if (waitSkillId != null) {
                    skillNetwork.playSkill(actor, waitSkillId, false);
                }
                end();
            }
            
            return;
        }
        
//        if (actor.getData().getAttributeStore().getName().equals("樱")) {
//            System.out.println("测试。。。");
//        }
        
        // 检查是否需要重新缓存技能，没有技能，或者期间突然切换了武器则需要重新载入技能。
        // 因为武器切换稍频繁，为了及时检测到武器切换，所以放在这里进行检查,这可避免像这种情况：
        // 当角色突然从刀类切换到弓类武器后，因没有更新技能，导致角色拿着弓类武器
        // 却使用刀剑类的技能去判断目标是否在技能范围内。这导致角色拿着弓类武器走到刀剑的技能范围内才进行射击。
        checkAndRecacheSkill();

        if (skill == null) {
            skill = getSkill();
            // 并不一定能够找到合适的技能，如果没有合适的技能则返回。
            if (skill == null) {
                return;
            }
            tempSkillInstance = skillService.getSkillInstance(actor, skill.getId());
        }
        
        // 判断是否在攻击范围内,或者跟随目标
        if (!isPlayable(skill, enemy)) {
            
            // 如果不允许跟随敌人,则清空敌人（或许敌人已经走出攻击范围外。）
            // 清空目标及结束当前行为
            if (!isAllowFollow()) {
                
                // 刻偿试为当前角色查找一次敌人，以避免SearchEnemyLogic的延迟
                Actor newTarget = actorService.findNearestEnemyExcept(actor, actorService.getViewDistance(actor), enemy);
                if (newTarget != null) {
                    actorNetwork.setTarget(actor, newTarget);
                    setEnemy(newTarget);
                } else {
                    // 如果找不到下一个目标，则这里要释放目标。
                    actorNetwork.setTarget(actor, null);
                    if (waitSkillId != null) {
                        skillNetwork.playSkill(actor, waitSkillId, false);
                    }
                    end();
                }
                
            } else {
                super.doFollow(enemy.getModel(), tpf);
                // 如果跟随的时间达到允许的跟随限制,则不再跟随,并退出当前行为.
                followTimeUsed += tpf;
                if (followTimeUsed >= followTimeMax) {
                    followTimeUsed = 0;
                    
                    // 刻偿试为当前角色查找一次敌人，以避免SearchEnemyLogic的延迟
                    Actor newTarget = actorService.findNearestEnemyExcept(actor, actorService.getViewDistance(actor), enemy);
                    if (newTarget != null) {
                        actorNetwork.setTarget(actor, newTarget);
                        setEnemy(newTarget);
                    } else {
                        // 如果找不到下一个目标，则这里要释放目标。
                        actorNetwork.setTarget(actor, null);
                        if (waitSkillId != null) {
                            skillNetwork.playSkill(actor, waitSkillId, false);
                        }
                        end();
                    }
                }
            }
            return;
        }
        
        // 在攻击范围内则进行攻击
        // 技能执行成功，并不一定能完整执行完，在攻击过程中可能被打断。即使如此，仍然
        // 要等待一定时间间隔,以避免攻击太频繁。
        if (attack(skill)) {
            followTimeUsed = 0;
            timeUsed = 0;
            
            // 当attackIntervalAttribute = 1.0f的时候，攻击间隔限制将完全消失
            // 也即可看到角色最平滑的连招
            float attackLimit = attackIntervalMax;
            if (attackIntervalAttribute != null) {
                attackLimit = attackIntervalMax - attackIntervalMax * attributeService.getDynamicValue(actor, attackIntervalAttribute);
                attackLimit = MathUtils.clamp(attackLimit, 0, attackIntervalMax);
            }
            interval = skillService.getSkillTrueUseTime(actor, skill) + attackLimit;
            
        }
        
        // 不管攻击是否成功，都要清空技能，以便使用下一个技能。
        skill = null;
        tempSkillInstance = null;
        
        // 如果不是自动AI，则应该退出。 
        if (!actorService.isAutoAi(actor) || actorService.getTarget(actor) == null) {
            end();
        }
    }

    @Override
    public void cleanup() {
        if (autoTakeOffWeapon && skinService.isWeaponTakeOn(actor)) {
            skinNetwork.takeOffWeapon(actor, true);
        }
        super.cleanup();
    }
    
    /**
     * 使用指定技能攻击目标。
     * @param skill
     * @return 
     */
    protected boolean attack(SkillData skill) {
        // 使角色朝向目标
        if (isAutoFacing()) {
            skillNetwork.playFaceTo(actor, enemy.getModel().getWorldTranslation());
        }
        return skillNetwork.playSkill(actor, skill.getId(), false);
    }
    
    /**
     * 判断是否在技能的攻击范围内
     * @param attackSkill 使用的技能
     * @return 
     */
    protected boolean isPlayable(SkillData attackSkill, Actor target) {
        // 正常攻击类技能都应该是HitSkill,使用hitSkill的isInHitDistance来判断以优化
        // 性能，
//        Skill as = skillService.getSkillInstance(actor, attackSkill.getId());
        if (tempSkillInstance instanceof HitSkill) {
            return ((HitSkill) tempSkillInstance).isInHitDistance(target);
        }
        
        // 只有非HitSkill时才使用canPlay，这个方法稍微耗性能
        return tempSkillInstance.canPlay() == SkillConstants.STATE_OK;
    }
    
    private void checkAndRecacheSkill() {
        
        // 如果技能列表有更新,或者角色武器类型发生变化则重新获取技能进行缓存
        int weaponState = skinService.getWeaponState(actor);
        if (lastWeaponState != weaponState
                || lastGetCommonSkillTime < actor.getData().getSkillStore().getLastModifyTime()) {
            if (Config.debug) {
                LOG.log(Level.INFO, "last weaponState={0}({1}), actor current weaponState={2}({3}), need to reload skills."
                        , new Object[] {lastWeaponState, Integer.toBinaryString(lastWeaponState)
                                , weaponState, Integer.toBinaryString(weaponState)});
            }
            
            lastGetCommonSkillTime = Common.getGameTime();
            
            lastWeaponState = weaponState;
            if (attackSkills != null) {
                attackSkills.clear();
            }
            attackSkills = loadAttackSkill(actor, lastWeaponState, attackSkillTypes, attackSkills);
            
            // 重新缓存技能后，检查一次当前正在使用的技能是否适合当前的武器，如果不行则清除它，让它
            // 重新获取一个可用的。否则不应该清除当前的技能。
            if (skill != null) {
                List<Integer> weaponLimit = skill.getWeaponStateLimit();
                if (weaponLimit != null && !weaponLimit.isEmpty() && !weaponLimit.contains(lastWeaponState)) {
                    skill = null;
                    tempSkillInstance = null;
                }
            }
        }
    }
    
    private SkillData getSkill() {
        if (attackSkills == null || attackSkills .isEmpty()) {
            return null;
        } else {
            // 获取一个不处于冷却中的技能
            return getNotCooldown(attackSkills);
        }
    }
    
    private SkillData getNotCooldown(List<SkillData> skills) {
        int startIndex = FastMath.nextRandomInt(0, skills.size() - 1);
        SkillData result;
        for (int i = startIndex;;) {
            result = skills.get(i);
            if (!skillService.isCooldown(result)) {
                return result;
            }
            i++;
            // 达到list最后则i重新从0开始。
            if (i >= skills.size()) {
                i = 0;
            }
            // 绕了一个loop，如果还没有适合的技能，则返回null.
            if (i == startIndex) {
                return null;
            }
        }
    }
    
    /**
     * 从角色身上获取适用于指定武器类型的攻击技能,只要适合于该武器的"攻击“技能就可以，
     * 不管是否处于冷却中。另外如果skillTypes不为null,则获取的攻击技能必须限制在这个列表之内
     * @param ac 指定的角色
     * @param weaponState 
     * @param skillTypes 指定的技能类型限制
     * @param store 存放结果集，如果为null则创建一个
     * @return "攻击"技能列表,可能包含：attack.common/shot/trick/magic
     */
    protected List<SkillData> loadAttackSkill(Actor ac, int weaponState, Set<SkillType> skillTypes, List<SkillData> store) {
        if (store == null) {
            store = new ArrayList<SkillData>();
        }
        
        // 从攻击类技能中找出符合当前武器使用的以及在指定的技能类型内
        List<SkillData> allSkills = skillService.getSkill(actor);
        for (SkillData as : allSkills) {
            // 如果非攻击类技能
            if (as.getSkillType() != SkillType.attack && as.getSkillType() != SkillType.trick) {
                continue;
            }
            
            // 技能类型过滤,如果指定了只能使用的技能类型。
            if (skillTypes != null && !skillTypes.contains(as.getSkillType())) {
                continue;
            }
            
            // 武器类型的过滤,只有技能与当前武器相容才能添加
            if (as.getWeaponStateLimit() == null 
                    || as.getWeaponStateLimit().contains(weaponState)) {
                store.add(as);
            }
        }

        return store;
    }

    /**
     * 设置是否允许跟随，只有打开跟随，并且角色质量mass大于0才能进行跟随。
     * @param allowFollow 
     */
    public void setAllowFollow(boolean allowFollow) {
        this.allowFollow = allowFollow;
    }
    
    /**
     * 是否允许跟随攻击敌人,只有打开跟随并且角色质量(mass)大于0才能进行跟随。
     * @return 
     */
    protected boolean isAllowFollow() {
        return allowFollow && actor.getMass() > 0;
    }
    
    public float getFollowTimeMax() {
        return followTimeMax;
    }

    public void setFollowTimeMax(float followTimeMax) {
        this.followTimeMax = followTimeMax;
    }
    
}
