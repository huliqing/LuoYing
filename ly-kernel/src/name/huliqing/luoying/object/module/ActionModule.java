/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.module;

import com.jme3.scene.control.Control;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.object.action.Action;
import name.huliqing.luoying.object.action.FightAction;
import name.huliqing.luoying.object.action.RunAction;
import name.huliqing.luoying.object.attribute.BooleanAttribute;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 角色行为控制器
 * @author huliqing
 */
public class ActionModule extends AbstractModule<ModuleData> {
    private final ActorService actorService = Factory.get(ActorService.class);
    
    // 用于判断角色是否死亡的属性
    private String bindDeadAttribute;
    
    // ---- inner
    // 两个默认行为,当角色接收玩家控制时需要这两个默认行为
    // see ActionServcice.playRun,playFight
    private RunAction defRunAction;
    private FightAction defFightAction;
    
    // 当前正在执行的行为逻辑
    private Action current;
    
    private Control updateControl;
    
    private BooleanAttribute deadAttribute;

    @Override
    public void setData(ModuleData data) {
        super.setData(data); 
        bindDeadAttribute = data.getAsString("bindDeadAttribute");
    }
    
    @Override
    public void updateDatas() {
    }

    @Override
    public void initialize(Entity actor) {
        super.initialize(actor);
        if (bindDeadAttribute != null) {
            deadAttribute = actor.getAttributeManager().getAttribute(bindDeadAttribute, BooleanAttribute.class);
        }
        
        updateControl = new AdapterControl() {
            @Override
            public void update(float tpf) {
                actionUpdate(tpf);
            }
        };
        this.entity.getSpatial().addControl(updateControl);
    }
    
    // 更新action逻辑
    private void actionUpdate(float tpf) {
        if (deadAttribute != null && deadAttribute.getValue()) {
            return;
        }
        
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
    public void cleanup() {
        if (current != null) {
            current.cleanup();
            current = null;
        }
        defRunAction = null;
        defFightAction = null;

        if (updateControl != null) {
            entity.getSpatial().removeControl(updateControl);
        }
        super.cleanup(); 
    }
    
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
            current.initialize();
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
