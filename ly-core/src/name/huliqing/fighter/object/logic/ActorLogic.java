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
import name.huliqing.fighter.object.DataProcessor;
import name.huliqing.fighter.object.IntervalLogic;

/**
 * 角色逻辑
 * @author huliqing
 * @param <T>
 */
public abstract class ActorLogic<T extends LogicData> extends IntervalLogic implements DataProcessor<T>{
    private final ActionService actionService = Factory.get(ActionService.class);
    
    private T data;
    /**
     * 运行当前逻辑的角色.
     */
    protected Actor self;

    @Override
    public void setData(T data) {
        this.data = data;
        interval = data.getAsFloat("interval", interval);
    }

    @Override
    public T getData() {
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
