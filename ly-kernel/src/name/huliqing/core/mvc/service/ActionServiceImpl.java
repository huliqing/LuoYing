/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import com.jme3.math.Vector3f;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.mvc.dao.SkillDao;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.object.action.Action;
import name.huliqing.core.object.action.FightAction;
import name.huliqing.core.object.action.FollowAction;
import name.huliqing.core.object.action.RunAction;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.control.ActorActionControl;

/**
 *
 * @author huliqing
 */
public class ActionServiceImpl implements ActionService {

    private ActorService actorService;
    private SkinService skinService;
    private SkillDao skillDao;
    
    @Override
    public void inject() {
        actorService = Factory.get(ActorService.class);
        skinService = Factory.get(SkinService.class);
        skillDao = Factory.get(SkillDao.class);
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
        getActionControl(actor).startAction(action);
    }

    @Override
    public void playRun(Actor actor, Vector3f pos) {
        ActorActionControl control = getActionControl(actor);
        RunAction ra = control.getDefRunAction();
        // 如果角色没有指定默认“跑路”行为，则为角色创建一个。
        if (ra == null) {
            String defRunAction = actor.getData().getActionDefRun();
            if (defRunAction != null) {
                ra = (RunAction) Loader.loadAction(defRunAction);
            } else {
                ra = (RunAction) Loader.loadAction(IdConstants.ACTION_RUN_SIMPLE);
            }
            ra.setActor(actor);
            control.setDefRunAction(ra);
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
//        ActionProcessor ap = actor.getActionProcessor();
        ActorActionControl control = getActionControl(actor);
        FightAction fa = control.getDefFightAction();
        // 如果角色没有指定特定的战斗行为，则为角色创建一个。
        if (fa == null) {
            String defFightAction = actor.getData().getActionDefFight();
            if (defFightAction != null) {
                fa = (FightAction) Loader.loadAction(defFightAction);
            } else {
                if (actorService.getMass(actor) <= 0) {
                    fa = (FightAction) Loader.loadAction(IdConstants.ACTION_FIGHT_STATIC);
                } else {
                    fa = (FightAction) Loader.loadAction(IdConstants.ACTION_FIGHT_FOR_PLAYER);
                }
            }
            fa.setActor(actor);
            control.setDefFightAction(fa);
        }
        
        SkillData skillData = null;
        if (skillId != null) {
            skillData = skillDao.getSkillById(actor.getData(), skillId);
        }
        
        fa.setEnemy(target);
        fa.setSkill(skillData);
        playAction(actor, fa);
    }

    @Override
    public boolean isPlayingFight(Actor actor) {
        ActorActionControl control = getActionControl(actor);
        Action current = control.getAction();
        return current instanceof FightAction;
    }

    @Override
    public boolean isPlayingRun(Actor actor) {
        Action current = getActionControl(actor).getAction();
        return current instanceof RunAction;
    }

    @Override
    public boolean isPlayingFollow(Actor actor) {
        Action current = getActionControl(actor).getAction();
        return current instanceof FollowAction;
    }

    @Override
    public Action getPlayingAction(Actor actor) {
        return getActionControl(actor).getAction();
    }
    
    private ActorActionControl getActionControl(Actor actor) {
        return actor.getModel().getControl(ActorActionControl.class);
    }
}
