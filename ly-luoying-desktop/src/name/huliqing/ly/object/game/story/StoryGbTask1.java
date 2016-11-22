/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game.story;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Config;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.ly.view.talk.Talk;
import name.huliqing.ly.view.talk.TalkImpl;
import name.huliqing.ly.view.talk.TalkListener;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.network.SkillNetwork;
import name.huliqing.luoying.layer.network.StateNetwork;
import name.huliqing.luoying.layer.service.ActionService;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.logic.scene.ActorMultLoadHelper;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.ui.Button;
import name.huliqing.luoying.ui.TextPanel;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UI.Corner;
import name.huliqing.luoying.ui.UI.Listener;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.ly.object.game.StoryServerNetworkRpgGame;

/**
 * 寻找古柏
 * @author huliqing
 */
public class StoryGbTask1 extends AbstractTaskStep {
    private final ActorService actorService = Factory.get(ActorService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActionService actionService = Factory.get(ActionService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
//    private final LogicService logicService = Factory.get(LogicService.class);
    
    private final GameService gameService = Factory.get(GameService.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
//    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
//    private final StateNetwork stateNetwork = Factory.get(StateNetwork.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    
    // 开始任务面板:说明任务信息
    private TextPanel taskFind;
    // 任务窗口:选择难度
    private TextPanel taskSave;
    // 任务面板：显示已经回收的树根的数量
    private StoryGbTaskLogic storyGbTaskLogic;
    
    // 回收的子孙根数选择
    private int[] saveList = new int[]{15, 30, 45};
    
    // 敌我角色载入器
    private ActorMultLoadHelper loader;
    // 古柏及幼仔，敌人
    private Entity gb;
    private final List<Entity> gbSmalls = new ArrayList<Entity>(2);
    private final List<Entity> enemies =  new ArrayList<Entity>(3);
    // 谈话
    private Talk talk1; // 1~4 enemy
    private Talk talk2;
    private Talk talk3;
    private Talk talk5; // gb talk xy
    private Talk talk6; // book
    
    // 标记是否接受了任务
    private boolean taskStarted;
    // 是否已经获得魔法书
    private boolean gotBook;
    
    // ---- 内部参数
    private Entity player;
    // 当前任务阶段
    private int stage;
    // 任务是否完成
    private boolean finished;
    // 选择回收多少个
    private int saveTotal;
    
    private final StoryGbGame game;
    
    public StoryGbTask1(StoryGbGame game) {
        this.game = game;
    }

    @Override
    protected void doInit(TaskStep previous) {
        player = game.getPlayer();

        // 任务开始提示面板
        taskFind = new TextPanel(get("taskFind.title"), playService.getScreenWidth() * 0.75f, playService.getScreenHeight() * 0f);
        taskFind.addText(get("taskFind.content"));
        taskFind.setCloseable(false);
        taskFind.setToCorner(Corner.CC);
        taskFind.setDragEnabled(true);
        Button startBtn = new Button(get("taskFind.confirm"));
        startBtn.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    stage = 1;
//                    game.getScene().removeObject(taskFind);
                    taskFind.removeFromParent();
                }
            }
        });
        taskFind.addButton(startBtn);
//        playService.addObject(taskFind, true);
        UIState.getInstance().addUI(taskFind);
        
        // 古柏和敌人载入器
        enemies.clear();
        
        loader = new ActorMultLoadHelper(
                  IdConstants.ACTOR_NINJA, IdConstants.ACTOR_NINJA, IdConstants.ACTOR_NINJA // 0,1,2 敌人
                , IdConstants.ACTOR_GB    // 3 古柏
                , IdConstants.ACTOR_GB_SMALL, IdConstants.ACTOR_GB_SMALL ){ // 4,5 古柏幼仔
        
            @Override
            public void callback(Entity actor, int loadIndex) {
                // 敌人
                if (loadIndex >= 0 && loadIndex <= 2) {
                    gameService.setLevel(actor, 6);
                    gameService.setGroup(actor, game.groupEnemy);
                    enemies.add(actor);
//                    if (loadIndex == 0) {
//                        gameService.setColor(actor, new ColorRGBA(1, 1, 3f, 1));
//                    }
                }
                // gb
                if (loadIndex == 3) {
                    gb = actor;
                    gameService.setLevel(gb, 15);
                    gameService.setGroup(gb, StoryServerNetworkRpgGame.GROUP_PLAYER);
                }
                // gbsmall
                if (loadIndex >= 4 && loadIndex <= 5) {
                    gameService.setLevel(actor, 2);
                    gameService.setGroup(actor, StoryServerNetworkRpgGame.GROUP_PLAYER);
                    gbSmalls.add(actor);
                }
                actorService.setLocation(actor, game.getGbPosition());
                // 保护角色不死
                setProtected(actor, true);
                playNetwork.addEntity(actor);
            }
        };
        
