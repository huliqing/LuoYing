/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game.story;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Config;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.enums.Diffculty;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.view.talk.Talk;
import name.huliqing.ly.view.talk.TalkImpl;
import name.huliqing.ly.view.talk.TalkListener;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.LogicService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.logic.scene.ActorBuildSimpleLogic;
import name.huliqing.luoying.logic.scene.ActorBuildSimpleLogic.Callback;
import name.huliqing.luoying.logic.scene.ActorMultLoadHelper;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.game.Game;
import name.huliqing.luoying.object.gamelogic.AbstractGameLogic;
import name.huliqing.luoying.object.logic.Logic;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.constants.StoryConstants;
import name.huliqing.ly.object.view.View;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 * 守护古柏
 * @author huliqing
 */
public class StoryGuardTask2 extends AbstractTaskStep {
    private final static Logger LOG = Logger.getLogger(StoryGuardTask2.class.getName());
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final LogicService logicService = Factory.get(LogicService.class);
    private final GameService gameService = Factory.get(GameService.class);
    
//    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    private final StoryGuardGame game;
    
    // ---- 静态参数设定
    // 难易程度
    private Diffculty level = Diffculty.normal;
    // 难度对应的等级,分别对应:easy,normal,hard
    private final float[] levelFactors = {0.7f, 1, 1.2f};
    // 角色的复活频繁,单位秒,只要祭坛或古柏存在，则角色死亡后会复活，
    private final float resurrectInterval = 30;
    // 基本的等级范围,最终的等级范围会受难易程度选择的不同而不同.
    // 并且boss的等级范围可能超过这个限制(see bossLevelUp);
    private final int[] levelRange = {14, 20};
    // 多少秒钟换算角色的一个等级,当角色复活时会通过时间来换算角色的等级
    private final int timeForUpLevel = 60;
    // boss(sinbad)的等级提升，boss的等级至少会比小兵多一些等级数
    private final int bossLevelUp = 5;
    // 主基据点(祭坛、古柏)的等级提升
    private final int footHoldLevelUp = 0;
    // 可用的防御塔类型ID
    private final String[] towerIds = {IdConstants.ACTOR_TOWER, IdConstants.ACTOR_TOWER_STONE};
    // 任务结束检测
    private final EndChecker endChecker = new EndChecker();
    
    // ---- 动态参数设定
    // 所选择的最终难度等级
    private float levelFactor = levelFactors[Diffculty.normal.ordinal()];
    // 当前选择的最低等级数
    private int minLevel = levelRange[0];
    // 当前选择的最高等级数
    private int maxLevel = levelRange[1];
    // 主基GB、祭坛等级这个等级是固定的，不会再提升，否则打不动就不好办了
    private int footHoldLevel = maxLevel + footHoldLevelUp;
    // 当前用时,单位秒,这个时间是标记开始攻防战开时后的已用时间，主要用于换算
    // 角色的复活等级，每分钟让角色的复活等级提高一个级别
    private float time;
    
    // ---- 友军参数设置
    private Entity player;
    private Entity gb;
    private ActorBuildSimpleLogic selfActorBuilder;
    
    // ---- 敌军参数设置
    // 敌人祭坛、主将、手下、熊、防御塔
    private Entity altar;
    private Entity sinbad;
    private Entity ninjia1;
    private Entity ninjia2;
    private Entity bear;
    private ActorBuildSimpleLogic enemyActorBuilder0;
    // 敌人小兵数量,和防御塔数量
    private final String[] enemyTowers = {
         towerIds[FastMath.nextRandomInt(0, towerIds.length - 1)]
        ,towerIds[FastMath.nextRandomInt(0, towerIds.length - 1)]
        ,towerIds[FastMath.nextRandomInt(0, towerIds.length - 1)]
        ,towerIds[FastMath.nextRandomInt(0, towerIds.length - 1)]
    };
    private ActorMultLoadHelper enemyTowerLoader;
    // 敌方小角色
    private final String[] enemySoldiers = new String[] {
              IdConstants.ACTOR_BEAR
            , IdConstants.ACTOR_SPIDER,  IdConstants.ACTOR_SPIDER
            , IdConstants.ACTOR_WOLF,    IdConstants.ACTOR_WOLF
            , IdConstants.ACTOR_SCORPION,IdConstants.ACTOR_SCORPION
    };
    private ActorBuildSimpleLogic enemyActorBuilder1;
    private ActorBuildSimpleLogic enemyActorBuilder2;
    private ActorBuildSimpleLogic enemyActorBuilder3;
    
