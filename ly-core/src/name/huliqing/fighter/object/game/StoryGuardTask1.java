/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.IdConstants;
import name.huliqing.fighter.enums.Diffculty;
import name.huliqing.fighter.manager.talk.Talk;
import name.huliqing.fighter.manager.talk.TalkImpl;
import name.huliqing.fighter.manager.talk.TalkListener;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.logic.scene.ActorMultLoadHelper;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.anim.Anim;
import name.huliqing.fighter.object.anim.CurveMoveAnim;
import name.huliqing.fighter.object.anim.ScaleAnim;
import name.huliqing.fighter.object.anim.SimpleGroup;
import name.huliqing.fighter.ui.UIUtils;
import name.huliqing.fighter.ui.UIFactory;
import name.huliqing.fighter.ui.TextPanel;
import name.huliqing.fighter.utils.MathUtils;
import name.huliqing.fighter.ui.Button;
import name.huliqing.fighter.ui.UI;
import name.huliqing.fighter.ui.UI.Corner;
import name.huliqing.fighter.ui.UI.Listener;

/**
 * 守护古柏,与古柏交谈
 * @author huliqing
 */
public class StoryGuardTask1 extends GameTaskBase {
    private final PlayService playService = Factory.get(PlayService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private StoryGuardGame game;

    // 开始任务面板
    private TextPanel startPanel;
    private Actor player;
    
    // 古柏的地点
    private Actor gb;
    private boolean finished = false;
    // 载入古柏
    private ActorMultLoadHelper gbLoader;
    // 防御塔载入器
    private ActorMultLoadHelper towerLoader;
    private String[] towerIds = {IdConstants.ACTOR_TOWER, IdConstants.ACTOR_TOWER_STONE};
    private String[] selfTowers = {
        towerIds[FastMath.nextRandomInt(0, towerIds.length - 1)]
    };
    
    // 古柏与player的对话
    private Talk talk;
    private TextPanel taskPanel;
    
    // 难易程序选择按钮
    private Button easy;
    private Button normal;
    private UI crazy;
    // 因为性能问题，UI类暂不支持Control
    private Anim easyAnim;
    private Anim normalAnim;
    private Anim crazyAnim;
    private boolean displayUIAnim;
    
    // 困难程度
    private Diffculty level;
    
    // ---- 任何阶段
    private int stage;

    public StoryGuardTask1(StoryGuardGame game) {
        this.game = game;
    }
    
    @Override
    protected void doInit(GameTask previous) {
        
        player = playService.getPlayer();
        
        // ----开始任何面板
        createStartPanel();
        
        // ---- 角色载入器
        gbLoader = new ActorMultLoadHelper(IdConstants.ACTOR_GB) {
            @Override
            public void callback(Actor actor, int loadIndex) {
                gb = actor;
                gb.setLocation(game.getSelfPosition());
                actorService.setLevel(gb, 30);
                actorService.setGroup(gb, actorService.getGroup(player));
                playNetwork.addActor(gb);
            }
        };
        
        // ---- 载入防御塔
        towerLoader = new ActorMultLoadHelper(selfTowers) {
            @Override
            public void callback(Actor actor, int loadIndex) {
                actorService.setLevel(actor, 20);
                actor.setLocation(getRandomPosition(game.getSelfPosition()));
                actorService.setGroup(actor, actorService.getGroup(player));
                playNetwork.addActor(actor);
            }
        };
        
        playService.addObject(towerLoader, false);
        playService.addObject(gbLoader, false);
    }

    @Override
    protected void doLogic(float tpf) {
////        test
//        if (stage == 0) {
//            createTaskPanel();
//            stage = 1;
//        }
        
        if (stage == 0) {
            if (gb != null) {
                stage = 1;
            }
            return;
        }
        
        if (stage == 1) {
            if (player.getDistance(gb) < 10) {
                createTalk();
                stage = 2;
            }
            return;
        }
        
        if (displayUIAnim) {
            easyAnim.update(tpf);
            normalAnim.update(tpf);
            crazyAnim.update(tpf);
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
    
    private String get(String rid, Object... param) {
        return ResourceManager.getOther("resource_guard", rid, param);
    }
    
    private void setFinished() {
        finished = true;
    }
    
    private void clearButton() {
        playService.removeObject(easy);
        playService.removeObject(normal);
        playService.removeObject(crazy.getDisplay());
        displayUIAnim = false;
    }
    
    private void endTalk() {
        Talk endTalk = new TalkImpl();
        endTalk.delay(1);
        endTalk.speak(gb, get("talkGb.other"));
        actorNetwork.talk(endTalk);
    }
    
    // 显示难易程度让玩家选择
    private void showDifficulty() {
        float sw = playService.getScreenWidth();
        float sh = playService.getScreenHeight();
        float otWidth = playService.getScreenWidth() / 3;
        float btnWidth = otWidth / 3;
        float btnHeight = sh * 0.2f;
        ColorRGBA btnColor = new ColorRGBA(0, 0.5f, 1, 1);
        // easy level
        easy = new Button(get("task.easy"));
        easy.setBackground(UIFactory.getUIConfig().getBackground(), true);
        easy.setBackgroundColor(btnColor, true);
        easy.setWidth(btnWidth);
        easy.setHeight(btnHeight);
        easy.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    level = Diffculty.easy;
                    setFinished();
                    clearButton();
                    endTalk();
                }
            }
        });
        easyAnim = createUIAnim(easy
                , new Vector3f(sw,0,0)
                , new Vector3f(otWidth * 0.5f, sh * 7 / 8, 0)
                , new Vector3f(otWidth, sh * 0.5f, 0)
                , 1.8f);
        