        if (Config.debug) {
            saveList = new int[]{1, 3, 5};
        }
    }

    @Override
    protected void doLogic(float tpf) {
        // 0.逻辑：显示任务面板
        if (stage == 0) {
            // donothing
            return;
        }
        
        // 1.载入敌我角色,并初始化对话
        if (stage == 1) {
            loader.update(tpf);
            if (gb != null && gbSmalls.size() >= 2 && enemies.size() >= 3) {
                // 分配不同的敌对方
                createFightTarget();
                // 创建并准备好对话(不是立即执行)
                createEnemyTalk();
                stage = 2;
            }
            return;
        }
        
        // 2.检测角色是否走近
        if (stage == 2) {
            if (checkPlayerNear()) {
                setProtected(gbSmalls, false);
                gameNetwork.talk(talk1);
                gameNetwork.talk(talk2);
                gameNetwork.talk(talk3);
                stage = 3;
            }
            return;
        }
        
        // 3.检测谈话结束，后允许player动手救人
        if (stage == 3) {
            if (talk1.isEnd()) {
                setProtected(enemies, false);
                setTarget(enemies.get(0), player, false);
                stage = 4;
            }
            return;
        }
        
        // 4.测试敌人是否全部已死
        if (stage == 4) {
            if (checkAllEnemyDead() && !gameService.isDead(player)) {
                stage = 5;
            }
            return;
        }
        
        // 5.古柏与小樱的对话
        if (stage == 5) {
            // 不让乱动，不然在对话的时候会执行idle行为
            gameService.setAutoLogic(gb, false);
            actionService.playAction(gb, null);
            skillNetwork.playSkill(gb, skillService.getSkillWaitDefault(gb), true);
            
            createGbPlayerTalk();
            stage = 6;
        }
        
        // 6.弹出任务窗口
        if (stage == 6) {
            if (talk5.isEnd()) {
                createTaskPanel();
                stage = 7;
            }
        }
        
        // 7.接受了任务,古柏赠书
        if (stage == 7) {
            if (taskStarted == true) {
                // 显示任务提示面板
                displayTaskPanel();
                
                // 送书
                createGbGiveBookTalk();
                
                stage = 8;
            }
        }
        
        if (stage == 8) {
            if (gotBook) {
                gameService.setAutoLogic(gb, true);
                finished = true;
                stage = 9;
            }
        }
    }
    
    public StoryGbTaskLogic getTaskPanel() {
        return storyGbTaskLogic;
    }
    
    // 任务显示面板
    private void displayTaskPanel() {
        storyGbTaskLogic = new StoryGbTaskLogic(saveTotal, player);

        game.addLogic(storyGbTaskLogic);
    }
    
    // 古柏赚书对话
    private void createGbGiveBookTalk() {
        talk6 = new TalkImpl();
        talk6.face(gb, player, true);
        talk6.speak(gb, get("talk6.gb1"));
        talk6.speak(gb, get("talk6.gb2"));
        talk6.addListener(new TalkListener() {
            @Override
            public void onTalkEnd() {
                entityNetwork.addObjectData(player, Loader.loadData(IdConstants.ITEM_BOOK_006), 1);
                gotBook = true;
            }
        });
        gameNetwork.talk(talk6);
    }
    
    // 创建任务窗口
    private void createTaskPanel() {
        taskSave = new TextPanel(get("taskSave.title")
                , playService.getScreenWidth() * 0.75f
                , playService.getScreenHeight() * 0f);
        taskSave.addText(get("taskSave.content"));
        taskSave.setCloseable(false);
        taskSave.setToCorner(Corner.CC);
        taskSave.setDragEnabled(true);
        taskSave.addButton(get("taskSave.confirm", saveList[0]), new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    saveTotal = saveList[0];
                    taskStarted = true;
//                    playService.removeObject(taskSave);
                    taskSave.removeFromParent();
                }
            }
        });
        taskSave.addButton(get("taskSave.confirm", saveList[1]), new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    saveTotal = saveList[1];
                    taskStarted = true;
//                    playService.removeObject(taskSave);
                    taskSave.removeFromParent();
                }
            }
        });
        taskSave.addButton(get("taskSave.confirm", saveList[2]), new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    saveTotal = saveList[2];
                    taskStarted = true;
//                    playService.removeObject(taskSave);
                    taskSave.removeFromParent();
                }
            }
        });
