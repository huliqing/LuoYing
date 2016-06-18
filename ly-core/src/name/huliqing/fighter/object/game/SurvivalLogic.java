/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import com.jme3.util.TempVars;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.constants.IdConstants;
import name.huliqing.fighter.constants.ResConstants;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.enums.SkillType;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.game.network.SkillNetwork;
import name.huliqing.fighter.game.network.StateNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.LogicService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.SkillService;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.game.service.ViewService;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.object.logic.PositionLogic;
import name.huliqing.fighter.logic.scene.ActorBuildLogic;
import name.huliqing.fighter.logic.scene.ActorBuildLogic.Callback;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.AbstractPlayObject;
import name.huliqing.fighter.object.view.TextView;

/**
 * 宝箱任务第二阶段：守护宝箱
 * @author huliqing
 */
public class SurvivalLogic extends AbstractPlayObject {
    private boolean debug = false;
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final StateNetwork stateNetwork = Factory.get(StateNetwork.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final LogicService logicService = Factory.get(LogicService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final ViewService viewService = Factory.get(ViewService.class);
    
    // 任务位置
    private final SurvivalGame game;
    
    // 怪物刷新器及刷新位置
    private ActorBuildLogic builderLogic;
    private SurvivalLevelLogic levelLogic;
    private SurvivalBoss bossLogic;
    
    // 宝箱角色
    private Actor treasure;
    
    // 当前任务阶段
    private int stage;
    
    /**
     * @param game
     */
    public SurvivalLogic(SurvivalGame game) {
        this.game = game;
    }

    @Override
    public void update(float tpf) {
        if (stage == 0) {
            Actor player = playService.getPlayer();
            if (player != null) {
                doInit();
                stage = 1;
            }
        }
        
        // 任务逻辑
        if (stage == 1) {
            if (treasure != null && treasure.isDead()) {
                playNetwork.addMessage(get(ResConstants.TASK_FAILURE), MessageType.notice);
                TextView textView = (TextView) viewService.loadView(IdConstants.VIEW_TEXT_FAILURE);
                textView.setUseTime(-1);
                playNetwork.addView(textView);
                game.removeLogic(builderLogic);
                game.removeLogic(levelLogic);
                game.removeLogic(bossLogic);
                stage = 999;
            }
            return;
        } 
        
        if (stage == 999) {
            // end
        }
    }
    
    private void doInit() {
        treasure = actorService.loadActor(IdConstants.ACTOR_TREASURE);
        treasure.setLocation(game.treasurePos);
        actorService.setGroup(treasure, game.SELF_GROUP);
        actorService.setTeam(treasure, actorService.getTeam(playService.getPlayer()));
        playNetwork.addActor(treasure);
        
        builderLogic = new ActorBuildLogic();
        builderLogic.setCallback(new Callback() {
            @Override
            public Actor onAddBefore(Actor actor) {
                actorService.setGroup(actor, game.GROUP_ENEMY);
                skillService.playSkill(actor, skillService.getSkill(actor, SkillType.wait).getId(), false);

                TempVars tv = TempVars.get();
                tv.vect1.set(game.treasurePos);
                tv.vect1.setY(playService.getTerrainHeight(tv.vect1.x, tv.vect1.z));
                PositionLogic runLogic = (PositionLogic) Loader.loadLogic(IdConstants.LOGIC_POSITION);
                runLogic.setInterval(3);
                runLogic.setPosition(tv.vect1);
                runLogic.setNearestDistance(game.nearestDistance);
                logicService.addLogic(actor, runLogic);
                tv.release();
                
                // 等级达到最高之后不再刷新敌人
                int level = levelLogic.getLevel();
                actorService.setLevel(actor, level < 1 ? 1 : level);
                return actor;
            }
        });
        
        builderLogic.setRadius(game.nearestDistance);
        builderLogic.setTotal(game.buildTotal);
        builderLogic.addPosition(game.enemyPositions);
        builderLogic.setInterval(3f);
        builderLogic.addId(
                  IdConstants.ACTOR_NINJA,IdConstants.ACTOR_NINJA
                , IdConstants.ACTOR_SPIDER, IdConstants.ACTOR_SPIDER
                , IdConstants.ACTOR_WOLF, IdConstants.ACTOR_WOLF
                , IdConstants.ACTOR_BEAR, IdConstants.ACTOR_BEAR
                , IdConstants.ACTOR_SCORPION, IdConstants.ACTOR_SCORPION
                );
        
        levelLogic = new SurvivalLevelLogic(game.levelUpBySec, game.maxLevel);
        bossLogic = new SurvivalBoss(game, builderLogic, levelLogic);

        // 刷新普通角色,等级更新器,BOSS逻辑
        game.addLogic(builderLogic);
        game.addLogic(levelLogic);
        game.addLogic(bossLogic);
    }
        
    private String get(String rid, Object... params) {
        if (params == null) {
            return ResourceManager.get(rid);
        } else {
            return ResourceManager.get(rid, params);
        }
    }
}
