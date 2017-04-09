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
package name.huliqing.luoying.object.logic;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.action.Action;
import name.huliqing.luoying.object.el.SBooleanEl;
import name.huliqing.luoying.object.module.ActionModule;

/**
 *
 * @author huliqing
 */
public class IdleLogic extends AbstractLogic {
    private final ElService elService = Factory.get(ElService.class);
    
    // 普通的idle行为，在原地不动执行idle动作。
    private Action idleSimpleAction;
    // 巡逻行为，在一个地方走来走去
    private Action idlePatrolAction;
    
    // 这个EL用于判断在什么情况下当前逻辑可以使用Patrol的idle行为
    private SBooleanEl patrolCheckEl;
    
    // ---- inner
    private ActionModule actionModule;

    @Override
    public void setData(LogicData data) {
        super.setData(data); 
        this.idleSimpleAction = Loader.load(data.getAsString("idleSimpleAction"));
        this.idlePatrolAction = Loader.load(data.getAsString("idlePatrolAction"));
        String tempEl = data.getAsString("patrolCheckEl");
        if (tempEl != null) {
            this.patrolCheckEl = elService.createSBooleanEl(tempEl);
        }
    }
    
    @Override
    public void initialize() {
        super.initialize();
        actionModule = actor.getModule(ActionModule.class);
        if (patrolCheckEl != null) {
            patrolCheckEl.setSource(actor.getAttributeManager());
        }
    }
    
    @Override
    protected void doLogic(float tpf) {
        // 只有空闲时才执行IDLE：
        // 1. 在没有跟随目标时执行巡逻行为，可走来走去
        // 2. 在有跟随目标时则只执行普通idle行为，不可走来走去。
        Action current = actionModule.getAction();
        if (current == null) {
            if (patrolCheckEl != null && patrolCheckEl.getValue()) {
                actionModule.startAction(idlePatrolAction);
            } else {
                actionModule.startAction(idleSimpleAction);
            }
        }
    }
    
}
