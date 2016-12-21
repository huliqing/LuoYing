/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.object.module;

import com.jme3.scene.control.Control;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.object.action.Action;
import name.huliqing.luoying.object.action.FightAction;
import name.huliqing.luoying.object.action.RunAction;
import name.huliqing.luoying.object.attribute.BooleanAttribute;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 角色行为控制器
 * @author huliqing
 */
public class ActionModule extends AbstractModule {
    
    // 用于判断角色是否死亡的属性
    private String bindDeadAttribute;
    
    // ---- inner
    // 两个默认行为,当角色接收玩家控制时需要这两个默认行为
    // see ActionServcice.playRun,playFight
    private RunAction defRunAction;
    private FightAction defFightAction;
    
    // 当前正在执行的行为逻辑
    private Action currentAction;
    
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
    public void initialize(Entity entity) {
        super.initialize(entity);
        deadAttribute = getAttribute(bindDeadAttribute, BooleanAttribute.class);
        
        updateControl = new AdapterControl() {
            @Override
            public final void update(float tpf) {
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
        
        if (currentAction != null) {
            if (currentAction.isEnd()) {
                currentAction.cleanup();
                currentAction = null;
            } else {
                currentAction.update(tpf);
            }
        }
    }

    @Override
    public void cleanup() {
        if (currentAction != null) {
            currentAction.cleanup();
            currentAction = null;
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
        if (currentAction == action) {
            return;
        }
        
        if (currentAction != null) {
            currentAction.cleanup();
        }
        
        currentAction = action;
        if (currentAction != null) {
            currentAction.setActor(entity);
            currentAction.initialize();
        }
    }
    
    /**
     * 获取当前正在执行的行为,如果没有则返回null.
     * @return 
     */
    public Action getAction() {
        return currentAction;
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
