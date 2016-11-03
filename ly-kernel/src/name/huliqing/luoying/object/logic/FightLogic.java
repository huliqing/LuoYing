/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.logic;

import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.object.action.FightAction;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.layer.service.ActionService;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 战斗逻辑
 * @author huliqing
 */
public class FightLogic extends AbstractLogic {
//    private static final Logger LOG = Logger.getLogger(FightLogic.class.getName());
    
    private final ActionService actionService = Factory.get(ActionService.class);
    private  FightAction fightAction;
    
    // ---- inner
    
    @Override
    public void setData(LogicData data) {
        super.setData(data); 
        fightAction = (FightAction) actionService.loadAction(data.getAsString("fightAction"));
    }

    @Override
    public void initialize() {
        super.initialize();
    }
    
    @Override
    protected void doLogic(float tpf) {
        // remove20161102
//        Entity t = actorService.getTarget(actor);
//        if (t != null && !actorService.isDead(t) 
//                && actorService.isEnemy(t, actor) 
//                && t.getScene() != null) {
//            
//            fightAction.setEnemy(t);
//            actionService.playAction(actor, fightAction);
//        }
        
        Entity target = getTarget();
        
        // 只有Actor才有属性
        if (!(target instanceof Actor)) {
            return;
        }
        if (target.getScene() == null) {
            return;
        }
        if (isEnemy(target)) {
            fightAction.setEnemy(target);
            actionService.playAction(actor, fightAction);
        }
    }
    
}
