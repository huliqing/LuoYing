/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.game.impl;

import name.huliqing.core.Factory;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.enums.SkillType;
import name.huliqing.ly.manager.talk.Talk;
import name.huliqing.ly.manager.talk.TalkImpl;
import name.huliqing.ly.manager.talk.TalkListener;
import name.huliqing.core.game.network.ActorNetwork;
import name.huliqing.core.game.network.PlayNetwork;
import name.huliqing.core.game.network.StateNetwork;
import name.huliqing.core.game.service.ActorService;
import name.huliqing.core.game.service.PlayService;
import name.huliqing.core.game.service.SkillService;
import name.huliqing.core.game.service.StateService;
import name.huliqing.ly.logic.scene.ActorLoadHelper;
import name.huliqing.core.object.gamelogic.AbstractGameLogic;
import name.huliqing.core.ui.Button;
import name.huliqing.core.ui.TextPanel;
import name.huliqing.core.ui.UI;
import name.huliqing.core.ui.UI.Corner;
import name.huliqing.core.ui.UI.Listener;

/**
 * 宝箱任务第一阶段:寻找宝箱
 * @author huliqing
 */
public class StoryTreasureTask1 extends GameTaskBase {
    private final PlayService playService = Factory.get(PlayService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final StateNetwork stateNetwork = Factory.get(StateNetwork.class);
    
    // 宝箱
    private final StoryTreasureGame game;
    private Actor player;
    private Actor spider;
    private Actor victim;
    private Actor treasure;
    
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
    private float stage;
    
    private Helper helper;
    
    public StoryTreasureTask1(StoryTreasureGame game) {
        this.game = game;
    }

    @Override
    protected void doInit(GameTask previous) {
        stage = 0;
        finished = false;
        paused = false;
        conditionTreasureFound = false;
        helper = new Helper();
        player = playService.getPlayer();
        actorNetwork.setGroup(player, game.groupPlayer);
        
        treasureLoader = new ActorLoadHelper(IdConstants.ACTOR_TREASURE){
            @Override
            public void callback(Actor actor) {
                treasure = actor;
                treasure.setLocation(game.treasurePos);
                actorService.setLevel(treasure, game.treasureLevel);
                actorService.setGroup(treasure, game.groupPlayer);
                playNetwork.addActor(treasure);
            }
        };
        
        victimLoader = new ActorLoadHelper(IdConstants.ACTOR_AILIN) {
            @Override
            public void callback(Actor actor) {
                victim = actor;
                victim.setLocation(game.treasurePos.clone().addLocal(1, 0, 1));
                actorService.setGroup(victim, game.groupPlayer);
                stateService.addState(victim, IdConstants.STATE_SAFE, null);
                actorService.setEssential(victim, true);// 设置为"必要的",这样不会被移除出场景
                skillService.playSkill(actor, skillService.getSkill(actor, SkillType.wait).getId(), false);
                playNetwork.addActor(victim);
                
                // 救命
                victimTalkHelp = new TalkImpl();
            }
            
        };
        
        spiderLoader = new ActorLoadHelper(IdConstants.ACTOR_SPIDER) {
            @Override
            public void callback(Actor actor) {
                // 邪恶蜘蛛
                spider = actor;
                spider.setLocation(game.treasurePos.add(2, 0, 2));
                actorService.setGroup(spider, game.groupEnemy);
                stateService.addState(spider, IdConstants.STATE_SAFE, null);
                playNetwork.addActor(spider);
            }
        };
        
        game.addLogic(helper);
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
            game.addLogic(treasureLoader);
            game.addLogic(victimLoader);
            game.addLogic(spiderLoader);
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
            if (victim.isDead() && spider.isDead() && victim.getDistance(player) < 15) {
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
    public Actor getTreasure() {
        return treasure;
    }
    
    private String get(String rid) {
        return ResourceManager.getOther("resource_treasure", rid);
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
                    playService.removeObject(task1Start);
                }
            }
        });
        task1Start.addText(get("task1.content"));
        task1Start.addButton(startBtn);
        task1Start.setCloseable(false);
        task1Start.setDragEnabled(true);
        task1Start.resize();
        task1Start.setToCorner(Corner.CC);
        
        playService.addObject(task1Start, true);
        
        paused = true;
    }
    
    private void doSaveVictim() {
        // 找到宝箱
        if (!conditionTreasureFound && player.getDistance(treasure.getModel().getWorldTranslation()) <= 10) {
            conditionTreasureFound = true;
            playService.addMessage(get("task1.found"), MessageType.info);
        }
        
        // 1.目标始终
        Actor target = victim.isDead() ? player : victim;
        if (actorService.getTarget(spider) != target) {
            actorNetwork.setTarget(spider, target);
        }
        
        // 2.玩家接近时修改victim防御值,让她受伤而死
        if (!victim.isDead() && victim.getDistance(player) <= 25 && stateService.existsState(victim, IdConstants.STATE_SAFE)) {
            stateNetwork.removeState(victim, IdConstants.STATE_SAFE);
        }
        // 3.如果受害者已死，则降低蜘蛛防御。
        if (!spider.isDead() && victim.isDead() && stateService.existsState(spider, IdConstants.STATE_SAFE)) {
            stateNetwork.removeState(spider, IdConstants.STATE_SAFE);
        }
        
        if (!victim.isDead()) {
            if (victimTalkHelp.isEnd()) {
                victimTalkHelp.delay(3f);
                actorNetwork.speak(victim, get("talk1.aiLin.help"), 0);
                actorNetwork.talk(victimTalkHelp);
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
                    actorService.setEssential(victim, false);
                    // 结束当前任务
                    finished = true;
                    stage = 4;
                }
            });
            actorNetwork.talk(victimTalk);
        }
    }
    
    // 用于提供帮助信息
    private class Helper extends AbstractGameLogic {
        private int index;
        
        public Helper() {
            this.interval = 3;
        }

        @Override
        protected void doLogic(float tpf) {
            if (index == 0) {
                playService.addMessage(get("help.run"), MessageType.info);
                index++;
            } else if (index == 1) {
                playService.addMessage(get("help.rotate"), MessageType.info);
                index++;
            } else if (index == 2) {
                playService.addMessage(get("help.scale"), MessageType.info);
                index++;
            } else if (index == 3) {
                playService.addMessage(get("help.userPanel"), MessageType.info);
                index++;
            } else {
                // 退出
                game.removeLogic(this);
            }
        }
        
    }
}