        // normal level
        normal = new Button(get("task.normal"));
        normal.setBackground(UIFactory.getUIConfig().getBackground(), true);
        normal.setBackgroundColor(btnColor, true);
        normal.setWidth(btnWidth);
        normal.setHeight(btnHeight);
        normal.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    level = Diffculty.normal;
                    setFinished();
                    clearButton();
                    endTalk();
                }
            }
        });
        normalAnim = createUIAnim(normal
                , new Vector3f(sw,0,0)
                , new Vector3f(sw * 0.25f, sh * 7 / 8, 0)
                , new Vector3f((sw - btnWidth) * 0.5f, sh * 0.5f, 0)
                , 1.4f);
        
        // hard level
        crazy = UIUtils.createMultView(btnWidth, btnHeight
                , "Interface/icon/skull.png", UIFactory.getUIConfig().getBackground());
        crazy.setBackgroundColor(btnColor, true);
        crazy.setWidth(btnWidth);
        crazy.setHeight(btnHeight);
        crazy.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    level = Diffculty.hard;
                    setFinished();
                    clearButton();
                    endTalk();
                }
            }
        });
        crazyAnim = createUIAnim(crazy.getDisplay()
                , new Vector3f(sw,0,0)
                , new Vector3f(otWidth, sh * 7 / 8, 0)
                , new Vector3f(sw - otWidth - btnWidth, sh * 0.5f, 0)
                , 1.0f);
        
        playService.addObject(easy, true);
        playService.addObject(normal, true);
        playService.addObject(crazy.getDisplay(), true);
        
        easyAnim.start();
        normalAnim.start();
        crazyAnim.start();
        displayUIAnim = true;
    }
    
    private Anim createUIAnim(Spatial target, Vector3f p1, Vector3f p2, Vector3f p3, float speed) {
        List<Vector3f> points = new ArrayList<Vector3f>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        // 移动
        CurveMoveAnim cma = new CurveMoveAnim();
        cma.setControlPoints(points);
        cma.setSpeed(speed);
        cma.setTarget(target);
        
        ScaleAnim sa = new ScaleAnim();
        sa.setStartScale(0.1f);
        sa.setEndScale(1);
        sa.setSpeed(speed);
        sa.setTarget(target);
        
        SimpleGroup anim = new SimpleGroup();
        anim.addAnimation(cma, 0);
        anim.addAnimation(sa, 0);
        
        return anim;
    }
    
    // 显示任务面板，让player接受任务
    private void createTaskPanel() {
        taskPanel = new TextPanel(get("taskGuard.title"), playService.getScreenWidth() * 0.75f, playService.getScreenHeight() * 0f);
        taskPanel.setCloseable(false);
        taskPanel.setToCorner(Corner.CC);
        taskPanel.setDragEnabled(true);
        taskPanel.addText(get("taskGuard.content"));
        taskPanel.addText(get("taskGuard.content1"));
        taskPanel.addText(get("taskGuard.content2"));
        Button acceptBtn = new Button(get("taskGuard.confirm"));
        acceptBtn.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    showDifficulty();
                    playService.removeObject(taskPanel);
                }
            }
        });
        taskPanel.addButton(acceptBtn);
        playService.addObject(taskPanel, true);
    }
    
    private void createTalk() {
        talk = new TalkImpl();
        talk.face(gb, player, false);
        talk.speak(gb, get("talk1.gb1"));
        talk.speak(gb, get("talk1.gb2"));
        talk.speak(gb, get("talk1.gb3"));
        talk.speak(gb, get("talk1.gb4"));
        talk.speak(gb, get("talk1.gb5"));
        talk.face(player, gb, false);
        talk.speak(player, get("talk1.player1"));
        talk.face(gb, player, false);
        talk.speak(gb, get("talk1.gb6"));
        talk.speak(gb, get("talk1.gb7"));
        talk.face(player, gb, false);
        talk.speak(player, get("talk1.player2"));
        talk.delay(1);
        talk.addListener(new TalkListener() {
            @Override
            public void onTalkEnd() {
                createTaskPanel();
            }
        });
        actorNetwork.talk(talk);
    }
    
    private void createStartPanel() {
        startPanel = new TextPanel(get("taskStart.title"), playService.getScreenWidth() * 0.75f, playService.getScreenHeight() * 0f);
        startPanel.addText(get("taskStart.content"));
        startPanel.setCloseable(false);
        startPanel.setToCorner(Corner.CC);
        startPanel.setDragEnabled(true);
        Button startBtn = new Button(get("taskStart.confirm"));
        startBtn.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    playService.removeObject(startPanel);
                }
            }
        });
        startPanel.addButton(startBtn);
        playService.addObject(startPanel, true);
    }

    public Diffculty getLevel() {
        return level;
    }

    public Actor getGb() {
        return gb;
    }
    
    private Vector3f getRandomPosition(Vector3f center) {
        Vector3f pos = MathUtils.getRandomPosition(center, 7, 12, null);
        pos.setY(playService.getTerrainHeight(pos.x, pos.z) + 0.2f);
        return pos;
    }
}