    // 盗贼的对话
    private Talk dzTalk;
    // 谈话过程发现偷袭时的对话
    private Talk dzTalkFoundSneak;
    // 用于检测盗贼对话过程中是否发现player偷袭
    private PlayerChecker playerChecker;
    
    // ---- 任务阶段
    // 0.对话阶段；1.开始攻防战
    private int stage;
    // 是否开启敌军主动进攻
    private boolean offensiveEnemy;
    // 是否结束
    private boolean finished;
    
    // ==== 妖精任务
    private StoryGuardFairyTask fairyTask;
    
    public StoryGuardTask2(StoryGuardGame game) {
        this.game = game;
    }
    
    @Override
    protected void doInit(TaskStep previous) {
        player = game.getPlayer();
        
        if (previous != null) {
            StoryGuardTask1 gtPrevious = (StoryGuardTask1) previous;
            level = gtPrevious.getLevel();
            levelFactor = levelFactors[level.ordinal()];
            minLevel = (int) (levelRange[0] * levelFactor);
            maxLevel = (int) (levelRange[1] * levelFactor);
            footHoldLevel = maxLevel + footHoldLevelUp;
            gb = gtPrevious.getGb();
            gameNetwork.setLevel(gb, footHoldLevel);
        } else {
            // for test
            level = Diffculty.normal;
            levelFactor = levelFactors[level.ordinal()];
            minLevel = (int) (levelRange[0] * levelFactor);
            maxLevel = (int) (levelRange[1] * levelFactor);
            footHoldLevel = maxLevel + footHoldLevelUp;
            
            gb = Loader.load(IdConstants.ACTOR_GB);
            
            gameService.setLevel(gb, footHoldLevel);
            gameService.setGroup(gb, gameService.getGroup(player));
            actorService.setLocation(gb, new Vector3f(42, 0, -61));
            playNetwork.addEntity(gb);
        }
        
        // ---- self load
        Callback selfCallback = new Callback(){
            @Override
            public void onload(Entity actor) {
                setSelfActor(actor, false);
            }
        };
        selfActorBuilder = new ActorBuildSimpleLogic();
        selfActorBuilder.addBuilder(getRandomPosition(game.getSelfPosition())
                , new String[] {IdConstants.ACTOR_GB_SMALL}, resurrectInterval, selfCallback);
        selfActorBuilder.addBuilder(getRandomPosition(game.getSelfPosition())
                , new String[] {IdConstants.ACTOR_GB_SMALL}, resurrectInterval, selfCallback);
        
        // ---- enemy load
        // 载入敌方祭坛
        altar = Loader.load(IdConstants.ACTOR_ALTAR);
        
        gameService.setLevel(altar, footHoldLevel);
        gameService.setGroup(altar, StoryGuardGame.GROUP_ENEMY);
        actorService.setLocation(altar, game.getEnemyPosition());
        playNetwork.addEntity(altar);
        
        // sinbad和手下的载入
        Callback enemyCallback = new Callback(){
            @Override
            public void onload(Entity actor) {
                setEnemyActor(actor, false);
                String id = actor.getData().getId();
                if (id.equals(IdConstants.ACTOR_SINBAD)) {
                    sinbad = actor;
                } else if (id.equals(IdConstants.ACTOR_NINJA)) {
                    if (ninjia1 == null) {
                        ninjia1 = actor;
                    } else {
                        ninjia2 = actor;
                    }
                } else if (id.equals(IdConstants.ACTOR_BEAR)) {
                    bear = actor;
                }
            }
        };
        enemyActorBuilder0 = new ActorBuildSimpleLogic();
        // sinbad
        enemyActorBuilder0.addBuilder(getRandomPosition(game.getEnemyPosition())
                , new String[]{IdConstants.ACTOR_SINBAD}, resurrectInterval, enemyCallback);
        // ninjia
        enemyActorBuilder0.addBuilder(getRandomPosition(game.getEnemyPosition())
                , new String[]{IdConstants.ACTOR_NINJA},  resurrectInterval, enemyCallback);
        // ninjia
        enemyActorBuilder0.addBuilder(getRandomPosition(game.getEnemyPosition())
                , new String[]{IdConstants.ACTOR_NINJA},  resurrectInterval, enemyCallback);
        // bear
        enemyActorBuilder0.addBuilder(getRandomPosition(game.getEnemyPosition())
                , new String[]{IdConstants.ACTOR_BEAR},  resurrectInterval, enemyCallback);
        // towers
        enemyTowerLoader = new ActorMultLoadHelper(enemyTowers) { 
            @Override
            public void callback(Entity actor, int loadIndex) {
                setEnemyActor(actor, true);
                playNetwork.addEntity(actor);
            }
        };
        
        // 敌军小兵
        Callback enemyCallback2 = new Callback(){
            @Override
            public void onload(Entity actor) {
                setEnemyActor(actor, false);
            }
        };
        enemyActorBuilder1 = new ActorBuildSimpleLogic();
        enemyActorBuilder1.addBuilder(getRandomPosition(game.getEnemyPosition()), enemySoldiers, resurrectInterval, enemyCallback2);
        enemyActorBuilder2 = new ActorBuildSimpleLogic();
        enemyActorBuilder2.addBuilder(getRandomPosition(game.getEnemyPosition()), enemySoldiers, resurrectInterval, enemyCallback2);
        enemyActorBuilder3 = new ActorBuildSimpleLogic();
        enemyActorBuilder3.addBuilder(getRandomPosition(game.getEnemyPosition()), enemySoldiers, resurrectInterval, enemyCallback2);
        
        playerChecker = new PlayerChecker();
        
        // 妖精任务
        fairyTask = new StoryGuardFairyTask(game);
        
        // ---- 刷怪
        game.addLogic(enemyTowerLoader);
        game.addLogic(enemyActorBuilder0);
        game.addLogic(enemyActorBuilder1);
        game.addLogic(enemyActorBuilder2);
        game.addLogic(enemyActorBuilder3);
        game.addLogic(fairyTask);
        game.addLogic(endChecker);
    }

