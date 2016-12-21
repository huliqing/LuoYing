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
package name.huliqing.luoying.layer.service;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.action.Action;
import name.huliqing.luoying.object.action.FightAction;
import name.huliqing.luoying.object.action.FollowAction;
import name.huliqing.luoying.object.action.RunAction;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ActionModule;

/**
 *
 * @author huliqing
 */
public class ActionServiceImpl implements ActionService {

    private SkillService skillService;
    
    @Override
    public void inject() {
        skillService = Factory.get(SkillService.class);
    }
    
    @Override
    public void playAction(Entity actor, Action action) {
        getActionModule(actor).startAction(action);
    }

    @Override
    public void playRun(Entity actor, Vector3f pos) {
        ActionModule module = getActionModule(actor);
        RunAction ra = module.getDefRunAction();
        // 如果角色没有指定默认“跑路”行为，则为角色创建一个。
        if (ra == null) {
            ra = (RunAction) Loader.load(IdConstants.SYS_ACTION_SIMPLE_RUN);
            ra.setActor(actor);
            module.setDefRunAction(ra);
        }
        
        ra.setPosition(pos);
        playAction(actor, ra);
    }

    /**
     * 执行行为：使用目标与指定角色战斗。<br>
     * 该方法作为玩家控制角色时使用，不建议在logic和action内部调用该方法。
     * @param target 战斗目标
     */
    @Override
    public void playFight(Entity actor, Entity target, String skillId) {
        ActionModule module = getActionModule(actor);
        FightAction fa = module.getDefFightAction();
        // 如果角色没有指定特定的战斗行为，则为角色创建一个。
        if (fa == null) {
            fa = (FightAction) Loader.load(IdConstants.SYS_ACTION_SIMPLE_FIGHT);
            fa.setActor(actor);
            module.setDefFightAction(fa);
        }
        
        fa.setEnemy(target);
        if (skillId != null) {
            fa.setSkill(skillService.getSkill(actor, skillId));
        }
        playAction(actor, fa);
    }

    @Override
    public boolean isPlayingFight(Entity actor) {
        ActionModule control = getActionModule(actor);
        Action current = control.getAction();
        return current instanceof FightAction;
    }

    @Override
    public boolean isPlayingRun(Entity actor) {
        Action current = getActionModule(actor).getAction();
        return current instanceof RunAction;
    }

    @Override
    public boolean isPlayingFollow(Entity actor) {
        Action current = getActionModule(actor).getAction();
        return current instanceof FollowAction;
    }

    @Override
    public Action getPlayingAction(Entity actor) {
        return getActionModule(actor).getAction();
    }
    
    private ActionModule getActionModule(Entity actor) {
        return actor.getModuleManager().getModule(ActionModule.class);
    }
}
