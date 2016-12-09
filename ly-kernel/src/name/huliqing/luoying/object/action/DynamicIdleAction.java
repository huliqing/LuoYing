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
import name.huliqing.luoying.layer.network.SkillNetwork;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.object.entity.EntityDataListener;
import name.huliqing.luoying.object.module.SkillModule;
import name.huliqing.luoying.object.skill.Skill;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 适合于生物角色的空闲行为，角色每隔一段时间就会随机执行一个idle动作,在idle动作执行
 * 完毕的间隔期则执行wait循环动作。在此期间角色不会移动位置。
 * @author huliqing
 */
public class DynamicIdleAction extends AbstractAction implements IdleAction, EntityDataListener {
//    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private SkillModule skillModule;
    
    //  IDLE行为的最大时间间隔,单位秒
    private float intervalMax = 7.0f;
    private float intervalMin = 3.0f;
    
    // ---- 内部参数
    private final List<Skill> idleSkills = new ArrayList<Skill>();
    private float interval = 4.0f;
    private float intervalUsed;
    
    // 缓存技能id
    private Skill waitSkill;
    
    public DynamicIdleAction() {
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
        skillModule = actor.getModuleManager().getModule(SkillModule.class);
        actor.addEntityDataListener(this);
        if (waitSkill == null) {
            List<Skill> waitSkills = skillModule.getSkillWait(null);
            if (waitSkills != null && !waitSkills.isEmpty()) {
                waitSkill = waitSkills.get(0);
            }
        }
        
        recacheIdleSkills();
    }

    @Override
    public void cleanup() {
        actor.removeEntityDataListener(this);
        super.cleanup();
    }
    
    @Override
    public void doLogic(float tpf) {
        intervalUsed += tpf;
        
        if (intervalUsed < interval) {
            // 在idle动作执行的间隔要执行一个wait动作，使角色看起来不会像是完全静止的。
            if (!skillService.isPlayingSkill(actor)) {
                // 注：wait可能不是循环的，所以需要判断
                if (!skillModule.isWaiting() && waitSkill != null) {
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
        if (idleSkills.isEmpty()) {
            return null;
        }
        return idleSkills.get(FastMath.nextRandomInt(0, idleSkills.size() - 1));
    }

    @Override
    public void onDataAdded(ObjectData data, int amount) {
        if (data instanceof SkillData) {
            recacheIdleSkills();
        }
    }

    @Override
    public void onDataRemoved(ObjectData data, int amount) {
        if (data instanceof SkillData) {
            recacheIdleSkills();
        }
    }

    @Override
    public void onDataUsed(ObjectData data) {
        // ignore
    }

    // 重新缓存技能
    private void recacheIdleSkills() {
        idleSkills.clear();
        skillModule.getSkillIdle(idleSkills);
    }
    
}
