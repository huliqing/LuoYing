/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.logic;

import name.huliqing.core.Factory;
import name.huliqing.core.data.LogicData;
import name.huliqing.core.mvc.service.ActionService;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.object.action.Action;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class IdleLogic<T extends LogicData> extends Logic<T> {
    private final ActionService actionService = Factory.get(ActionService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    
    // 普通的idle行为，在原地不动执行idle动作。
    private Action idleSimpleAction;
    // 巡逻行为，在一个地方走来走去
    private Action idlePatrolAction;

    @Override
    public void setData(T data) {
        super.setData(data); 
        this.idleSimpleAction = actionService.loadAction(data.getAsString("idleSimpleAction"));
        this.idlePatrolAction = actionService.loadAction(data.getAsString("idlePatrolAction"));
    }

    @Override
    protected void doLogic(float tpf) {
        // 只有空闲时才执行IDLE：
        // 1. 在没有跟随目标时执行巡逻行为，可走来走去
        // 2. 在有跟随目标时则只执行普通idle行为，不可走来走去。
        Action current = actionService.getPlayingAction(actor);
        if (current == null) {
            if (actorService.getFollow(actor) > 0) {
                actionService.playAction(actor, idleSimpleAction);
            } else {
                actionService.playAction(actor, idlePatrolAction);
            }
        }
    }
    
}
