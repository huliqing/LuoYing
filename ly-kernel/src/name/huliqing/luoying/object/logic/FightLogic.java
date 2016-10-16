/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.logic;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.object.action.FightAction;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.layer.service.ActionService;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 战斗逻辑
 * @author huliqing
 * @param <T>
 */
public class FightLogic<T extends LogicData> extends Logic<T> {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final ActionService actionService = Factory.get(ActionService.class);
    private  FightAction fightAction;
    
    @Override
    public void setData(T data) {
        super.setData(data); 
        fightAction = (FightAction) actionService.loadAction(data.getAsString("fightAction"));
    }
    
    @Override
    protected void doLogic(float tpf) {
        
        Entity t = actorService.getTarget(actor);
        if (t != null && !actorService.isDead(t) 
                && actorService.isEnemy(t, actor) 
                && t.getScene() != null) {
            
            fightAction.setEnemy(t);
            actionService.playAction(actor, fightAction);
        }
    }
    
}
