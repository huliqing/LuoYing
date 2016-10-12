/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import com.jme3.math.Vector3f;
import name.huliqing.ly.Factory;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.action.Action;
import name.huliqing.ly.object.action.FightAction;
import name.huliqing.ly.object.action.FollowAction;
import name.huliqing.ly.object.action.RunAction;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.object.module.ActionModule;

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
    public void playAction(Entity actor, Action action) {
        // action可能为null. 当action=null的时候会结束角色当前的行为
        if (action != null) {
            action.setActor(actor);
        }
        getActionModule(actor).startAction(action);
    }

    @Override
    public void playRun(Entity actor, Vector3f pos) {
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
    public void playFight(Entity actor, Entity target, String skillId) {
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
        return actor.getEntityModule().getModule(ActionModule.class);
    }
}
