/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.action.impl;

import com.jme3.math.FastMath;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.object.action.AbstractAction;
import name.huliqing.fighter.object.action.IdleAction;
import name.huliqing.fighter.data.ActionData;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.enums.SkillType;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.network.SkillNetwork;
import name.huliqing.fighter.game.service.SkillService;

/**
 * 适合于生物角色的空闲行为，角色每隔一段时间就会随机执行一个idle动作,在idle动作执行
 * 完毕的间隔期则执行wait循环动作。在此期间角色不会移动位置。
 * @author huliqing
 */
public class IdleDynamicAction extends AbstractAction implements IdleAction {
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    
    //  IDLE行为的最大时间间隔,单位秒
    private float intervalMax = 7.0f;
    private float intervalMin = 3.0f;
    
    // ---- 内部参数
    private List<SkillData> skills = new ArrayList<SkillData>();
    private long lastGetSkillTime;
    private float interval = 4.0f;
    private float intervalUsed;
    
    // 缓存技能id
    private String waitSkillId;
    
    public IdleDynamicAction() {
        super();
    }

    public IdleDynamicAction(ActionData data) {
        super(data);
        intervalMax = data.getAsFloat("intervalMax", intervalMax);
        intervalMin = data.getAsFloat("intervalMin", intervalMin);
    }

    @Override
    protected void doInit() {
        SkillData waitSkill = skillService.getSkill(actor, SkillType.wait);
        if (waitSkill != null) {
            waitSkillId = waitSkill.getId();
        }
    }
    
    @Override
    public void doLogic(float tpf) {
        intervalUsed += tpf;
        
        if (intervalUsed < interval) {
            // 在idle动作执行的间隔要执行一个wait动作，使角色看起来不会像是完全静止的。
            if (!skillService.isPlayingSkill(actor)) {
                // 注：wait可能不是循环的，所以需要判断
                if (!skillService.isWaiting(actor) && waitSkillId != null) {
                    skillNetwork.playSkill(actor, waitSkillId, false);
                }
            }
            return;
        }
        
        SkillData idle = getIdleSkill();
        if (idle == null) {
            return;
        }
        skillNetwork.playSkill(actor, idle.getId(), false);
        
        intervalUsed = 0;
        interval = (intervalMax - intervalMin) * FastMath.nextRandomFloat() + intervalMin;
        
//        interval += idle.getTrueUseTime(); // remove
        interval += skillService.getSkillTrueUseTime(actor, idle);
    }
    
    private SkillData getIdleSkill() {
        if (actorNetwork.isSkillUpdated(actor, lastGetSkillTime)) {
            lastGetSkillTime = Common.getGameTime();
            skills.clear();
            List<SkillData> all = skillService.getSkill(actor);
            for (SkillData sd : all) {
                if (sd.getSkillType() == SkillType.idle) {
                    skills.add(sd);
                }
            }
        }
        if (skills == null || skills.isEmpty()) {
            return null;
        }
        return skills.get(FastMath.nextRandomInt(0, skills.size() - 1));
    }

  
}