//        playService.addObject(taskSave, true);
        UIState.getInstance().addUI(taskSave);
    }
    
    // 古柏与小樱的对话，详明被袭击原因
    private void createGbPlayerTalk() {
        talk5 = new TalkImpl();
        talk5.face(gb, player, true);
        talk5.speak(gb, get("talk5.gb1"));
        talk5.speak(gb, get("talk5.gb2"));
        talk5.face(player, gb, true);
        talk5.speak(player, get("talk5.xy1"));
        talk5.speak(gb, get("talk5.gb3"));
        talk5.speak(gb, get("talk5.gb4"));
        talk5.face(player, gb, true);
        talk5.speak(player, get("talk5.xy2"));
        talk5.speak(gb, get("talk5.gb5"));
        talk5.speak(gb, get("talk5.gb6"));
        talk5.speak(gb, get("talk5.gb7"));
        talk5.speak(gb, get("talk5.gb8"));
        talk5.face(player, gb, true);
        talk5.speak(player, get("talk5.xy3"));
        talk5.face(gb, player, true);
        talk5.speak(gb, get("talk5.gb9"));
        talk5.speak(gb, get("talk5.gb10"));
        talk5.face(player, gb, true);
        talk5.speak(player, get("talk5.xy4"));
        talk5.face(gb, player, true);
        talk5.speak(gb, get("talk5.gb11"));
        gameNetwork.talk(talk5);
    }
    
    // 检测角色是否走近
    private boolean checkPlayerNear() {
        return player.getSpatial().getWorldTranslation().distance(gb.getSpatial().getWorldTranslation()) < 15;  
    }
    
    private void createFightTarget() {
        setTarget(gb, enemies.get(0), true);
        setTarget(gbSmalls.get(0), enemies.get(1), true);
        setTarget(gbSmalls.get(1), enemies.get(2), true);
    }
    
    private void createEnemyTalk() {
        Entity e0 = enemies.get(0);
        Entity e1 = enemies.get(1);
        Entity e2 = enemies.get(2);
        
        gameNetwork.setTarget(e0, gb.getEntityId());
        
        talk1 = new TalkImpl();
        talk1.face(e0, gb, true);
        talk1.speak(e0, get("talk1.enemy1"));
        talk1.delay(3);
        talk1.speak(gb, get("talk1.gb1"));
        talk1.speak(e0, get("talk1.enemy2"));
        talk1.delay(3);
        
        talk2 = new TalkImpl();
        talk2.delay(3);
        talk2.speak(e1, get("talk2.enemy1"));
        
        talk3 = new TalkImpl();
        talk3.delay(13);
        talk3.speak(e2, get("talk3.enemy1"));
    }

    
    // =========================================================================
    
    @Override
    public boolean isFinished() {
        return finished;
    }
    
    private String get(String rid, Object... param) {
        return ResourceManager.get("storyGb." + rid, param);
    }
    
    // 设置角色为保护状态或非保护状态
    private void setProtected(Entity actor, boolean bool) {
        if (bool) {
            entityNetwork.addObjectData(actor, Loader.loadData(IdConstants.STATE_SAFE), 1);
        } else {
            ObjectData safeState = actor.getData().getObjectData(IdConstants.STATE_SAFE);
            entityNetwork.removeObjectData(actor, safeState.getUniqueId(), 1);
        }
    }
    
    private void setProtected(List<Entity> actors, boolean bool) {
        for (Entity a : actors) {
            setProtected(a, bool);
        }
    }
    
    // 将actor的目标设置为enemy,如果each为true,则同时将enemy的目标设置为actor
    private void setTarget(Entity actor, Entity target, boolean each) {
        gameNetwork.setTarget(actor, target.getEntityId());
        if (each) {
            gameNetwork.setTarget(target, actor.getEntityId());
        }
    }
    
    // 检测是否所有敌人已死
    private boolean checkAllEnemyDead() {
        for (Entity a : enemies) {
            if (!gameService.isDead(a)) {
                return false;
            }
        }
        return true;
    }
}
