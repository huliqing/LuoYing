/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game.story;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.view.talk.Talk;
import name.huliqing.ly.view.talk.TalkImpl;
import name.huliqing.ly.view.talk.TalkListener;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.logic.scene.ActorLoadHelper;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.game.Game;
import name.huliqing.luoying.object.gamelogic.AbstractGameLogic;
import name.huliqing.luoying.ui.Button;
import name.huliqing.luoying.ui.TextPanel;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UI.Corner;
import name.huliqing.luoying.ui.UI.Listener;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 * 宝箱任务第一阶段:寻找宝箱
 * @author huliqing
 */
public class StoryTreasureTask1 extends AbstractTaskStep {
    private final PlayService playService = Factory.get(PlayService.class);
//    private final StateService stateService = Factory.get(StateService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final GameService gameService = Factory.get(GameService.class);
    
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
//    private final StateNetwork stateNetwork = Factory.get(StateNetwork.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    
    // 宝箱
    private final StoryTreasureGame _game;
    private Entity player;
    private Entity spider;
    private Entity victim;
    private Entity treasure;
    
    // loader
    private ActorLoadHelper treasureLoader;
    private ActorLoadHelper victimLoader;
    private ActorLoadHelper spiderLoader;
    
    // help!help!
    private Talk victimTalkHelp;
    private Talk victimTalk;
    
    // 任务开始提示
    private TextPanel task1Start;
    
    private boolean conditionTreasureFound;
    private boolean finished;
    
    private boolean paused;
    private int stage;
    
    private Helper helper;
    
    public StoryTreasureTask1(StoryTreasureGame game) {
        this._game = game;
    }

    @Override
    protected void doInit(TaskStep previous) {
        stage = 0;
        finished = false;
        paused = false;
        conditionTreasureFound = false;
        helper = new Helper();
        player = _game.getPlayer();
        gameNetwork.setGroup(player, _game.groupPlayer);
        
        treasureLoader = new ActorLoadHelper(IdConstants.ACTOR_TREASURE){
            @Override 
            public void callback(Entity actor) {
                treasure = actor;
                actorService.setLocation(treasure, _game.treasurePos);
                gameService.setLevel(treasure, _game.treasureLevel);
                gameService.setGroup(treasure, _game.groupPlayer);
                playNetwork.addEntity(treasure);
            }
        };
        
        victimLoader = new ActorLoadHelper(IdConstants.ACTOR_AILIN) {
            @Override
            public void callback(Entity actor) {
                victim = actor;
                actorService.setLocation(victim, _game.treasurePos.clone().addLocal(1, 0, 1));
                gameService.setGroup(victim, _game.groupPlayer);
                gameService.setEssential(victim, true);// 设置为"必要的",这样不会被移除出场景
                victim.addObjectData(Loader.loadData(IdConstants.STATE_SAFE), 1);
                
//                skillService.playSkill(actor, skillService.getSkill(actor, SkillType.wait), false);
                playNetwork.addEntity(victim);
                
                // 救命
                victimTalkHelp = new TalkImpl();
            }
            
        };
        
        spiderLoader = new ActorLoadHelper(IdConstants.ACTOR_SPIDER) {
            @Override
            public void callback(Entity actor) {
                // 邪恶蜘蛛
                spider = actor;
                actorService.setLocation(spider, _game.treasurePos.add(2, 0, 2));
                gameService.setGroup(spider, _game.groupEnemy);
//                stateService.addState(spider, IdConstants.STATE_SAFE, null);
                spider.addObjectData(Loader.loadData(IdConstants.STATE_SAFE), 1);
                playNetwork.addEntity(spider);
            }
        };
        
        _game.addLogic(helper);
    }

