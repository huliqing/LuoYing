/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game.story;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.List;
import name.huliqing.luoying.Config;
import name.huliqing.luoying.Factory;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.view.talk.Talk;
import name.huliqing.ly.view.talk.TalkImpl;
import name.huliqing.ly.view.talk.TalkListener;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.network.SkillNetwork;
import name.huliqing.luoying.layer.network.SkinNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.LogicService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.SceneService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.logic.PositionLogic;
import name.huliqing.luoying.logic.scene.ActorLoadHelper;
import name.huliqing.luoying.logic.scene.ActorBuildLogic;
import name.huliqing.luoying.logic.scene.ActorBuildLogic.Callback;
import name.huliqing.luoying.logic.scene.ActorBuildLogic.ModelLoader;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.ly.view.talk.AbstractTalkLogic;
import name.huliqing.luoying.object.module.SkillListenerAdapter;
import name.huliqing.luoying.object.module.StateModule;
import name.huliqing.luoying.object.skill.Skill;
import name.huliqing.luoying.object.skill.BackSkill;
import name.huliqing.luoying.object.state.State;
import name.huliqing.ly.object.view.TimerView;
import name.huliqing.luoying.ui.TextPanel;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UI.Corner;
import name.huliqing.luoying.ui.UI.Listener;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.constants.StoryConstants;
import name.huliqing.ly.object.view.View;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 * 宝箱任务第二阶段：守护宝箱
 * @author huliqing
 */
