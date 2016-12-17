/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.action;

import com.jme3.math.FastMath;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ActionData;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.luoying.layer.network.ActorNetwork;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.network.SkillNetwork;
import name.huliqing.luoying.layer.network.SkinNetwork;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.message.StateCode;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.EntityDataListener;
import name.huliqing.luoying.object.module.SkillModule;
import name.huliqing.luoying.object.module.SkinModule;
import name.huliqing.luoying.object.skill.HitSkill;
import name.huliqing.luoying.object.skill.Skill;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 动态的角色战斗行为，该战斗行为下角色能够移动，转向跟随等。
 * @author huliqing
 */
public class DynamicFightAction extends PathFollowAction implements FightAction, EntityDataListener {
    private final SkillService skillService = Factory.get(SkillService.class);
    private final EntityService entityService = Factory.get(EntityService.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final SkinNetwork skinNetwork = Factory.get(SkinNetwork.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    private SkinModule skinModule;
    private SkillModule skillModule;
    
    // 是否允许跟随敌人
    private boolean allowFollow = true;
    // 允许跟随的时长限制,在该跟随时间内如果无法攻击目标,则停止跟随,这可以防止
    // monster一下跟随攻击目标.(单位秒)
    private float followTimeMax = 8;
    private float followTimeUsed;
    
    //  战斗技能类型
    private long fightSkillTypes;
    
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
    private Skill skill;
    private Entity enemy;
    
    // 上一次使用技能后到当前所使用的时间，如果该值小于interval则不允许NPC发技能，
    // 避免连续发技能。
    private float timeUsed;
    // 出招延迟限制,避免NPC角色出招间隔太短
    private float interval;
    
    // 最近获取到的技能缓存,注：当武器类型发生变化或者角色的技能增或减少
    // 时都应该重新获取。
    private final List<Skill> fightSkills = new ArrayList<Skill>();
    
    // 缓存技能
    private Skill waitSkill;

    @Override
    public void setData(ActionData data) {
        super.setData(data);
        followTimeMax = data.getAsFloat("followTimeMax", followTimeMax);
        allowFollow = data.getAsBoolean("allowFollow", allowFollow);
        autoTakeOffWeapon = data.getAsBoolean("autoTakeOffWeapon", autoTakeOffWeapon);
        attackIntervalAttribute = data.getAsString("attackIntervalAttribute");
        attackIntervalMax = data.getAsFloat("attackIntervalMax", attackIntervalMax);
        fightSkillTypes = skillService.convertSkillTypes(data.getAsArray("fightSkillTypes"));
    }

    @Override
    public void setActor(Entity actor) {
        super.setActor(actor); 
        skillModule = actor.getModuleManager().getModule(SkillModule.class);
        skinModule = actor.getModuleManager().getModule(SkinModule.class);
    }
    
    @Override
    public void setEnemy(Entity enemy) {
        this.enemy = enemy;
        super.setFollow(enemy.getSpatial());
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
        actor.addEntityDataListener(this);
        
        // 如果角色切换到战斗状态，则强制取出武器
        if (!skinModule.isWeaponTakeOn()) {
            skinNetwork.takeOnWeapon(actor);
        }
        
        if (waitSkill == null) {
            List<Skill> tempSkills = skillModule.getSkillWait(null);
            if (tempSkills != null && !tempSkills.isEmpty()) {
                waitSkill = tempSkills.get(0);
            }
        }
        
        // 载入战斗技能
        recacheSkill();
        
        // add20150611,处理player在点了“攻击”图标后会不定时长的延迟一段时间的问题
        timeUsed = interval;
    }
    
    @Override
    public void cleanup() {
        actor.removeEntityDataListener(this);
        
        // 效果不是太好，不再使用自动收刀。
//        if (autoTakeOffWeapon && skinService.isWeaponTakeOn(actor)) {
//            skinNetwork.takeOffWeapon(actor);
//        }

        super.cleanup();
    }
    
    @Override
    public void doLogic(float tpf) {
        timeUsed += tpf;
        
        // 攻击间隔时间限制
        if (timeUsed < interval) {
            return;
        }
        
        // 没目标
        if (enemy == null || enemy.getScene() == null) {
            if (waitSkill != null) {
                
//                skillNetwork.playSkill(actor, waitSkill, false); // remove
                entityNetwork.useObjectData(actor, waitSkill.getData().getUniqueId());
                
            }
            end();
            return;
        }
        
        
        if (skill == null) {
            skill = getSkill();
            // 并不一定能够找到合适的技能，如果没有合适的技能则返回。
            if (skill == null) {
                return;
            }
        }
        
        // 判断是否在攻击范围内
        // 如果不在攻击范围内则偿试跟随
        if (!inHitRange(skill, enemy)) {
            if (allowFollow) {
                super.doFollow(enemy.getSpatial(), tpf);
                // 如果跟随的时间达到允许的跟随限制,则不再跟随,并释放目标
                followTimeUsed += tpf;
                if (followTimeUsed >= followTimeMax) {
                    followTimeUsed = 0;
                    enemy = null;
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
                attackLimit = attackIntervalMax - attackIntervalMax * entityService.getNumberAttributeValue(actor, attackIntervalAttribute, 0).floatValue();
                attackLimit = MathUtils.clamp(attackLimit, 0, attackIntervalMax);
            }
            interval = skill.getTrueUseTime() + attackLimit;
        }
        
        // 不管攻击是否成功，都要清空技能，以便使用下一个技能。
        skill = null;
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
        
        // remove
//        return skillNetwork.playSkill(actor, skill, false);

        return entityNetwork.useObjectData(actor, skill.getData().getUniqueId());
    }
    
    /**
     * 判断是否在技能的攻击范围内
     * @param attackSkill 使用的技能
     * @param target
     * @return 
     */
    protected boolean inHitRange(Skill attackSkill, Entity target) {
        // 正常攻击类技能都应该是HitSkill,使用hitSkill的isInHitDistance来判断以优化
        // 性能，
        if (attackSkill instanceof HitSkill) {
            return ((HitSkill) attackSkill).isInHitDistance(target);
        }
        // 只有非HitSkill时才使用canPlay，这个方法稍微耗性能
        return attackSkill.checkState() == StateCode.DATA_USE;
    }
    
    /**
     * 获取一个可以用的fight技能
     * @return 
     */
    private Skill getSkill() {
        if (fightSkills.isEmpty()) {
            return null;
        }
        int startIndex = FastMath.nextRandomInt(0, fightSkills.size() - 1);
        Skill result;
        for (int i = startIndex;;) {
            result = fightSkills.get(i);
            if (!result.isCooldown() 
                    && result.isPlayableByAttributeLimit() 
                    && result.isPlayableByElCheck()) {
                return result;
            }
            i++;
            // 达到list最后则i重新从0开始。
            if (i >= fightSkills.size()) {
                i = 0;
            }
            // 绕了一个loop，如果还没有适合的技能，则返回null.
            if (i == startIndex) {
                return null;
            }
        }
    }

    @Override
    public void onDataAdded(ObjectData data, int amount) {
        if (data instanceof SkillData) {
            recacheSkill();
        }
    }

    @Override
    public void onDataRemoved(ObjectData data, int amount) {
        if (data instanceof SkillData) {
            recacheSkill();
        }
    }

    @Override
    public void onDataUsed(ObjectData data) {
         if (data instanceof SkinData) {
            if (((SkinData)data).isWeapon()) {
                recacheSkill();
            }
        }
    }
    
    private void recacheSkill() {
        fightSkills.clear();
        loadFightSkill(fightSkillTypes, fightSkills);
        // 重新缓存技能后，检查一次当前正在使用的技能是否适合当前的武器，如果不行则清除它，让它
        // 重新获取一个可用的。否则不应该清除当前的技能。
        if (skill != null) {
            if (!skill.isPlayableByWeapon()) {
                skill = null;
            }
        }
    }
    
    /**
     * 载入战斗技能
     * @param skillTypes 指定的技能类型限制
     * @param store
     * @return "攻击"技能列表,可能包含：attack.common/shot/trick/magic
     */
    private List<Skill> loadFightSkill(long skillTypes, List<Skill> store) {
        List<Skill> allSkills = skillModule.getSkills();
        for (Skill fightSkill : allSkills) {
            if ((skillTypes & fightSkill.getData().getTypes()) != 0) {
                // 武器类型的过滤,只有技能与当前武器相容才能添加
                if (fightSkill.isPlayableByWeapon()) {
                    store.add(fightSkill);
                }
            }
        }
        return store;
    }

}
