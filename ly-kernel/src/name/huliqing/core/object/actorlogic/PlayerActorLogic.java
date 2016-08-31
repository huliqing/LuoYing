/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actorlogic;

import name.huliqing.core.Factory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.action.FightAction;
import name.huliqing.core.data.ActorLogicData;
import name.huliqing.core.mvc.service.ActionService;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.object.Loader;

/**
 * 只用于player角色的逻辑,不会有idle行为，因为不让玩角角色在停下来的时候或
 * 没有目标的时候做idle行为
 * @author huliqing
 * @param <T>
 */
public class PlayerActorLogic<T extends ActorLogicData> extends ActorLogic<T> {
    private final ActionService actionService = Factory.get(ActionService.class);;
    private final ActorService actorService = Factory.get(ActorService.class);;
    
    protected FightAction fightAction;
    
    @Override
    public void setData(T data) {
        super.setData(data); 
        fightAction = (FightAction) Loader.loadAction(data.getAsString("fightAction"));
    }

    public FightAction getFightAction() {
        return fightAction;
    }

    public void setFightAction(FightAction fightAction) {
        this.fightAction = fightAction;
    }
    
    @Override
    protected void doLogic(float tpf) {
        // 如果玩家正在控制走路，则不执行逻辑
        if (actionService.isPlayingRun(actor)) {
            return;
        }
        
        Actor t = actorService.getTarget(actor);
        
        if (t != null && !actorService.isDead(t) 
                && t.getSpatial().getWorldTranslation().distance(actor.getSpatial().getWorldTranslation()) < actorService.getViewDistance(actor)
                
                // remove20160328 -> remove20160217,不再判断是否为敌人，是否可攻击目标以后交由hitChecker判断
                // 放开这个判断可允许玩家控制角色攻击同伴，只要技能的hitChecker设置即可。
                &&  actorService.isEnemy(t, actor)
                
                ) {
            fightAction.setEnemy(t);
            actionService.playAction(actor, fightAction);
        }
    }
    
}