    @Override
    protected void doLogic(float tpf) {
        // ---- 任务结果检测:“成功”“失败”停止所有刷新
        if (endChecker.isEnabled()) {
            if (endChecker.isFailure() || endChecker.isSuccess()) {
                enemyActorBuilder0.setEnabled(false);
                enemyActorBuilder1.setEnabled(false);
                enemyActorBuilder2.setEnabled(false);
                enemyActorBuilder3.setEnabled(false);
                selfActorBuilder.setEnabled(false);
                if (endChecker.isSuccess()) {
                    // 任务完成时清除所有存活敌人，并保存关卡数
                    killAllEnemies();
                    gameService.saveCompleteStage(StoryConstants.STORY_NUM_GUARD);
                    gameNetwork.addMessage(getOther("task.success"), MessageType.item);
                    gameNetwork.addMessage(get("common.backToContinue"), MessageType.item);
                    playNetwork.addEntity((View)Loader.load(IdConstants.VIEW_TEXT_SUCCESS));
                } else {
                    // say failure
                    gameNetwork.addMessage(getOther("task.failure"), MessageType.notice);
                    gameNetwork.addMessage(get("common.backToTryAgain"), MessageType.notice);
                    playNetwork.addEntity((View)Loader.load(IdConstants.VIEW_TEXT_FAILURE));
                }
                endChecker.setEnabled(false);
                stage = 999;
            }
        }
        
        // ---- 任何阶段检查
        // 1.并启动盗贼间的对话
        if (stage == 0) {
            if (checkToDzTalk()) {
                startDZTalk();
                game.addLogic(playerChecker);
                stage = 1;
            }
            return;
        } 
        
        // 2.检查盗贼对话是否被偷袭打断
        if (stage == 1) {
            if (playerChecker.result) {
                if (!dzTalk.isEnd()) {
                    dzTalk.cleanup();
                    startDZTalkFoundSneak();
                }
                game.addLogic(selfActorBuilder);
                stage = 2;
            }
            return;
        } 
        
        // 3.开始战斗逻辑,并计时,并刷小树人
        if (stage == 2) {
            time += tpf;
            return;
        } 
        
        // 4.结束
        if (stage == 999) {
            // end do nothing
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
    
    private void startDZTalkFoundSneak() {
        dzTalkFoundSneak = new TalkImpl();
        dzTalkFoundSneak.speak(sinbad, getOther("talkDZSneak.boss1"));
        dzTalkFoundSneak.speak(sinbad, getOther("talkDZSneak.boss2"));
        dzTalkFoundSneak.addListener(new TalkListener() {
            @Override
            public void onTalkEnd() {
                startEnemyOffensive();
            }
        });
        gameNetwork.talk(dzTalkFoundSneak);
    }
    
    private void startDZTalk() {
        dzTalk = new TalkImpl();
        dzTalk.speak(sinbad, getOther("talkDZ.boss1"));
        dzTalk.speak(sinbad, getOther("talkDZ.boss1"));
        dzTalk.speak(ninjia1, getOther("talkDZ.boyA1"));
        dzTalk.face(sinbad, ninjia1, false);
        dzTalk.speak(sinbad, getOther("talkDZ.boss2"));
        dzTalk.speak(sinbad, getOther("talkDZ.boss3"));
        dzTalk.speak(ninjia1, getOther("talkDZ.boyA2"));
        dzTalk.speak(bear, getOther("talkDZ.bear"));
        dzTalk.face(ninjia2, bear, false);
        dzTalk.speak(ninjia2, getOther("talkDZ.boyB1"));
        dzTalk.speak(ninjia1, getOther("talkDZ.boyA3"));
        dzTalk.speak(sinbad, getOther("talkDZ.boss4"));
        dzTalk.speak(sinbad, getOther("talkDZ.boss5"));
        dzTalk.addListener(new TalkListener() {
            @Override
            public void onTalkEnd() {
                startEnemyOffensive();
            }
        });
        gameNetwork.talk(dzTalk);
    }
    
    private boolean checkToDzTalk() {
        if (sinbad != null 
                && ninjia1 != null && ninjia2 != null && bear != null) {
            if (actorService.distance(altar, player) < 60) {
                return true;
            }
        }
        return false;
    }
    
    private String get(String rid, Object... param) {
        return ResourceManager.get(rid, param);
    }
    
    private String getOther(String rid, Object... param) {
//        return ResourceManager.getOther("resource_guard", rid, param);
        return ResourceManager.get("storyGuard." +  rid, param);
    }
    
    private void setSelfActor(Entity actor, boolean isTower) {
        actorService.setLocation(actor, getRandomPosition(game.getSelfPosition()));
        gameNetwork.setGroup(actor, gameService.getGroup(player));
        if (isTower) {
            gameNetwork.setLevel(actor, maxLevel);
        } else {
            calculateLevel(actor);
            createOffensiveLogic(actor, altar);
        }
    }
    
    private void setEnemyActor(Entity actor, boolean isTower) {
        actorService.setLocation(actor, getRandomPosition(game.getEnemyPosition()));
        gameNetwork.setGroup(actor, StoryGuardGame.GROUP_ENEMY);
        if (isTower) {
            gameNetwork.setLevel(actor, maxLevel);
        } else {
            calculateLevel(actor);
            if (offensiveEnemy) {
                createOffensiveLogic(actor, gb);
            }
        }
    }
    
    // 开启敌人主动进攻
    private void startEnemyOffensive() {
        offensiveEnemy = true;
        // 当切换模式时需要将当前在场景中的所有敌人的逻辑改变为进攻性的
        // 让所有手下跟着sinbad，而sinbad跟着gb
        List<Entity> enemys = findBiologyActors(StoryGuardGame.GROUP_ENEMY, null);
        for (Entity e : enemys) {
            createOffensiveLogic(e, gb);
        }
    }
    
    // 找出生物类角色
    private List<Entity> findBiologyActors(int group, List<Entity> store) {
        if (store == null) {
            store = new ArrayList<Entity>();
        }
        List<Actor> all = game.getScene().getEntities(Actor.class, null);
        for (Entity actor : all) {
            if (gameService.getGroup(actor) == group && gameService.isBiology(actor)) {
                 store.add(actor);
            }
        }
        return store;
    }
    
    /**
     * 创建进攻性的角色逻辑
     * @param actor 进攻的角色
     * @param fightTarget 被进攻的目标 
     */
    private void createOffensiveLogic(Entity actor, Entity fightTarget) {
        logicService.clearLogics(actor);
        logicService.addLogic(actor, (Logic) Loader.load(IdConstants.LOGIC_SEARCH_ENEMY));
        logicService.addLogic(actor, (Logic) Loader.load(IdConstants.LOGIC_FOLLOW));
        logicService.addLogic(actor, (Logic) Loader.load(IdConstants.LOGIC_FIGHT));
        gameService.setFollow(actor, fightTarget.getData().getUniqueId()); 
    }
    
    // 为角色计算一个等级
    private void calculateLevel(Entity actor) {
        int currentLevel = (int) (time / timeForUpLevel);
        currentLevel = MathUtils.clamp(currentLevel, minLevel, maxLevel);
        if (Config.debug) {
            LOG.log(Level.INFO, "calculateLevel={0}, time={1}", new Object[] {currentLevel, time});
        }
        String actorId = actor.getData().getId();
        if (actorId.equals(IdConstants.ACTOR_SINBAD)) {
            gameNetwork.setLevel(actor, currentLevel + bossLevelUp);
        } else {
            gameNetwork.setLevel(actor, currentLevel);
        }
    }
    
    /**
     * 在祭坛周围随机获得一个用于刷新敌人的地点。
     * @return 
     */
    private Vector3f getRandomPosition(Vector3f center) {
        Vector3f pos = MathUtils.getRandomPosition(center, 7, 12, null);
        Vector3f terrainHeight = playService.getTerrainHeight(game.getScene(), pos.x, pos.z);
        if (terrainHeight != null) {
            pos.set(terrainHeight).addLocal(0, 0.2f, 0);
        }
        return pos;
    }
    
    // 杀死所有敌方角色，在对方主基挂掉后调用
    private void killAllEnemies() {
        List<Actor> actors = game.getScene().getEntities(Actor.class, null);
        for (Entity actor : actors) {
            if (gameService.getGroup(actor) == StoryGuardGame.GROUP_ENEMY
                    && !gameService.isDead(actor)) {
                gameNetwork.kill(actor);
            }
        }
    }
    
    // 用于检查盗贼对话过程中是否发现player,以便打断对话
    private class PlayerChecker extends AbstractGameLogic {
        private boolean result;
        private final List<Actor> tempStore = new ArrayList<Actor>();
        public PlayerChecker() {
            super(1);
        }

        @Override
        protected void logicInit(Game game) {}
        
        @Override
        protected void logicUpdate(float tpf) {
            tempStore.clear();
            game.getScene().getEntities(Actor.class, tempStore);
            for (Actor a : tempStore) {
                if (gameService.getGroup(a) == StoryGuardGame.GROUP_ENEMY) {
                    if (gameService.getTarget(a) == player.getEntityId()) {
                        result = true;
                        return;
                    }
                }
            }
            result = false;
        }
    }
    
    private class EndChecker extends AbstractGameLogic {

        // 0:none; 1:failure; 2:success
        private int result;
        
        public EndChecker() {
            super(1);
        }

        @Override 
        protected void logicInit(Game game) {} 
    
        @Override
        protected void logicUpdate(float tpf) {
            if (gameService.isDead(gb)) {
                result = 1;
            } else if (gameService.isDead(altar)){
                result = 2;
            }
        }
        
        public boolean isFailure() {
            return result == 1;
        }
        
        public boolean isSuccess() {
            return result == 2;
        }
    }
}
