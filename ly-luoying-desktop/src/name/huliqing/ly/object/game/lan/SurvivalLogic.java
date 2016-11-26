/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game.lan;

import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import name.huliqing.luoying.Factory;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.luoying.data.GameLogicData;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.luoying.layer.network.ActorNetwork;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.network.SkillNetwork;
import name.huliqing.luoying.layer.network.StateNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.LogicService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.layer.service.StateService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.logic.PositionLogic;
import name.huliqing.luoying.logic.scene.ActorBuildLogic;
import name.huliqing.luoying.logic.scene.ActorBuildLogic.Callback;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.gamelogic.AbstractGameLogic;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.object.view.TextView;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 * 宝箱任务第二阶段：守护宝箱
 * @author huliqing
 * @param <T>
 */
public class SurvivalLogic<T extends GameLogicData> extends AbstractGameLogic<T> {
//    private final boolean debug = true;
    private final PlayService playService = Factory.get(PlayService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final LogicService logicService = Factory.get(LogicService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    
    private final GameService gameService = Factory.get(GameService.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    
    // 任务位置
    private final SurvivalGame game;
    
    // 怪物刷新器及刷新位置
    private ActorBuildLogic builderLogic;
    private SurvivalLevelLogic levelLogic;
    private SurvivalBoss bossLogic;
    
    // 宝箱角色
    private Entity treasure;
    
    // 当前任务阶段
    private int stage;
    
    /**
     * @param game
     */
    public SurvivalLogic(SurvivalGame game) {
        this.game = game;
    }

    @Override
    protected void doLogic(float tpf) {
        if (stage == 0) {
            Entity player = game.getPlayer();
            if (player != null) {
                doInit();
                stage = 1;
            }
        }
        
        // 任务逻辑
        if (stage == 1) {
            if (treasure != null && gameService.isDead(treasure)) {
                gameNetwork.addMessage(get(ResConstants.TASK_FAILURE), MessageType.notice);
                TextView textView = (TextView) Loader.load(IdConstants.VIEW_TEXT_FAILURE);
                textView.setUseTime(-1);
                playNetwork.addEntity(textView);
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
        treasure = Loader.load(IdConstants.ACTOR_TREASURE);
        actorService.setLocation(treasure, game.treasurePos);
        gameService.setGroup(treasure, game.PLAYER_GROUP);
        gameService.setTeam(treasure, gameService.getTeam(game.getPlayer()));
        playNetwork.addEntity(treasure);
        
        builderLogic = new ActorBuildLogic();
        builderLogic.setCallback(new Callback() {
            @Override
            public Entity onAddBefore(Entity actor) {
                gameService.setGroup(actor, game.ENEMY_GROUP);
                skillService.playSkill(actor, skillService.getSkillWaitDefault(actor), false);

                TempVars tv = TempVars.get();
                tv.vect1.set(game.treasurePos);
                Vector3f terrainHeight = playService.getTerrainHeight(game.getScene(), tv.vect1.x, tv.vect1.z);
                if (terrainHeight != null) {
                    tv.vect1.set(terrainHeight);
                }
                PositionLogic runLogic = (PositionLogic) Loader.load(IdConstants.LOGIC_POSITION);
//                runLogic.setInterval(3);
                runLogic.setPosition(tv.vect1);
                runLogic.setNearestDistance(game.nearestDistance);
                logicService.addLogic(actor, runLogic);
                tv.release();
                
                // 等级达到最高之后不再刷新敌人
                int level = levelLogic.getLevel();
                gameService.setLevel(actor, level < 1 ? 1 : level);
                return actor;
            }
        });
        
        builderLogic.setRadius(game.nearestDistance);
        builderLogic.setTotal(game.buildTotal);
        builderLogic.addPosition(game.enemyPositions);
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
