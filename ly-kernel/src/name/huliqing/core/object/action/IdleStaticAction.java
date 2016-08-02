/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.action;

import name.huliqing.core.Factory;
import name.huliqing.core.object.action.AbstractAction;
import name.huliqing.core.object.action.IdleAction;
import name.huliqing.core.data.ActionData;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.mvc.network.SkillNetwork;
import name.huliqing.core.mvc.service.SkillService;

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
    public void setData(ActionData data) {
        super.setData(data);
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
