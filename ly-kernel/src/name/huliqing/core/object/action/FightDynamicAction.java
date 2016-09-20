/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.action;

import com.jme3.math.FastMath;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.SkillConstants;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.ActionData;
import name.huliqing.core.mvc.network.ActorNetwork;
import name.huliqing.core.mvc.network.SkillNetwork;
import name.huliqing.core.mvc.network.SkinNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.mvc.service.SkinService;
import name.huliqing.core.object.module.ActorModule;
import name.huliqing.core.object.module.SkillListener;
import name.huliqing.core.object.skill.HitSkill;
import name.huliqing.core.object.skill.Skill;
import name.huliqing.core.object.skill.SkillTagFactory;
import name.huliqing.core.utils.MathUtils;

/**
 * 动态的角色战斗行为，该战斗行为下角色能够移动，转向跟随等。
 * @author huliqing
 */
public class FightDynamicAction extends FollowPathAction implements FightAction, SkillListener {
    private static final Logger LOG = Logger.getLogger(FightDynamicAction.class.getName());
//    private final StateService stateService = Factory.get(StateService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkinService skinService = Factory.get(SkinService.class);
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final SkinNetwork skinNetwork = Factory.get(SkinNetwork.class);
    private ActorModule actorModule;
    
    // 是否允许跟随敌人
    protected boolean allowFollow = true;
    // 允许跟随的时长限制,在该跟随时间内如果无法攻击目标,则停止跟随,这可以防止
    // monster一下跟随攻击目标.(单位秒)
    protected float followTimeMax = 8;
    protected float followTimeUsed;
    
    //  战斗技能tags
    private long fightSkillTags;
    
    // 战斗结束后自动摘下武器
    private boolean autoTakeOffWeapon = true;
    
    // 用于限制攻击时间间隔的角色属性的名称,这个属性的值的有效范围在[0.0~1.0]之间，
    // 超过该范围则被截取。
    private String attackIntervalAttribute;
    
    // 攻击间隔的最高时间限制，单位秒。
    // 攻击时间间隔受属性影响。
    private float attackIntervalMax = 1f;
    
    // ---- inner
    
    // 当前准备使用于攻击的技能
    protected Skill skill;
    protected Actor enemy;
    
    // 上一次使用技能后到当前所使用的时间，如果该值小于interval则不允许NPC发技能，
    // 避免连续发技能。
    protected float timeUsed;
    // 出招延迟限制,避免NPC角色出招间隔太短
    protected float interval;
  
    // 最近一次切换武器后的武器状态
    private int lastWeaponState = -1;
    
    // 最近获取到的技能缓存,注：当武器类型发生变化或者角色的技能增或减少
    // 时都应该重新获取。
    protected List<Skill> fightSkills = new ArrayList<Skill>();
    
    // 缓存技能
    private Skill waitSkill;

    public FightDynamicAction() {
        super();
    }
    
    @Override
    public void setData(ActionData data) {
        super.setData(data);
        followTimeMax = data.getAsFloat("followTimeMax", followTimeMax);
        allowFollow = data.getAsBoolean("allowFollow", allowFollow);
        autoTakeOffWeapon = data.getAsBoolean("autoTakeOffWeapon", autoTakeOffWeapon);
        attackIntervalAttribute = data.getAsString("attackIntervalAttribute");
        attackIntervalMax = data.getAsFloat("attackIntervalMax", attackIntervalMax);
        fightSkillTags = SkillTagFactory.convert(data.getAsArray("fightSkillTags"));
    }

    @Override
    public void setEnemy(Actor enemy) {
        this.enemy = enemy;
        super.setFollow(enemy.getSpatial());
        
//      // TODO 20150612,timeUsed=interval主要是让player能够在执行action后立即
        // 响应行为，但是Ai逻辑中可能会直接调用setEnemy和setSkill,所以这里可能
        // 需要优化，拆分一个API出来直接让player调用。
//        timeUsed = interval;
    }
    
