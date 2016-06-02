/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.logic;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.LogicData;
import name.huliqing.fighter.game.service.ActionService;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.object.action.Action;

/**
 *
 * @author huliqing
 */
public class IdleLogic extends ActorLogic {
    
    private final ActionService actionService = Factory.get(ActionService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    
    // 普通的idle行为，在原地不动执行idle动作。
    private Action idleSimpleAction;
    // 巡逻行为，在一个地方走来走去
    private Action idlePatrolAction;

    public IdleLogic(LogicData data) {
        super(data);
        this.idleSimpleAction = actionService.loadAction(data.getAttribute("idleSimpleAction"));
        this.idlePatrolAction = actionService.loadAction(data.getAttribute("idlePatrolAction"));
    }

    @Override
    protected void doLogic(float tpf) {
        // 只有空闲时才执行IDLE：
        // 1. 在没有跟随目标时执行巡逻行为，可走来走去
        // 2. 在有跟随目标时则只执行普通idle行为，不可走来走去。
        Action current = actionService.getPlayingAction(self);
        if (current == null) {
            if (self.getData().getFollowTarget() > 0) {
                playAction(idleSimpleAction);
            } else {
                playAction(idlePatrolAction);
            }
        }
    }
    
}
