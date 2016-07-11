/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.action.impl;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.object.action.AbstractAction;
import name.huliqing.fighter.object.action.IdleAction;
import name.huliqing.fighter.data.ActionData;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.enums.SkillType;
import name.huliqing.fighter.game.network.SkillNetwork;
import name.huliqing.fighter.game.service.SkillService;

/**
 * 静态的IDLE行为，对于不能动，静止的所有物体的行为。
 * @author huliqing
 */
public class IdleStaticAction extends AbstractAction implements IdleAction {
    private final SkillService skillService = Factory.get(SkillService.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);

    // 缓存技能id
    private String waitSkillId;
    
    public IdleStaticAction() {
        super();
    }

    @Override
    public void initData(ActionData data) {
        super.initData(data);
    }
    
    @Override
    protected void doInit() {
        SkillData waitSkill = skillService.getSkill(actor, SkillType.wait);
        if (waitSkill != null) {
            waitSkillId = waitSkill.getId();
        }
    }
    
    @Override
    protected void doLogic(float tpf) {
        if (!actor.isWaiting()) {
            if (waitSkillId != null) {
                skillNetwork.playSkill(actor, waitSkillId, false);
            }
            end();
        }
    }

    
}