    @Override
    public void setSkill(Skill skill) {
        // 这里判断skill是否为null以便不要覆盖当前this.skill
        if (skill != null) {
            this.skill = skill;
        }
        
        // add20150611,处理player在点了“攻击”图标后会不定时长的延迟一段时间的问题
        timeUsed = interval;
    }
    
    @Override
    public void initialize() {
        super.initialize();
        actorModule = actor.getModule(ActorModule.class);
        
        // 如果角色切换到战斗状态，则强制取出武器
        if (!skinService.isWeaponTakeOn(actor)) {
            skinNetwork.takeOnWeapon(actor, true);
        }
        
        waitSkill = skillService.getSkillWaitDefault(actor);
        skillService.addSkillListener(actor, this);
    }
    
    @Override
    public void cleanup() {
        if (autoTakeOffWeapon && skinService.isWeaponTakeOn(actor)) {
            skinNetwork.takeOffWeapon(actor, true);
        }
        skillService.removeSkillListener(actor, this);
        super.cleanup();
    }
    
    @Override
    public void doLogic(float tpf) {
        timeUsed += tpf;

        // 攻击间隔时间限制
        if (timeUsed < interval) {
            return;
        }
        
        if (enemy == null 
                || actorService.isDead(enemy) 
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
                if (waitSkill != null) {
                    skillNetwork.playSkill(actor, waitSkill, false);
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
        }
        
        // 判断是否在攻击范围内,或者跟随目标
        if (!isPlayable(skill, enemy)) {
            
            // 如果不允许跟随敌人,则清空敌人（或许敌人已经走出攻击范围外。）
            // 清空目标及结束当前行为
            if (!allowFollow || !actorModule.isMovable()) {
                
                // 刻偿试为当前角色查找一次敌人，以避免SearchEnemyLogic的延迟
                Actor newTarget = actorService.findNearestEnemyExcept(actor, actorService.getViewDistance(actor), enemy);
                if (newTarget != null) {
                    actorNetwork.setTarget(actor, newTarget);
                    setEnemy(newTarget);
                } else {
                    // 如果找不到下一个目标，则这里要释放目标。
                    actorNetwork.setTarget(actor, null);
                    if (waitSkill != null) {
                        skillNetwork.playSkill(actor, waitSkill, false);
                    }
                    end();
                }
                
            } else {
                super.doFollow(enemy.getSpatial(), tpf);
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
                        if (waitSkill != null) {
                            skillNetwork.playSkill(actor, waitSkill, false);
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
                attackLimit = attackIntervalMax - attackIntervalMax * attributeService.getNumberAttributeValue(actor, attackIntervalAttribute, 0);
                attackLimit = MathUtils.clamp(attackLimit, 0, attackIntervalMax);
            }
            interval = skill.getTrueUseTime() + attackLimit;
            
        }
        
        // 不管攻击是否成功，都要清空技能，以便使用下一个技能。
        skill = null;
   
        // remove20160831,暂不判断isAutoAi(actor)
//        // 如果不是自动AI，则应该退出。 
//        if (!actorService.isAutoAi(actor) || actorService.getTarget(actor) == null) {
//            end();
//        }

        // 如果不是自动AI，则应该退出。 
        if (actorService.getTarget(actor) == null) {
            end();
        }
    }
    
    /**
     * 使用指定技能攻击目标。
     * @param skill
     * @return 
     */
    protected boolean attack(Skill skill) {
        // 使角色朝向目标
        if (autoFacing) {
            actorNetwork.setLookAt(actor, enemy.getSpatial().getWorldTranslation());
        }
        return skillNetwork.playSkill(actor, skill, false);
    }
    
    /**
     * 判断是否在技能的攻击范围内
     * @param attackSkill 使用的技能
     * @param target
     * @return 
     */
    protected boolean isPlayable(Skill attackSkill, Actor target) {
        // 正常攻击类技能都应该是HitSkill,使用hitSkill的isInHitDistance来判断以优化
        // 性能，
        if (attackSkill instanceof HitSkill) {
            return ((HitSkill) attackSkill).isInHitDistance(target);
        }
        
        // 只有非HitSkill时才使用canPlay，这个方法稍微耗性能
        return attackSkill.canPlay(actor) == SkillConstants.STATE_OK;
    }
    
    private void checkAndRecacheSkill() {
        // 角色武器类型发生变化则重新获取技能进行缓存
        int weaponState = skinService.getWeaponState(actor);
        if (lastWeaponState != weaponState) {
            lastWeaponState = weaponState;
            recacheSkill();
        }
    }
    
    private Skill getSkill() {
        if (fightSkills == null || fightSkills .isEmpty()) {
            return null;
        } else {
            // 获取一个不处于冷却中的技能
            return getNotCooldown(fightSkills);
        }
    }
    
    private Skill getNotCooldown(List<Skill> skills) {
        int startIndex = FastMath.nextRandomInt(0, skills.size() - 1);
        Skill result;
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
     * 设置是否允许跟随，只有打开跟随，并且角色质量mass大于0才能进行跟随。
     * @param allowFollow 
     */
    public void setAllowFollow(boolean allowFollow) {
        this.allowFollow = allowFollow;
    }
    
    // remove20160920
//    /**
//     * 是否允许跟随攻击敌人,只有打开跟随并且角色质量(mass)大于0才能进行跟随。
//     * @return 
//     */
//    protected boolean isAllowFollow() {
//
//        return allowFollow && actorModule.isMovable();
//    }
//    
//    public float getFollowTimeMax() {
//        return followTimeMax;
//    }
//
//    public void setFollowTimeMax(float followTimeMax) {
//        this.followTimeMax = followTimeMax;
//    }

    @Override
    public void onSkillAdded(Actor actor, Skill skill) {
        recacheSkill();
    }

    @Override
    public void onSkillRemoved(Actor actor, Skill skill) {
        recacheSkill();
    }
    
    private void recacheSkill() {
        if (fightSkills != null) {
            fightSkills.clear();
        }
        fightSkills = loadAttackSkill(actor, lastWeaponState, fightSkillTags, fightSkills);
        // 重新缓存技能后，检查一次当前正在使用的技能是否适合当前的武器，如果不行则清除它，让它
        // 重新获取一个可用的。否则不应该清除当前的技能。
        if (skill != null) {
            List<Integer> weaponLimit = skill.getData().getWeaponStateLimit();
            if (weaponLimit != null && !weaponLimit.isEmpty() && !weaponLimit.contains(lastWeaponState)) {
                skill = null;
            }
        }
    }
    
    /**
     * 从角色身上获取适用于指定武器类型的攻击技能,只要适合于该武器的"攻击“技能就可以，
     * 不管是否处于冷却中。另外如果skillTypes不为null,则获取的攻击技能必须限制在这个列表之内
     * @param ac 指定的角色
     * @param weaponState 
     * @param skillTags 指定的技能类型限制
     * @param store 存放结果集，如果为null则创建一个
     * @return "攻击"技能列表,可能包含：attack.common/shot/trick/magic
     */
    private List<Skill> loadAttackSkill(Actor ac, int weaponState, long skillTags, List<Skill> store) {
        if (store == null) {
            store = new ArrayList<Skill>();
        }
        List<Skill> allSkills = skillService.getSkills(actor);
        for (Skill as : allSkills) {
            if ((skillTags & as.getData().getTags()) != 0) {
                // 武器类型的过滤,只有技能与当前武器相容才能添加
                if (as.getData().getWeaponStateLimit() == null 
                        || as.getData().getWeaponStateLimit().contains(weaponState)) {
                    store.add(as);
                }
            }
        }
        return store;
    }
}
