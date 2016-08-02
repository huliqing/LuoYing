/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actorlogic;

import com.jme3.math.Vector3f;
import name.huliqing.core.Factory;
import name.huliqing.core.object.action.RunAction;
import name.huliqing.core.data.ActorLogicData;
import name.huliqing.core.mvc.service.ActionService;
import name.huliqing.core.mvc.service.ActorService;

/**
 * 必须两个行为：FightAction, RunAction
 * 角色逻辑：
 * 1.受到敌人攻击时会和敌人战斗(不会主动查找敌人)
 * 2.没有敌人时就朝着目标前进,需要为逻辑指定一个目标地点，默认为Vector3f.ZERO
 * @author huliqing
 * @param <T>
 */
public class PositionActorLogic<T extends ActorLogicData> extends ActorLogic<T> {
    private final ActionService actionService = Factory.get(ActionService.class);;
    
    protected RunAction runAction;
    
    // 目标位置
    private final Vector3f position = new Vector3f();
    // 允许走到的最近距离
    private float nearest = 5;
    
    @Override
    public void setData(T data) {
        super.setData(data); 
        runAction = (RunAction) actionService.loadAction(data.getAttribute("runAction"));
        position.set(data.getAsVector3f("position", position));
        nearest = data.getAsFloat("nearest", nearest);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

//    public float getNearest() {
//        return nearest;
//    }
//
//    public void setNearest(float nearest) {
//        this.nearest = nearest;
//    }
    
    /**
     * 设置允许走到目标位置的最近距离，如果设置为0，则将走到精确的目标位置
     * @param distance 
     */
    public void setNearestDistance(float distance) {
        runAction.setNearest(distance);
    }

    @Override
    protected void doLogic(float tpf) {
        if (actionService.isPlayingFight(actor) 
                || actionService.isPlayingFollow(actor)) {
            return;
        }
        
        runAction.setNearest(nearest);
        runAction.setPosition(position);
        if (!runAction.isEndPosition()) {
            playAction(runAction);
        }
    }
    
}
