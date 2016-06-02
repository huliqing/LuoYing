/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.logic;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.action.Action;
import name.huliqing.fighter.data.LogicData;
import name.huliqing.fighter.game.service.ActionService;
import name.huliqing.fighter.object.IntervalLogic;

/**
 * 角色逻辑
 * @author huliqing
 */
public abstract class ActorLogic extends IntervalLogic {
    private final ActionService actionService = Factory.get(ActionService.class);
    
    private LogicData data;
    /**
     * 运行当前逻辑的角色.
     */
    protected Actor self;
    
    public ActorLogic() {
        super();
    }
    
    public ActorLogic(LogicData data) {
        super();
        this.data = data;
        interval = data.getAsFloat("interval", interval);
    }

    public LogicData getData() {
        return data;
    }
    
    public Actor getSelf() {
        return self;
    }
        
    public void setSelf(Actor self) {
        this.self = self;
    }
    
    /**
     * 执行行为
     * @param action 
     */
    protected void playAction(Action action) {
        actionService.playAction(self, action);
    }
    
}
