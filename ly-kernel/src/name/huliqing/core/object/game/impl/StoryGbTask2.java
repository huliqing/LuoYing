/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.game.impl;

import com.jme3.app.Application;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.data.DropData;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.mvc.network.ActorNetwork;
import name.huliqing.core.mvc.network.PlayNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.DropService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.mvc.service.StateService;
import name.huliqing.core.mvc.service.ViewService;
import name.huliqing.core.object.IntervalLogic;
import name.huliqing.core.logic.scene.ActorBuildSimpleLogic;
import name.huliqing.core.logic.scene.ActorBuildSimpleLogic.Callback;
import name.huliqing.core.logic.scene.ActorMultLoadHelper;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.utils.MathUtils;

/**
 * 这个任务阶段是角色打祭坛，回收树根的阶段。
 * @author huliqing
 */
public class StoryGbTask2 extends GameTaskBase{
    private final static Logger LOG = Logger.getLogger(StoryGbTask2.class.getName());
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final DropService dropService = Factory.get(DropService.class);
    private final ViewService viewService = Factory.get(ViewService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final StoryGbGame game;
    private Actor player;
    private boolean finished;
    
    // 任务面板
    private StoryGbTaskLogic taskPanel;
    // 祭坛
    private Actor altar;
    // 防御塔2个
    private final String[] towerIds = new String[] {
        IdConstants.ACTOR_TOWER, 
        IdConstants.ACTOR_TOWER_STONE};
    private final int maxTower = 2;
    private List<Actor> towers;
    
    // 用于检测所有防御塔是否已死
    private final TowerChecker towerChecker = new TowerChecker();
    // 检测任务是否已经完成
    private final TaskOkChecker taskChecker = new TaskOkChecker();
    // 用于移除古柏
    private final GbRemoveChecker gbRemoveChecker = new GbRemoveChecker();
    
    // 级别限制，祭坛和防御塔的级别固定
    private final int altarLevel = 18;
    private final int towerLevel = 15;
    // 普通角色的起始级别
    private final int enemyBaseLevel = 10;
    private final ColorRGBA enemyColor = new ColorRGBA(1.2f, 1f, 1.2f,1);
    
    // 祭坛载入器
    private ActorMultLoadHelper altarLoader;
    // 敌方角色载入器
    private ActorBuildSimpleLogic actorBuilder;
    // 敌方角色刷新间隔,单位秒
    private final float refreshInterval = 5;
    
    // 任务完成后的“向古柏交任务”阶段
    private StoryGbTask2End taskEndLogic;
    private boolean taskSuccess;
    
    // 小猴子
    private ActorMultLoadHelper jaimeLoader;
    
    // ---- 内部参数
    // 当前任务阶段
    private int stage;
    
    public StoryGbTask2(StoryGbGame game) {
        this.game = game;
    }

    @Override
    protected void doInit(GameTask previous) {
        player = playService.getPlayer();
        towers = new ArrayList<Actor>(maxTower);
        
        // 1.任务面板
        if (previous != null) {
            taskPanel = ((StoryGbTask1) previous).getTaskPanel();
        } else {
            // Just for test
            taskPanel = new StoryGbTaskLogic(1, player);
        }
        
        // 2.祭坛和防御塔载入器
        String[] ids = new String[maxTower + 1];
        ids[0] = IdConstants.ACTOR_ALTAR;
        for (int i = 0; i < maxTower; i++) {
            ids[i + 1] = towerIds[FastMath.nextRandomInt(0, towerIds.length - 1)];
        }
        altarLoader = new ActorMultLoadHelper(ids) {
            @Override
            public void callback(Actor actor, int loadIndex) {
                if (loadIndex == 0) {
                    altar = actor;
                    actorService.setLevel(altar, altarLevel);
                    actorService.setGroup(altar, game.groupEnemy);
                    stateService.addStateForce(altar, IdConstants.STATE_SAFE, 0, null);
                    
                    // 意外的收获
                    DropData dropData = altar.getData().getDrop();
                    if (dropData == null) {
                        dropData = dropService.createDrop(IdConstants.DROP_EMPTY);
                        altar.getData().setDrop(dropData);
                    }
                    dropData.setBaseDrop(IdConstants.ITEM_BOOK_007);
                    altar.setLocation(game.getEnemyPosition());
                }
                if (loadIndex > 0) {
                    actorService.setLevel(actor, towerLevel);
                    actorService.setGroup(actor, game.groupEnemy);
                    actor.setLocation(getRandomEnemyPosition());
                    actorService.setColor(actor, enemyColor);
                    towers.add(actor);
                }
                playNetwork.addActor(actor);
//                logger.log(Level.INFO, "111ActorMultLoadHelper load ok, actor={0}", actor.getData().getDef().getId());
            }
        };
        
        // 3.敌人角色刷新器
        Callback cb = new Callback() {
            @Override
            public void onload(Actor actor) {
                actorService.setLevel(actor, enemyBaseLevel);
                actorService.setGroup(actor, game.groupEnemy);
                // 如果是古柏幼仔，则设置它的必然掉落物品
                if (actor.getData().getId().equals(IdConstants.ACTOR_GB_SMALL)) {
                    DropData dropData = actor.getData().getDrop();
                    if (dropData == null) {
                        dropData = dropService.createDrop(IdConstants.DROP_EMPTY);
                        actor.getData().setDrop(dropData);
                    }
                    dropData.setBaseDrop(IdConstants.ITEM_GB_STUMP);
                }
                actorService.setColor(actor, enemyColor);
            }
        };
        actorBuilder = new ActorBuildSimpleLogic();
        actorBuilder.addBuilder(getRandomEnemyPosition(), new String[] {IdConstants.ACTOR_NINJA}, refreshInterval, cb);
        actorBuilder.addBuilder(getRandomEnemyPosition(), new String[] {IdConstants.ACTOR_NINJA}, refreshInterval, cb);
        
        actorBuilder.addBuilder(getRandomEnemyPosition(), game.getEnemyActors(), refreshInterval, cb);
        actorBuilder.addBuilder(getRandomEnemyPosition(), game.getEnemyActors(), refreshInterval, cb);
        actorBuilder.addBuilder(getRandomEnemyPosition(), game.getEnemyActors(), refreshInterval, cb);
        actorBuilder.addBuilder(getRandomEnemyPosition(), game.getEnemyActors(), refreshInterval, cb);
        
        taskEndLogic = new StoryGbTask2End(game, player, findGbPosition(), taskPanel);
        
        // 载入jaime
        jaimeLoader = new ActorMultLoadHelper(IdConstants.ACTOR_JAIME) {
            @Override
            public void callback(Actor actor, int loadIndex) {
                actorService.setGroup(actor, -1);
                skillService.playSkill(actor, skillService.getSkill(actor, SkillType.wait).getId(), false);
                playNetwork.addActor(actor);
            }
        };
        
        playService.addObject(gbRemoveChecker); // 用于移除古柏
        playService.addObject(taskPanel);
        playService.addObject(taskChecker);
    }

    @Override
    protected void doLogic(float tpf) {
        // 任务完成后开始载入“提交任务阶段的逻辑”
        if (!taskSuccess && taskChecker.ok) {
            playService.removeObject(gbRemoveChecker);
            playService.addObject(taskEndLogic);
            taskSuccess = true;
        }
        
        // 0.载入祭坛及防御塔
        if (stage == 0) {
            playService.addObject(altarLoader);
            playService.addObject(jaimeLoader);
            stage = 1;
            return;
        }
        
        // 载入角色刷新器
        if (stage == 1) {
            if (altar != null && towers.size() >= maxTower) {
                playService.removeObject(altarLoader);
                playService.addObject(actorBuilder);
                playService.addObject(towerChecker);
                stage = 2;
            }
            return;
        }
        
        // 1.主逻辑
        if (stage == 2) {
            // 如果所有防御塔都已经死亡，则恢复祭坛的防御，让它可被击死
            if (towerChecker.allTowerDead) {
                playService.removeObject(towerChecker);
                stateService.removeState(altar, IdConstants.STATE_SAFE);
                stage = 3;
            }
            return;
        }
        
        if (stage == 3) {
            // 如果祭坛死亡，则角色刷新器将停止工作，不再刷新敌人
            if (altar.isDead()) {
                playService.removeObject(actorBuilder);
                stage = 4;
            }
            return;
        }
        
    }
   
    // =========================================================================
    
    @Override
    public boolean isFinished() {
        return finished;
    }
    
    // 获取资源文件中的信息
    private String getOther(String rid, Object... param) {
        return ResourceManager.getOther("resource_gb", rid, param);
    }
    
    /**
     * 在祭坛周围随机获得一个用于刷新敌人的地点。
     * @return 
     */
    private Vector3f getRandomEnemyPosition() {
        Vector3f pos = MathUtils.getRandomPosition(game.getEnemyPosition(), 4, 10, null);
        pos.setY(playService.getTerrainHeight(pos.x, pos.z) + 0.2f);
        return pos;
    }
    
    // 用于检测并移除古柏的逻辑,当主角接完任务后古柏就可以离开场景了。
    // 等任务完成之后再重新载入
    private class GbRemoveChecker extends IntervalLogic {
        private boolean end;

        @Override
        public void initialize(Application app) {
            super.initialize(app); 
            end = false;
        }
        
        @Override
        protected void doLogic(float tpf) {
            if (end) {
                return;
            }
            Actor gb = playService.findActor(IdConstants.ACTOR_GB);
            if (gb != null) {
                // 古柏离开场景之后就不再需要检查了,这里通过判断主角与古柏的距离
                // 当远离一定距离的时候就从场景移除古柏
                if (player.getDistance(gb) > 50) {
                    playNetwork.removeObject(gb.getModel());
                    end = true;
                }
            }
        }
    }

    // 检测所有防御塔是否已经全部死亡
    private class TowerChecker extends IntervalLogic {
        private boolean allTowerDead;
        @Override
        protected void doLogic(float tpf) {
            for (Actor tower : towers) {
                if (!tower.isDead()) {
                    allTowerDead = false;
                    return;
                }
            }
            allTowerDead = true;
        }
    }
    
    // 检测任务是否已经全部完成
    private class TaskOkChecker extends IntervalLogic {
        private boolean ok;
        private boolean noticed; // 是否已经提示任务完成
        public TaskOkChecker() {
            super(3); // 3秒检测一次
        }
        @Override
        protected void doLogic(float tpf) {
            ok = taskPanel.isOk();
            
            // 如果任务完成，则弹出提示,并转到下一阶段
            if (ok && !noticed) {
                playService.addMessage(getOther("taskSave.saveOk"), MessageType.item);
                noticed = true;
            }
        }
    }
    
    // 查找古柏的位置, 由于任务阶段2没有传入古柏的位置，所以需要在场景中查找
    private Vector3f findGbPosition() {
        Actor gb = playService.findActor(IdConstants.ACTOR_GB);
        if (gb != null) {
            return gb.getModel().getWorldTranslation();
        }
        return new Vector3f(0, 1, 0);
    }
}
