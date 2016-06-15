/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.List;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.constants.IdConstants;
import name.huliqing.fighter.constants.ResConstants;
import name.huliqing.fighter.constants.StoryConstants;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.enums.SkillType;
import name.huliqing.fighter.manager.talk.Talk;
import name.huliqing.fighter.manager.talk.TalkImpl;
import name.huliqing.fighter.manager.talk.TalkListener;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.game.network.SkillNetwork;
import name.huliqing.fighter.game.network.SkinNetwork;
import name.huliqing.fighter.game.network.StateNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.LogicService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.SkillService;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.game.service.ViewService;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.object.logic.PositionLogic;
import name.huliqing.fighter.logic.scene.ActorLoadHelper;
import name.huliqing.fighter.logic.scene.ActorBuildLogic;
import name.huliqing.fighter.logic.scene.ActorBuildLogic.Callback;
import name.huliqing.fighter.logic.scene.ActorBuildLogic.ModelLoader;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.manager.talk.AbstractTalkLogic;
import name.huliqing.fighter.object.actor.SkillListenerAdapter;
import name.huliqing.fighter.object.skill.Skill;
import name.huliqing.fighter.object.skill.impl.BackSkill;
import name.huliqing.fighter.object.view.TimerView;
import name.huliqing.fighter.ui.TextPanel;
import name.huliqing.fighter.utils.MathUtils;
import name.huliqing.fighter.ui.UI;
import name.huliqing.fighter.ui.UI.Corner;
import name.huliqing.fighter.ui.UI.Listener;

/**
 * 宝箱任务第二阶段：守护宝箱
 * @author huliqing
 */
public class StoryTreasureTask2 extends GameTaskBase {
    private final PlayService playService = Factory.get(PlayService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final LogicService logicService = Factory.get(LogicService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final ViewService viewService = Factory.get(ViewService.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final SkinNetwork skinNetwork = Factory.get(SkinNetwork.class);
    private final StateNetwork stateNetwork = Factory.get(StateNetwork.class);
    
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
    private Actor treasure;
    private Actor player;
    private Actor companion;
    private CompanionLoader companionLoader;
    private final float nearestDistance = 5;
    
    // ==== 角色分组
    
    // ====任务阶段
    private int stage;
    // 结束时的谈话
    private Talk endTalk;
    
    // ==== 
    // 时间选择
    private int[] mins = new int[] {5, 10, 15};
    // 怪物刷新点
    private Vector3f[] enemyPositions;
    // 同伴的随机生成位置
    private final Vector3f[] companionPosition = new Vector3f[] {new Vector3f(0, 0, 0)};
    
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
    protected void doInit(GameTask previous) {
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
        timerView = (TimerView) viewService.loadView(IdConstants.VIEW_TIMER);
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
                    playService.removeObject(startPanel);
                    actorNetwork.setTeam(treasure, actorService.getTeam(player));
                    playNetwork.addView(timerView);
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
                    playService.removeObject(startPanel);
                    actorNetwork.setTeam(treasure, actorService.getTeam(player));
                    playNetwork.addView(timerView);
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
                    playService.removeObject(startPanel);
                    actorNetwork.setTeam(treasure, actorService.getTeam(player));
                    playNetwork.addView(timerView);
                    stage = 1;
                }
            }
        });
        playService.addObject(startPanel, true);
        
        // Actor builder
        // 随机生成刷怪点
        enemyPositions = new Vector3f[buildTotal];
        for (int i = 0; i < buildTotal; i++) {
            enemyPositions[i] = MathUtils.getRandomPosition(80, 80, game.treasurePos, 50, null);
        }
        sceneBuilder = new ActorBuildLogic();
        sceneBuilder.setModelLoader(new ModelLoader() {
            @Override
            public Actor load(String actorId) {
                Actor actor = actorService.loadActor(actorId);
                return actor;
            }
        });
        sceneBuilder.setCallback(new Callback() {

            @Override
            public Actor onAddBefore(Actor actor) {
                actorService.setGroup(actor, game.groupEnemy);
                skillService.playSkill(actor, skillService.getSkill(actor, SkillType.wait).getId(), false);

                TempVars tv = TempVars.get();
                tv.vect1.set(game.treasurePos);
                tv.vect1.setY(playService.getTerrainHeight(tv.vect1.x, tv.vect1.z));
                PositionLogic runLogic = (PositionLogic) Loader.loadLogic(IdConstants.LOGIC_POSITION);
                runLogic.setPosition(tv.vect1);
                runLogic.setNearestDistance(nearestDistance);
                logicService.addLogic(actor, runLogic);
                tv.release();
                
                int level = (int) (timerView.getTimeUsed() / 60);
                actorService.setLevel(actor, level > 15 ? 15 : level); // 限制最高15级
                return actor;
            }
        });
        sceneBuilder.setRadius(buildRadius);
        sceneBuilder.setTotal(buildTotal);
        sceneBuilder.addPosition(enemyPositions);
        sceneBuilder.setInterval(3f);
        sceneBuilder.addId(
                IdConstants.ACTOR_NINJA, IdConstants.ACTOR_NINJA
                , IdConstants.ACTOR_SPIDER, IdConstants.ACTOR_SPIDER
                , IdConstants.ACTOR_WOLF
                , IdConstants.ACTOR_BEAR
                , IdConstants.ACTOR_SCORPION
                );
        
