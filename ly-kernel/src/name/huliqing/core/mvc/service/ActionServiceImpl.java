/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import com.jme3.math.Vector3f;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.action.Action;
import name.huliqing.core.object.action.FightAction;
import name.huliqing.core.object.action.FollowAction;
import name.huliqing.core.object.action.RunAction;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.ActionModule;

/**
 *
 * @author huliqing
 */
public class ActionServiceImpl implements ActionService {

    private ActorService actorService;
    private SkinService skinService;
    private SkillService skillService;
    
    @Override
    public void inject() {
        actorService = Factory.get(ActorService.class);
        skinService = Factory.get(SkinService.class);
        skillService = Factory.get(SkillService.class);
    }

    @Override
    public Action loadAction(String actionId) {
        return Loader.loadAction(actionId);
    }
    
    @Override
    public void playAction(Actor actor, Action action) {
        // action可能为null. 当action=null的时候会结束角色当前的行为
        if (action != null) {
            action.setActor(actor);
        }
        getActionModule(actor).startAction(action);
    }

    @Override
    public void playRun(Actor actor, Vector3f pos) {
        ActionModule module = getActionModule(actor);
        RunAction ra = module.getDefRunAction();
        // 如果角色没有指定默认“跑路”行为，则为角色创建一个。
        if (ra == null) {
            ra = (RunAction) Loader.loadAction(IdConstants.ACTION_RUN_SIMPLE);
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
    public void playFight(Actor actor, Actor target, String skillId) {
        ActionModule module = getActionModule(actor);
        FightAction fa = module.getDefFightAction();
        // 如果角色没有指定特定的战斗行为，则为角色创建一个。
        if (fa == null) {
            if (actorService.getMass(actor) <= 0) {
                fa = (FightAction) Loader.loadAction(IdConstants.ACTION_FIGHT_STATIC);
            } else {
                fa = (FightAction) Loader.loadAction(IdConstants.ACTION_FIGHT_FOR_PLAYER);
            }
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
    public boolean isPlayingFight(Actor actor) {
        ActionModule control = getActionModule(actor);
        Action current = control.getAction();
        return current instanceof FightAction;
    }

    @Override
    public boolean isPlayingRun(Actor actor) {
        Action current = getActionModule(actor).getAction();
        return current instanceof RunAction;
    }

    @Override
    public boolean isPlayingFollow(Actor actor) {
        Action current = getActionModule(actor).getAction();
        return current instanceof FollowAction;
    }

    @Override
    public Action getPlayingAction(Actor actor) {
        return getActionModule(actor).getAction();
    }
    
    private ActionModule getActionModule(Actor actor) {
        return actor.getModule(ActionModule.class);
    }
}
