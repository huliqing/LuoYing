/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actorlogic;

import name.huliqing.core.Factory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.action.Action;
import name.huliqing.core.data.ActorLogicData;
import name.huliqing.core.game.service.ActionService;
import name.huliqing.core.object.DataProcessor;
import name.huliqing.core.object.IntervalLogic;

/**
 * 角色逻辑
 * @author huliqing
 * @param <T>
 */
public abstract class ActorLogic<T extends ActorLogicData> extends IntervalLogic implements DataProcessor<T>{
    private final ActionService actionService = Factory.get(ActionService.class);
    
    protected T data;
    
    /**
     * 运行当前逻辑的角色.
     */
    protected Actor actor;

    @Override
    public void setData(T data) {
        this.data = data;
        interval = data.getAsFloat("interval", interval);
    }

    @Override
    public T getData() {
        return data;
    }
    
    /**
     * 获取执行逻辑的角色.
     * @return 
     */
    public Actor getActor() {
        return actor;
    }
    
    /**
     * 设置执行逻辑的角色。
     * @param self 
     */
    public void setActor(Actor self) {
        this.actor = self;
    }
    
    /**
     * 执行行为
     * @param action 
     */
    protected void playAction(Action action) {
        actionService.playAction(actor, action);
    }
    
}