        player = playService.getPlayer();
        
        
    }

    @Override
    protected void doLogic(float tpf) {
        if (!enabled) {
            return;
        }
        
        if (stage == 1) {
//            playService.addObject(sceneBuilder, false);
            game.addLogic(sceneBuilder);
            stage = 2;
            return;
        }
        
        // 任务逻辑
        if (stage == 2) {
            
            if (timerView != null && timerView.getTime() <= 0) {
                companionLoader = new CompanionLoader();
                
//                playService.addObject(companionLoader, false);
                game.addLogic(companionLoader);
                
                playNetwork.removeObject(timerView);
                timerView = null;
            }
            
            // 任务失败检测
            if (treasure != null && treasure.isDead()) {
                playNetwork.addMessage(get(ResConstants.TASK_FAILURE), MessageType.notice);
                playNetwork.addMessage(get(ResConstants.COMMON_BACK_TO_TRY_AGAIN), MessageType.notice);
                playNetwork.addView(viewService.loadView(IdConstants.VIEW_TEXT_FAILURE));
                if (timerView != null) {
                    timerView.setEnabled(false);
                }
                stage = 999;
            } 
            
            // 任务完成检测
            if (companion != null 
                    && companion.getDistance(game.treasurePos) <= 20
                    && checkEnemyRemain() <= 0) {
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
                        skinNetwork.takeOnWeapon(companion, true);
                    }
                    @Override
                    protected void doTalkLogic(float tpf) {}
                });
                
                endTalk.addListener(new TalkListener() {
                    @Override
                    public void onTalkEnd() {
                        Skill skill = skillService.getSkillInstance(companion, IdConstants.SKILL_BACK);
                        if (skill == null || !(skill instanceof BackSkill)) {
                            playNetwork.removeObject(companion.getModel());
                            playNetwork.removeObject(treasure.getModel());
                            doTaskComplete();
                        } else {
                            final BackSkill backSkill = (BackSkill) skill;
                            backSkill.addBackObject(treasure.getModel());
                            actorService.addSkillListener(companion, new SkillListenerAdapter() {
                                @Override
                                public void onSkillEnd(Actor source, Skill skill) {
                                    if (skill == backSkill) {
                                        playNetwork.removeObject(companion.getModel());
                                        playNetwork.removeObject(treasure.getModel());
                                        doTaskComplete();
                                    }
                                }
                            });
                            skillNetwork.playSkill(companion, backSkill.getSkillData().getId(), true);
                        }
                    }
                });
                actorNetwork.talk(endTalk);
                stage = 999;
            }
            
        } 
        
    }
    
    private void doTaskComplete() {
        playService.saveCompleteStage(StoryConstants.STORY_NUM_TREASURE);
        playNetwork.addMessage(get(ResConstants.TASK_SUCCESS), MessageType.item);
        playNetwork.addMessage(get(ResConstants.COMMON_BACK_TO_CONTINUE), MessageType.item);
        playNetwork.addView(viewService.loadView(IdConstants.VIEW_TEXT_SUCCESS));
    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
    private String getOther(String rid, Object... params) {
        if (params == null) {
            return ResourceManager.getOther("resource_treasure", rid);
        } else {
            return ResourceManager.getOther("resource_treasure", rid, params);
        }
    }
    
    private String get(String rid, Object... params) {
        if (params == null) {
            return ResourceManager.get(rid);
        } else {
            return ResourceManager.get(rid, params);
        }
    }
    
    // 判断战场中还有多少个敌人
    private int checkEnemyRemain() {
        List<Actor> actors = playService.findAllActor();
        int alive = 0;
        for (Actor a : actors) {
            if (actorService.getGroup(a) == game.groupEnemy 
                    && !actorService.isDead(a)
                    && a.getDistance(player) < 20
                    ) {
                alive++;
            }
        }
        return alive;
    }
    
    /**
     * 将当前战场所有敌人的目标重定向到指定的actor.
     * @param actor 
     */
    private void setAllEnemyTarget(Actor actor) {
        List<Actor> actors = playService.findAllActor();
        for (Actor a : actors) {
            if (!a.isDead() && a.isEnemy(actor)) {
                actorNetwork.setTarget(a, actor);
            }
        }
    }
    
    // 载入同伴
    private class CompanionLoader extends ActorLoadHelper {
        
        @Override
        public Actor load() {
            Actor actor = actorService.loadActor(IdConstants.ACTOR_DINA);
            return actor;
        }

        @Override
        public void callback(Actor actor) {
            companion = actor;
            companion.setLocation(companionPosition[FastMath.nextRandomInt(0, companionPosition.length - 1)]);
            actorService.setLevel(companion, 40);
            actorService.setPartner(player, actor);
            actorService.setTeam(companion, player.getData().getTeam());
            skillService.playSkill(companion, skillService.getSkill(companion, SkillType.wait).getId(), false);
            // 同伴进入战场后，刷新器不再刷怪。
            sceneBuilder.setEnabled(false);
            // 同伴进入战场后宝箱不死
            if (treasure != null) {
                stateNetwork.addStateForce(treasure, IdConstants.STATE_SAFE, 0, null);
            }
            playNetwork.addActor(companion);
        }
        
    }
}
