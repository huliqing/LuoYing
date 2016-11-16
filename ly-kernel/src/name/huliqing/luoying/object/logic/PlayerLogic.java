/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.logic;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.object.action.FightAction;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.layer.service.ActionService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 只用于player角色的逻辑,不会有idle行为，因为不让玩角角色在停下来的时候或
 * 没有目标的时候做idle行为
 * @author huliqing
 */
public class PlayerLogic extends AbstractLogic {
    private final ActionService actionService = Factory.get(ActionService.class);
    
    private FightAction fightAction;
    
    // ---- inner
    private float viewDistanceSquared;
    
    @Override
    public void setData(LogicData data) {
        super.setData(data); 
        fightAction = (FightAction) Loader.load(data.getAsString("fightAction"));
    }

    @Override
    public void initialize() {
        super.initialize();
        if (viewAttribute != null) {
            viewDistanceSquared = viewAttribute.floatValue() * viewAttribute.floatValue();
        }
    }
    
    @Override
    protected void doLogic(float tpf) {
        if (targetAttribute == null || viewAttribute == null) {
            return;
        }
        
        // 如果玩家正在控制走路，则不执行逻辑
        if (actionService.isPlayingRun(actor)) {
            return;
        }
        
        // 目标不是角色
        Entity target = getTarget();
        if (!(target instanceof Actor)) {
            return;
        }
        
        // 已经从场景移除
        if (target.getScene() == null) {
            return;
        }
        
        // 目标在视线之外
        if (target.getSpatial().getWorldTranslation().distanceSquared(actor.getSpatial().getWorldTranslation()) > viewDistanceSquared) {
            return;
        }
        
        // 非敌人
        if (!isEnemy(target)) {
            return;
        }
        
        // <<<战斗开始>>>
        fightAction.setEnemy(target);
        actionService.playAction(actor, fightAction);
        
    }
    
}
