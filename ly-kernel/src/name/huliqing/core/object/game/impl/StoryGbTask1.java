/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.game.impl;

import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.Config;
import name.huliqing.core.Factory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.view.talk.Talk;
import name.huliqing.core.view.talk.TalkImpl;
import name.huliqing.core.view.talk.TalkListener;
import name.huliqing.core.mvc.network.ActorNetwork;
import name.huliqing.core.mvc.network.PlayNetwork;
import name.huliqing.core.mvc.network.ProtoNetwork;
import name.huliqing.core.mvc.network.SkillNetwork;
import name.huliqing.core.mvc.network.StateNetwork;
import name.huliqing.core.mvc.service.ActionService;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.ProtoService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.mvc.service.StateService;
import name.huliqing.core.logic.scene.ActorMultLoadHelper;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.ui.Button;
import name.huliqing.core.ui.TextPanel;
import name.huliqing.core.ui.UI;
import name.huliqing.core.ui.UI.Corner;
import name.huliqing.core.ui.UI.Listener;

/**
 * 寻找古柏
 * @author huliqing
 */
public class StoryGbTask1 extends GameTaskBase {
    private final ActorService actorService = Factory.get(ActorService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final ActionService actionService = Factory.get(ActionService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final StateNetwork stateNetwork = Factory.get(StateNetwork.class);
//    private final ItemNetwork itemNetwork = Factory.get(ItemNetwork.class);
    private final ProtoNetwork protoNetwork = Factory.get(ProtoNetwork.class);
    private final ProtoService protoService = Factory.get(ProtoService.class);
    
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
    private Actor gb;
    private final List<Actor> gbSmalls = new ArrayList<Actor>(2);
    private final List<Actor> enemies =  new ArrayList<Actor>(3);
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
    private Actor player;
    // 当前任务阶段
    private int stage;
    // 任务是否完成
    private boolean finished;
    // 选择回收多少个
    private int saveTotal;
    
    private StoryGbGame game;
    
    public StoryGbTask1(StoryGbGame game) {
        this.game = game;
    }

    @Override
    protected void doInit(GameTask previous) {
        player = playService.getPlayer();

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
                    playService.removeObject(taskFind);
                }
            }
        });
        taskFind.addButton(startBtn);
        playService.addObject(taskFind, true);
        
        // 古柏和敌人载入器
        enemies.clear();
        
        loader = new ActorMultLoadHelper(
                  IdConstants.ACTOR_NINJA, IdConstants.ACTOR_NINJA, IdConstants.ACTOR_NINJA // 0,1,2 敌人
                , IdConstants.ACTOR_GB    // 3 古柏
                , IdConstants.ACTOR_GB_SMALL, IdConstants.ACTOR_GB_SMALL ){ // 4,5 古柏幼仔
        
            @Override
            public void callback(Actor actor, int loadIndex) {
                // 敌人
                if (loadIndex >= 0 && loadIndex <= 2) {
                    actorService.setLevel(actor, 6);
                    actorService.setGroup(actor, game.groupEnemy);
                    enemies.add(actor);
                    if (loadIndex == 0) {
                        actorService.setColor(actor, new ColorRGBA(1, 1, 3f, 1));
                    }
                }
                // gb
                if (loadIndex == 3) {
                    gb = actor;
                    actorService.setLevel(gb, 15);
                    actorService.setGroup(gb, StoryGame.GROUP_PLAYER);
                }
                // gbsmall
                if (loadIndex >= 4 && loadIndex <= 5) {
                    actorService.setLevel(actor, 2);
                    actorService.setGroup(actor, StoryGame.GROUP_PLAYER);
                    gbSmalls.add(actor);
                }
                actorService.setLocation(actor, game.getGbPosition());
                // 保护角色不死
                setProtected(actor, true);
                playNetwork.addActor(actor);
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
                actorNetwork.talk(talk1);
                actorNetwork.talk(talk2);
                actorNetwork.talk(talk3);
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
            if (checkAllEnemyDead() && !actorService.isDead(player)) {
                stage = 5;
            }
            return;
        }
        
        // 5.古柏与小樱的对话
        if (stage == 5) {
            // 不让乱动，不然在对话的时候会执行idle行为
            actorService.setAutoAi(gb, false);
            actionService.playAction(gb, null);
            skillNetwork.playSkill(gb, skillService.getSkill(gb, SkillType.wait), true);
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
                actorService.setAutoAi(gb, true);
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
                
//                itemNetwork.addItem(player, IdConstants.ITEM_BOOK_006, 1);
                
                protoNetwork.addData(player, IdConstants.ITEM_BOOK_006, 1);
                
                gotBook = true;
            }
        });
        actorNetwork.talk(talk6);
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
                    playService.removeObject(taskSave);
                }
            }
        });
        taskSave.addButton(get("taskSave.confirm", saveList[1]), new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    saveTotal = saveList[1];
                    taskStarted = true;
                    playService.removeObject(taskSave);
                }
            }
        });
        taskSave.addButton(get("taskSave.confirm", saveList[2]), new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    saveTotal = saveList[2];
                    taskStarted = true;
                    playService.removeObject(taskSave);
                }
            }
        });
        playService.addObject(taskSave, true);
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
        actorNetwork.talk(talk5);
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
        Actor e0 = enemies.get(0);
        Actor e1 = enemies.get(1);
        Actor e2 = enemies.get(2);
        
        actorNetwork.setTarget(e0, gb);
        
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
        return ResourceManager.getOther("resource_gb", rid, param);
    }
    
    // 设置角色为保护状态或非保护状态
    private void setProtected(Actor actor, boolean bool) {
        if (bool) {
            stateNetwork.addState(actor, IdConstants.STATE_SAFE, null);
        } else {
            stateNetwork.removeState(actor, IdConstants.STATE_SAFE);
        }
    }
    
    private void setProtected(List<Actor> actors, boolean bool) {
        for (Actor a : actors) {
            setProtected(a, bool);
        }
    }
    
    // 将actor的目标设置为enemy,如果each为true,则同时将enemy的目标设置为actor
    private void setTarget(Actor actor, Actor target, boolean each) {
        actorNetwork.setTarget(actor, target);
        if (each) {
            actorNetwork.setTarget(target, actor);
        }
    }
    
    // 检测是否所有敌人已死
    private boolean checkAllEnemyDead() {
        for (Actor a : enemies) {
            if (!actorService.isDead(a)) {
                return false;
            }
        }
        return true;
    }
}
