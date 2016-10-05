/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.logic;

import com.jme3.math.Vector3f;
import name.huliqing.ly.object.action.RunAction;
import name.huliqing.ly.data.LogicData;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.action.Action;
import name.huliqing.ly.object.action.FightAction;
import name.huliqing.ly.object.action.FollowAction;
import name.huliqing.ly.object.module.ActionModule;

/**
 * 必须两个行为：FightAction, RunAction
 * 角色逻辑：
 * 1.受到敌人攻击时会和敌人战斗(不会主动查找敌人)
 * 2.没有敌人时就朝着目标前进,需要为逻辑指定一个目标地点，默认为Vector3f.ZERO
 * @author huliqing
 * @param <T>
 */
public class PositionLogic<T extends LogicData> extends Logic<T> {
    private ActionModule actionModule;
    
    // 允许走到的最近距离
    private float nearest = 5;
    
    // ---- inner
    protected RunAction runAction;
    
    @Override
    public void setData(T data) {
        super.setData(data); 
        runAction = (RunAction) Loader.load(data.getAsString("runAction"));
        Vector3f position = data.getAsVector3f("position");
        if (position != null) {
            runAction.setPosition(position);
        }
        nearest = data.getAsFloat("nearest", nearest);
    }

    @Override
    public void initialize() {
        super.initialize();
        actionModule = actor.getModule(ActionModule.class);
        runAction.setActor(actor);
    }
    
    public void setPosition(Vector3f position) {
        runAction.setPosition(position);
    }
    
    /**
     * 设置允许走到目标位置的最近距离，如果设置为0，则将走到精确的目标位置
     * @param distance 
     */
    public void setNearestDistance(float distance) {
        runAction.setNearest(distance);
    }

    private Action tempAction;
    @Override
    protected void doLogic(float tpf) {
        tempAction = actionModule.getAction();
        if ((tempAction  instanceof FightAction) || (tempAction  instanceof FollowAction)) {
            return;
        }
        
        if (!runAction.isEndPosition()) {
            actionModule.startAction(runAction);
        }
    }
    
}