    @Override
    protected void doLogic(float tpf) {
        if (finished || paused) {
            return;
        }
        
        if (stage == 0) {
            // 显示任务面板，开始调查任务
            doStartFindTreasure();
            return;
        } 
        
        if (stage == 1) {
            // 载入“艾琳”、“蜘蛛”
            _game.addLogic(treasureLoader);
            _game.addLogic(victimLoader);
            _game.addLogic(spiderLoader);
            stage = 2;
            return;
        } 
        
        if (stage == 2) {
            if (treasure != null && victim != null && spider != null) {
                stage = 3;
            }
            return;
        }
        
        
        if (stage == 3) {
            // 杀死蜘蛛、与艾琳的对话逻辑
            doSaveVictim();
            if (gameService.isDead(victim) && gameService.isDead(spider) && actorService.distance(victim, player) < 15) {
                stage = 4;
            }
            return;
        } 
        
        if (stage == 4) {
            // 与艾琳的对话 talk
            doTalkVictim();
            stage = 5;
        }
        
        // remove20160615
//        // help 
//        if (stage <= 4) {
//            helper.update(tpf);
//        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    /**
     * 获得宝箱物体
     * @return 
     */
    public Entity getTreasure() {
        return treasure;
    }
    
    private String get(String rid) {
//        return ResourceManager.getOther("resource_treasure", rid);
        return ResourceManager.get("storyTreasure." +  rid);
    }
    
    private void doStartFindTreasure() {
        // 任务开始提示面板
        task1Start = new TextPanel(get("task1.title")
            , playService.getScreenWidth() * 0.5f
                , playService.getScreenHeight() * 0f);
        Button startBtn = new Button(get("task1.confirm"));
        startBtn.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    stage = 1;
                    paused = false;
                    task1Start.removeFromParent();
                }
            }
        });
        task1Start.addText(get("task1.content"));
        task1Start.addButton(startBtn);
        task1Start.setCloseable(false);
        task1Start.setDragEnabled(true);
        task1Start.resize();
        task1Start.setToCorner(Corner.CC);
        
//        playService.addObject(task1Start, true);
        UIState.getInstance().addUI(task1Start);
        
        paused = true;
    }
    
    private void doSaveVictim() {
        // 找到宝箱
        if (!conditionTreasureFound && actorService.distance(player, treasure) <= 10) {
            conditionTreasureFound = true;
            gameService.addMessage(get("task1.found"), MessageType.info);
        }
        
        // 1.目标始终
        Entity target = gameService.isDead(victim) ? player : victim;
        if (gameService.getTarget(spider) != target.getEntityId()) {
            gameNetwork.setTarget(spider, target.getEntityId());
        }
        
        // 2.玩家接近时修改victim防御值,让她受伤而死
        if (!gameService.isDead(victim) && actorService.distance(victim, player) <= 25 
                && victim.getData().getObjectData(IdConstants.STATE_SAFE) != null) {
            entityNetwork.removeObjectData(victim, victim.getData().getObjectData(IdConstants.STATE_SAFE).getUniqueId(), 1);
        }
        // 3.如果受害者已死，则降低蜘蛛防御。
        if (!gameService.isDead(spider) && gameService.isDead(victim) 
                && spider.getData().getObjectData(IdConstants.STATE_SAFE) != null) {
            entityNetwork.removeObjectData(spider, spider.getData().getObjectData(IdConstants.STATE_SAFE).getUniqueId(), 1);
        }
        
        if (!gameService.isDead(victim)) {
            if (victimTalkHelp.isEnd()) {
                victimTalkHelp.delay(3f);
                gameNetwork.speak(victim, get("talk1.aiLin.help"), 0);
                gameNetwork.talk(victimTalkHelp);
            }
        }
    }
    
    private void doTalkVictim() {
        if (victimTalk == null) {
            victimTalk = new TalkImpl();
            victimTalk.delay(0.5f);
            victimTalk.speak(victim, get("talk2.aiLin1"));
            victimTalk.delay(0.2f);
            victimTalk.speak(victim, get("talk2.aiLin2"));
            victimTalk.delay(0.2f);
            victimTalk.speak(victim, get("talk2.aiLin3"));
            victimTalk.delay(0.2f);
            victimTalk.speak(victim, get("talk2.aiLin4"));
            victimTalk.delay(0.5f);
            victimTalk.addListener(new TalkListener() {
                @Override
                public void onTalkEnd() {
                    // 让victim允许被清理出场景
                    gameService.setEssential(victim, false);
                    // 结束当前任务
                    finished = true;
                    stage = 4;
                }
            });
            gameNetwork.talk(victimTalk);
        }
    }
    
    // 用于提供帮助信息
    private class Helper extends AbstractGameLogic {
        private int index;
        
        public Helper() {
            super(3);
        }

        @Override
        protected void logicInit(Game game) {}

        @Override
        protected void logicUpdate(float tpf) {
            if (index == 0) {
                gameService.addMessage(get("help.run"), MessageType.info);
                index++;
            } else if (index == 1) {
                gameService.addMessage(get("help.rotate"), MessageType.info);
                index++;
            } else if (index == 2) {
                gameService.addMessage(get("help.scale"), MessageType.info);
                index++;
            } else if (index == 3) {
                gameService.addMessage(get("help.userPanel"), MessageType.info);
                index++;
            } else {
                // 退出
                game.removeLogic(this);
            }
        }
        
    }
}