public class StoryTreasureTask2 extends AbstractTaskStep {
    private final PlayService playService = Factory.get(PlayService.class);
    private final LogicService logicService = Factory.get(LogicService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final SkinNetwork skinNetwork = Factory.get(SkinNetwork.class);
    private final GameService gameService = Factory.get(GameService.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    private final SceneService sceneService = Factory.get(SceneService.class);
    
    // ==== 任务位置
    private final StoryTreasureGame game;
    
    // ==== 怪物刷新器及刷新位置
    private ActorBuildLogic sceneBuilder;
    private final float buildRadius = 5;
    private final int buildTotal = 8;
    
    // ==== 任务面板
    private TextPanel startPanel;
    
    // ==== 倒计时
    private TimerView timerView;
    
    // ==== 角色
    private Entity treasure;
    private Entity player;
    private Entity companion;
    private CompanionLoader companionLoader;
    private final float nearestDistance = 5;
    
    // ==== 角色分组
    
    // ====任务阶段
    private int stage;
    // 结束时的谈话o
    private Talk endTalk;
    
    // ==== 
    // 时间选择
    private int[] mins = new int[] {5, 10, 15};
    // 怪物刷新点
    private Vector3f[] enemyPositions;
    // 同伴的随机生成位置
    private final Vector3f companionPosition = new Vector3f(0, 0, 0);
    
    private boolean enabled = true;
    
    /**
     * @param game
     */
    public StoryTreasureTask2(StoryTreasureGame game) {
        this.game = game;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    @Override
    protected void doInit(TaskStep previous) {
        if (Config.debug) {
            mins = new int[] {1, 10, 15};
        }
        
        companion = null;
        companionLoader = null;
        stage = 0;
        if (previous != null && (previous instanceof StoryTreasureTask1)) {
            treasure = ((StoryTreasureTask1) previous).getTreasure();
        }
        float w = playService.getScreenWidth();
        float h = playService.getScreenHeight();
        float pw = w * 0.75f; // panel width
        
        // == timePanel
        timerView = (TimerView) Loader.load(IdConstants.VIEW_TIMER);
        timerView.setTitle(ResourceManager.getObjectName(IdConstants.GAME_STORY_TREASURE));
        timerView.setUp(false);
        
        // ===
        startPanel = new TextPanel(getOther("task2.title"), pw, 0);
        startPanel.setDragEnabled(true);
        startPanel.addText(getOther("task2.content"));
        startPanel.setToCorner(Corner.CC);
        startPanel.setCloseable(false);
        startPanel.addButton(getOther("task2.confirm", mins[0]) + "(" + get("level.easy") + ")"
                , new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    timerView.setStartTime(60 * mins[0]);
                    timerView.setUseTime(60 * mins[0]);
                    startPanel.removeFromParent();
                    gameNetwork.setTeam(treasure, gameService.getTeam(player));
                    playNetwork.addEntity(timerView);
                    stage = 1;
                }
            }
        });
        startPanel.addButton(getOther("task2.confirm", mins[1]) + "(" + get("level.normal") + ")"
                , new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    timerView.setStartTime(60 * mins[1]);
                    timerView.setUseTime(60 * mins[1]);
                    startPanel.removeFromParent();
                    gameNetwork.setTeam(treasure, gameService.getTeam(player));
                    playNetwork.addEntity(timerView);
                    stage = 1;
                }
            }
        });
        startPanel.addButton(getOther("task2.confirm", mins[2]) + "(" + get("level.hard") + ")"
                , new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    timerView.setStartTime(60 * mins[2]);
                    timerView.setUseTime(60 * mins[2]);
                    startPanel.removeFromParent();
                    gameNetwork.setTeam(treasure, gameService.getTeam(player));
                    playNetwork.addEntity(timerView);
                    stage = 1;
                }
            }
        });
        UIState.getInstance().addUI(startPanel);
        
        // Entity builder
        // 随机生成刷怪点
        enemyPositions = new Vector3f[buildTotal];
        for (int i = 0; i < buildTotal; i++) {
            enemyPositions[i] = MathUtils.getRandomPosition(80, 80, game.treasurePos, 50, null);
        }
        sceneBuilder = new ActorBuildLogic();
        sceneBuilder.setModelLoader(new ModelLoader() {
            @Override
            public Entity load(String actorId) {
                return Loader.load(actorId);
            }
        });
        sceneBuilder.setCallback(new Callback() {

            @Override
            public Entity onAddBefore(Entity actor) {
                gameService.setGroup(actor, game.groupEnemy);
                skillService.playSkill(actor, skillService.getSkillWaitDefault(actor), false);

                TempVars tv = TempVars.get();
                tv.vect1.set(game.treasurePos);
                Vector3f terrainHeight = playService.getTerrainHeight(game.getScene(), tv.vect1.x, tv.vect1.z);
                if (terrainHeight != null) {
                    tv.vect1.set(terrainHeight);
                }
                
                PositionLogic runLogic = (PositionLogic) Loader.load(IdConstants.LOGIC_POSITION);
                runLogic.setPosition(tv.vect1);
                runLogic.setNearestDistance(nearestDistance);
                logicService.addLogic(actor, runLogic);
                tv.release();
                
                int level = (int) (timerView.getTimeUsed() / 60);
                gameService.setLevel(actor, level > 15 ? 15 : level); // 限制最高15级
                return actor;
            }
        });
        sceneBuilder.setRadius(buildRadius);
        sceneBuilder.setTotal(buildTotal);
        sceneBuilder.addPosition(enemyPositions);
        sceneBuilder.addId(
                IdConstants.ACTOR_NINJA, IdConstants.ACTOR_NINJA
                , IdConstants.ACTOR_SPIDER, IdConstants.ACTOR_SPIDER
                , IdConstants.ACTOR_WOLF
                , IdConstants.ACTOR_BEAR
                , IdConstants.ACTOR_SCORPION
                );
        
        player = game.getPlayer();
        
        
    }

    @Override
    protected void doLogic(float tpf) {
        if (!enabled) {
            return;
        }
        
        if (stage == 1) {
            game.addLogic(sceneBuilder);
            stage = 2;
            return;
        }
        
        // 任务逻辑
        if (stage == 2) {
            
            if (timerView != null && timerView.getTime() <= 0) {
                companionLoader = new CompanionLoader();
                game.addLogic(companionLoader);
                playNetwork.removeEntity(timerView);
                timerView = null;
            }
            
            // 任务失败检测
            if (treasure != null && gameService.isDead(treasure)) {
                gameNetwork.addMessage(get(ResConstants.TASK_FAILURE), MessageType.notice);
                gameNetwork.addMessage(get(ResConstants.COMMON_BACK_TO_TRY_AGAIN), MessageType.notice);
                playNetwork.addEntity((View)Loader.load(IdConstants.VIEW_TEXT_FAILURE));
                if (timerView != null) {
                    timerView.setEnabled(false);
                }
                stage = 999;
            } 
            
            // 任务完成检测
            if (companion != null 
                    && actorService.distance(companion, game.treasurePos) <= 20
//                    && checkEnemyRemain() <= 0
                    ) {
                // 将所有敌人的目标重定向到companion,这个时候应该避免player被打死的可能。
                setAllEnemyTarget(companion);
                // 跳转到对话
                stage = 3;
            }
            
            return;
        }
        
        if (stage == 3) {
            // do talk
            if (endTalk == null) {
                endTalk = new TalkImpl();
                endTalk.delay(0.5f);
                endTalk.speak(companion, getOther("talk3.diNa1"));
                endTalk.speak(companion, getOther("talk3.diNa2"));
                endTalk.face(player, companion, false);
                endTalk.speak(player, getOther("talk3.player1"));
                endTalk.speak(companion, getOther("talk3.diNa3"));
                endTalk.speak(companion, getOther("talk3.diNa4"));
                endTalk.face(player, companion, false);
                endTalk.speak(player, getOther("talk3.player2"));
                endTalk.speak(companion, getOther("talk3.diNa5"));
                endTalk.face(companion, player, false);
                endTalk.speak(companion, getOther("talk3.diNa6"));
                endTalk.delay(1.5f);
                endTalk.face(companion, treasure, false);
                endTalk.addTalkLogic(new AbstractTalkLogic() {
                    @Override
                    protected void doInit() {
                        useTime = 1.2f;
                        skinNetwork.takeOnWeapon(companion);
                    }
                    @Override
                    protected void doTalkLogic(float tpf) {}
                });
                
                endTalk.addListener(new TalkListener() {
                    @Override
                    public void onTalkEnd() {
                        Skill skill = skillService.getSkill(companion, IdConstants.SKILL_BACK);
                        if (skill == null || !(skill instanceof BackSkill)) {
                            playNetwork.removeEntity(companion);
                            playNetwork.removeEntity(treasure);
                            doTaskComplete();
                        } else {
                            final BackSkill backSkill = (BackSkill) skill;
                            skillService.addSkillPlayListener(companion, new SkillListenerAdapter() {
                                @Override
                                public void onSkillEnd(Skill skill) {
                                    if (skill == backSkill) {
                                        playNetwork.removeEntity(companion);
                                        playNetwork.removeEntity(treasure);
                                        doTaskComplete();
                                    }
                                }
                            });
                            skillNetwork.playSkill(companion, backSkill, true);
                        }
                    }
                });
                gameNetwork.talk(endTalk);
                stage = 999;
            }
            
        } 
        
    }
    
    private void doTaskComplete() {
        gameService.saveCompleteStage(StoryConstants.STORY_NUM_TREASURE);
        gameNetwork.addMessage(get(ResConstants.TASK_SUCCESS), MessageType.item);
        gameNetwork.addMessage(get(ResConstants.COMMON_BACK_TO_CONTINUE), MessageType.item);
        playNetwork.addEntity((View)Loader.load(IdConstants.VIEW_TEXT_SUCCESS));
    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
    private String getOther(String rid, Object... params) {
        if (params == null) {
            return ResourceManager.get("storyTreasure." +  rid);
        } else {
            return ResourceManager.get("storyTreasure." +  rid, params);
        }
    }
    
    private String get(String rid, Object... params) {
        if (params == null) {
            return ResourceManager.get(rid);
        } else {
            return ResourceManager.get(rid, params);
        }
    }
    
//    // 判断战场中还有多少个敌人
//    private int checkEnemyRemain() {
//        List<Entity> actors = playService.findAllActor();
//        int alive = 0;
//        for (Entity a : actors) {
//            if (actorService.getGroup(a) == game.groupEnemy 
//                    && !gameService.isDead(a)
//                    && actorService.distance(a, player) < 20
//                    ) {
//                alive++;
//            }
//        }
//        return alive;
//    }
    
    /**
     * 将当前战场所有敌人的目标重定向到指定的actor.
     * @param actor 
     */
    private void setAllEnemyTarget(Entity actor) {
        List<Actor> actors = game.getScene().getEntities(Actor.class, null);
        for (Entity a : actors) {
            if (!gameService.isDead(a) && gameService.isEnemy(a, actor)) {
                gameNetwork.setTarget(a, actor.getEntityId());
            }
        }
    }
    
    // 载入同伴
    private class CompanionLoader extends ActorLoadHelper {
        
        @Override
        public Entity load() {
            return Loader.load(IdConstants.ACTOR_DINA);
        }

        @Override
        public void callback(Entity actor) {
            companion = actor;
            Vector3f locWithFixedHeight = sceneService.getSceneHeight(companionPosition.x, companionPosition.z);
            if (locWithFixedHeight != null) {
                actorService.setLocation(companion, locWithFixedHeight);
            } else {
                actorService.setLocation(companion, companionPosition);
            }
            gameService.setLevel(companion, 40);
            gameService.setPartner(player, actor);
            gameService.setTeam(companion, gameService.getTeam(player));
            skillService.playSkill(companion, skillService.getSkillWaitDefault(companion), false);
            // 同伴进入战场后，刷新器不再刷怪。
            sceneBuilder.setEnabled(false);
            // 同伴进入战场后宝箱不死
            if (treasure != null) {
                StateModule sm = treasure.getModuleManager().getModule(StateModule.class);
                if (sm != null) {
                    sm.addState((State)Loader.load(IdConstants.STATE_SAFE), true);
                }
            }
            playNetwork.addEntity(companion);
        } 
        
    }
}
