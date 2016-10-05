/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.logic;

import name.huliqing.ly.Factory;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.action.FightAction;
import name.huliqing.ly.data.LogicData;
import name.huliqing.ly.layer.service.ActionService;
import name.huliqing.ly.layer.service.ActorService;
import name.huliqing.ly.layer.service.PlayService;

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
        
        Actor t = actorService.getTarget(actor);
        if (t != null && !actorService.isDead(t) 
                
                //  remove20160328 -> remove20160217,不再判断是否为敌人，是否可攻击目标以后交由hitChecker判断
                // 放开这个判断可允许玩家控制角色攻击同伴，只要技能的hitChecker设置即可。
                && actorService.isEnemy(t, actor) 
                
                && playService.isInScene(t)) {
            fightAction.setEnemy(t);
            actionService.playAction(actor, fightAction);
        }
    }
    
}
