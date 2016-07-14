/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.logic;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.action.FightAction;
import name.huliqing.fighter.data.LogicData;
import name.huliqing.fighter.game.service.ActionService;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.PlayService;

/**
 * 战斗逻辑
 * @author huliqing
 * @param <T>
 */
public class FightLogic<T extends LogicData> extends ActorLogic<T> {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final ActionService actionService = Factory.get(ActionService.class);
    private  FightAction fightAction;
    
    @Override
    public void setData(T data) {
        super.setData(data); 
        fightAction = (FightAction) actionService.loadAction(data.getAttribute("fightAction"));
    }
    
    @Override
    protected void doLogic(float tpf) {
        
        Actor t = actorService.getTarget(actor);
        if (t != null && !t.isDead() 
                
                //  remove20160328 -> remove20160217,不再判断是否为敌人，是否可攻击目标以后交由hitChecker判断
                // 放开这个判断可允许玩家控制角色攻击同伴，只要技能的hitChecker设置即可。
                && t.isEnemy(actor) 
                
                && playService.isInScene(t)) {
            fightAction.setEnemy(t);
            playAction(fightAction);
        }
    }
    
}
