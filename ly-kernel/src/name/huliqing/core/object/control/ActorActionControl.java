/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import name.huliqing.core.data.ControlData;
import name.huliqing.core.object.action.Action;
import name.huliqing.core.object.action.FightAction;
import name.huliqing.core.object.action.RunAction;

/**
 * 角色行为控制器
 * @author huliqing
 * @param <T>
 */
public class ActorActionControl<T extends ControlData> extends ActorControl<T> {
    
    // 两个默认行为,当角色接收玩家控制时需要这两个默认行为
    // see ActionServcice.playRun,playFight
    private RunAction defRunAction;
    private FightAction defFightAction;
    
    // 当前正在执行的行为逻辑
    private Action current;

    @Override
    public void cleanup() {
        
        // remove20151231,暂不支持cleanup
//        if (current != null) {
//            current.cleanup();
//            current = null;
//        }
        
        super.cleanup(); 
    }
    
    @Override
    public void actorUpdate(float tpf) {
        if (current != null) {
            if (current.isEnd()) {
                current.cleanup();
                current = null;
            } else {
                current.update(tpf);
            }
        }
    }

    @Override
    public void actorRender(RenderManager rm, ViewPort vp) {}
    
     /**
     * 执行行为逻辑，如果当前没有正在执行的逻辑，则立即执行．否则偿试打断正在执
     * 行的逻辑，如果打断成功，则执行新逻辑，否则直接返回．
     * @param action 当为null时，将打断当前行为。
     */
    public void startAction(Action action) {
        if (current == action) {
            return;
        }
        
        if (current != null) {
            current.cleanup();
        }
        
        current = action;
        if (current != null) {
            current.start();
        }
    }
    
    /**
     * 获取当前正在执行的行为,如果没有则返回null.
     * @return 
     */
    public Action getAction() {
        return current;
    }

    public RunAction getDefRunAction() {
        return defRunAction;
    }

    public void setDefRunAction(RunAction defRunAction) {
        this.defRunAction = defRunAction;
    }

    public FightAction getDefFightAction() {
        return defFightAction;
    }

    public void setDefFightAction(FightAction defFightAction) {
        this.defFightAction = defFightAction;
    }
    
}
