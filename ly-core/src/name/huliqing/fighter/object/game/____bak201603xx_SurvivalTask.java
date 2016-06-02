///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.game;
//
//import com.jme3.util.TempVars;
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.object.actor.Actor;
//import name.huliqing.fighter.constants.IdConstants;
//import name.huliqing.fighter.constants.ResConstants;
//import name.huliqing.fighter.enums.MessageType;
//import name.huliqing.fighter.game.network.ActorNetwork;
//import name.huliqing.fighter.game.network.PlayNetwork;
//import name.huliqing.fighter.game.network.SkillNetwork;
//import name.huliqing.fighter.game.network.StateNetwork;
//import name.huliqing.fighter.game.service.ActorService;
//import name.huliqing.fighter.game.service.LogicService;
//import name.huliqing.fighter.game.service.PlayService;
//import name.huliqing.fighter.game.service.SkillService;
//import name.huliqing.fighter.game.service.StateService;
//import name.huliqing.fighter.game.service.ViewService;
//import name.huliqing.fighter.loader.Loader;
//import name.huliqing.fighter.object.logic.PositionLogic;
//import name.huliqing.fighter.logic.scene.ActorBuildLogic;
//import name.huliqing.fighter.logic.scene.ActorBuildLogic.Callback;
//import name.huliqing.fighter.logic.scene.ActorBuildLogic.ModelLoader;
//import name.huliqing.fighter.manager.ResourceManager;
//import name.huliqing.fighter.object.view.TextView;
//
///**
// * 宝箱任务第二阶段：守护宝箱
// * @author huliqing
// */
//public class SurvivalTask extends GameTaskBase {
//    private boolean debug = false;
//    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
//    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
//    private final StateNetwork stateNetwork = Factory.get(StateNetwork.class);
//    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
//    private final PlayService playService = Factory.get(PlayService.class);
//    private final StateService stateService = Factory.get(StateService.class);
//    private final LogicService logicService = Factory.get(LogicService.class);
//    private final ActorService actorService = Factory.get(ActorService.class);
//    private final SkillService skillService = Factory.get(SkillService.class);
//    private final ViewService viewService = Factory.get(ViewService.class);
//    
//    // 任务位置
//    private SurvivalGame game;
//    
//    // 怪物刷新器及刷新位置
//    private ActorBuildLogic sceneBuilder;
//    private SurvivalLevelLogic levelLogic;
//    
//    // 宝箱角色
//    private Actor treasure;
//    
//    // 当前任务阶段
//    private int stage;
//    
//    // 当前游戏的运行时间,单位秒
//    private float timeUsed;
//    
//    // 当前的敌人等级
//    private int level;
//    
//    /**
//     * @param playState
//     * @param targetPos 怪物的集合地点
//     */
//    public SurvivalTask(SurvivalGame game) {
//        this.game = game;
//    }
//    
//    @Override
//    protected void doInit(GameTask previous) {
//        treasure = actorService.loadActor(IdConstants.ACTOR_TREASURE);
//        treasure.setLocation(game.treasurePos);
//        actorService.setGroup(treasure, game.SELF_GROUP);
//        actorService.setTeam(treasure, actorService.getTeam(playService.getPlayer()));
//        playNetwork.addActor(treasure);
//        
//        sceneBuilder = new ActorBuildLogic();
//        // remove0225
////        sceneBuilder.setModelLoader(new ModelLoader() {
////            @Override
////            public Actor load(String actorId) {
////                Actor actor = actorService.loadActor(actorId);
////                return actor;
////            }
////        });
//        sceneBuilder.setCallback(new Callback() {
//            @Override
//            public Actor onAddBefore(Actor actor) {
//                actorService.setGroup(actor, game.GROUP_ENEMY);
//                skillService.playWait(actor, null, false);
//
//                TempVars tv = TempVars.get();
//                tv.vect1.set(game.treasurePos);
//                tv.vect1.setY(playService.getTerrainHeight(tv.vect1.x, tv.vect1.z));
//                PositionLogic runLogic = (PositionLogic) Loader.loadLogic(IdConstants.LOGIC_POSITION);
//                runLogic.setInterval(3);
//                runLogic.setPosition(tv.vect1);
//                runLogic.setNearestDistance(game.nearestDistance);
//                logicService.addLogic(actor, runLogic);
//                tv.release();
//                
//                int newLevel = (int) (timeUsed / game.levelUpBySec);
//                // 等级达到最高之后不再刷新敌人
//                if (newLevel > game.maxLevel) {
//                    sceneBuilder.setEnabled(false);
//                    return null;
//                } 
//                
//                // 设置等级,并提示
//                if (newLevel > level) {
//                    level = newLevel;
//                    String message = ResourceManager.get(ResConstants.COMMON_LEVEL) + " " + level;
//                    playNetwork.addMessage(message, MessageType.warn);
//                    TextView levelView = (TextView) viewService.loadView(IdConstants.VIEW_TEXT_SUCCESS);
//                    levelView.setText(message);
//                    playNetwork.addView(levelView);
//                }
//                actorService.setLevel(actor, level < 1 ? 1 : level);
//                return actor;
//            }
//        });
//        
//        sceneBuilder.setRadius(game.nearestDistance);
//        sceneBuilder.setTotal(game.buildTotal);
//        sceneBuilder.addPosition(game.enemyPositions);
//        sceneBuilder.setInterval(3f);
//        sceneBuilder.addId(
//                IdConstants.ACTOR_NINJA
////                , IdConstants.ACTOR_SPIDER
////                , IdConstants.ACTOR_WOLF
////                , IdConstants.ACTOR_BEAR
////                , IdConstants.ACTOR_SCORPION
//                , IdConstants.ACTOR_RAPTOR
//                , IdConstants.ACTOR_TREX
//                );
//        
//        timeUsed = 0;
//        stage = 1;
//        playService.addObject(sceneBuilder, false);
//    }
//
//    @Override
//    protected void doLogic(float tpf) {
//        
//        // 任务逻辑
//        if (stage == 1) {
//            timeUsed += tpf;
//            if (treasure != null && treasure.isDead()) {
//                playNetwork.addMessage(get(ResConstants.COMMON_TASK_FAILURE), MessageType.warn);
//                playNetwork.addView(viewService.loadView(IdConstants.VIEW_TEXT_FAILURE));
//                stage = 999;
//            }
//            return;
//        } 
//        
//        if (stage == 999) {
//            // end
//        }
//    }
//
//    @Override
//    public boolean isFinished() {
//        return false;
//    }
//    
//    private String get(String rid, Object... params) {
//        if (params == null) {
//            return ResourceManager.get(rid);
//        } else {
//            return ResourceManager.get(rid, params);
//        }
//    }
//    
//}
