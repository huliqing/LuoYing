/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.ly.object.game.story;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.layer.service.SceneService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.logic.scene.ActorBuildSimpleLogic;
import name.huliqing.luoying.logic.scene.ActorBuildSimpleLogic.Callback;
import name.huliqing.luoying.logic.scene.ActorMultLoadHelper;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.game.Game;
import name.huliqing.luoying.object.gamelogic.AbstractGameLogic;
import name.huliqing.luoying.object.skill.Skill;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.layer.service.GameService;

/**
 * 这个任务阶段是角色打祭坛，回收树根的阶段。
 * @author huliqing
 */
public class StoryGbTask2 extends AbstractTaskStep{
//    private final static Logger LOG = Logger.getLogger(StoryGbTask2.class.getName());
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final GameService gameService = Factory.get(GameService.class);
    private final SceneService sceneService = Factory.get(SceneService.class);
    private final EntityService entityService = Factory.get(EntityService.class);
    private final StoryGbGame _game;
    private Entity player;
    private boolean finished;
    
    // 祭坛
    private Entity altar;
    // 防御塔2个
    private final String[] towerIds = new String[] {
        IdConstants.ACTOR_TOWER, 
        IdConstants.ACTOR_TOWER_STONE};
    private final int maxTower = 2;
    private List<Entity> towers;
    
    // 任务面板
    private StoryGbTaskLogic taskPanel;
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
        this._game = game;
    }

    @Override
    protected void doInit(TaskStep previous) {
        player = _game.getPlayer();
        towers = new ArrayList<Entity>(maxTower);
        
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
            public void callback(Entity actor, int loadIndex) {
                if (loadIndex == 0) {
                    altar = actor;
                    gameService.setLevel(altar, altarLevel);
                    gameService.setGroup(altar, _game.groupEnemy);
                    gameService.setLocation(altar, _game.getEnemyPosition());
                    gameService.setOnTerrain(altar);
                    // 保护状态
                    entityService.addObjectData(altar, Loader.loadData(IdConstants.STATE_SAFE), 1);
                    // 意外的收获:星光传送术掉落
                    entityService.addObjectData(altar, Loader.loadData(IdConstants.DROP_BOOK_007), 1);
                }
                if (loadIndex > 0) {
                    gameService.setLevel(actor, towerLevel);
                    gameService.setGroup(actor, _game.groupEnemy);
                    gameService.setColor(actor, enemyColor);
                    gameService.setLocation(actor, getRandomEnemyPosition());
                    gameService.setOnTerrain(actor);
                    towers.add(actor);
                }
                playNetwork.addEntity(actor);
            }
        };
        
        // 3.敌人角色刷新器
        Callback cb = new Callback() {
            @Override
            public void onload(Entity actor) {
                gameService.setLevel(actor, enemyBaseLevel);
                gameService.setGroup(actor, _game.groupEnemy);
                // 如果是古柏幼仔，则让它掉落任务物品：树根
                if (actor.getData().getId().equals(IdConstants.ACTOR_GB_SMALL)) {
                    entityService.addObjectData(actor, Loader.loadData(IdConstants.DROP_TREE_STUMP), 1);
                }
                gameService.setColor(actor, enemyColor);
            }
        };
        actorBuilder = new ActorBuildSimpleLogic();
        actorBuilder.addBuilder(getRandomEnemyPosition(), new String[] {IdConstants.ACTOR_NINJA}, refreshInterval, cb);
        actorBuilder.addBuilder(getRandomEnemyPosition(), new String[] {IdConstants.ACTOR_NINJA}, refreshInterval, cb);
        
        actorBuilder.addBuilder(getRandomEnemyPosition(), _game.getEnemyActors(), refreshInterval, cb);
        actorBuilder.addBuilder(getRandomEnemyPosition(), _game.getEnemyActors(), refreshInterval, cb);
        actorBuilder.addBuilder(getRandomEnemyPosition(), _game.getEnemyActors(), refreshInterval, cb);
        actorBuilder.addBuilder(getRandomEnemyPosition(), _game.getEnemyActors(), refreshInterval, cb);
        
        taskEndLogic = new StoryGbTask2End(_game, player, findGbPosition(), taskPanel);
        
        // 载入jaime
        jaimeLoader = new ActorMultLoadHelper(IdConstants.ACTOR_JAIME) {
            @Override
            public void callback(Entity actor, int loadIndex) {
                gameService.setGroup(actor, -1);
                
//                skillService.playSkill(actor, skillService.getSkillWaitDefault(actor), false);
                Skill waitSkill = skillService.getSkillWaitDefault(actor);
                if (waitSkill != null) {
                    entityService.useObjectData(actor, waitSkill.getData().getUniqueId());
                }
                
                playNetwork.addEntity(actor);
            }
        };
   
        _game.addLogic(gbRemoveChecker); // 用于移除古柏
        _game.addLogic(taskPanel);
        _game.addLogic(taskChecker);
    }

    @Override
    protected void doLogic(float tpf) {
        // 任务完成后开始载入“提交任务阶段的逻辑”
        if (!taskSuccess && taskChecker.ok) {
            _game.removeLogic(gbRemoveChecker);
            _game.addLogic(taskEndLogic);
            taskSuccess = true;
        }
        
        // 0.载入祭坛及防御塔
        if (stage == 0) {
            _game.addLogic(altarLoader);
            _game.addLogic(jaimeLoader);
            stage = 1;
            return;
        }
        
        // 载入角色刷新器
        if (stage == 1) {
            if (altar != null && towers.size() >= maxTower) {
                _game.removeLogic(altarLoader);
                _game.addLogic(actorBuilder);
                _game.addLogic(towerChecker);
                stage = 2;
            }
            return;
        }
        
        // 1.主逻辑
        if (stage == 2) {
            // 如果所有防御塔都已经死亡，则恢复祭坛的防御，让它可被击死
            if (towerChecker.allTowerDead) {
                _game.removeLogic(towerChecker);
                altar.removeObjectData(altar.getData().getObjectData(IdConstants.STATE_SAFE), 1);
                stage = 3;
            }
            return;
        }
        
        if (stage == 3) {
            // 如果祭坛死亡，则角色刷新器将停止工作，不再刷新敌人
            if (gameService.isDead(altar)) {
                _game.removeLogic(actorBuilder);
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
        return ResourceManager.get("storyGb." + rid, param);
    }
    
    /**
     * 在祭坛周围随机获得一个用于刷新敌人的地点。
     * @return 
     */
    private Vector3f getRandomEnemyPosition() {
        Vector3f pos = MathUtils.getRandomPosition(_game.getEnemyPosition(), 4, 10, null);
        Vector3f terrainHeight = sceneService.getSceneHeight(_game.getScene(), pos.x, pos.z);
        if (terrainHeight != null) {
            pos.set(terrainHeight).addLocal(0, 0.2f, 0);
        }
        return pos;
    }
    
    private Actor findActorById(String id) {
        List<Actor> entities = _game.getScene().getEntities(Actor.class, null);
        Actor result = null;
        for (Actor e : entities) {
            if (e.getData().getId().equals(id)) {
                result = e;
                break;
            }
        }
        return result;
    }
    
    // 查找古柏的位置, 由于任务阶段2没有传入古柏的位置，所以需要在场景中查找
    private Vector3f findGbPosition() {
//        Entity gb = playService.findActor(IdConstants.ACTOR_GB);

        Actor gb = findActorById(IdConstants.ACTOR_GB);
        if (gb != null) {
            return gb.getSpatial().getWorldTranslation();
        }
        return new Vector3f(0, 1, 0);
    }
    
    // 用于检测并移除古柏的逻辑,当主角接完任务后古柏就可以离开场景了。
    // 等任务完成之后再重新载入
    private class GbRemoveChecker extends AbstractGameLogic {
        
        private Entity gb;

        @Override
        protected void logicInit(Game game) {}
        
        @Override
        protected void logicUpdate(float tpf) {
            if (gb == null) {
                gb = findActorById(IdConstants.ACTOR_GB);
                if (gb == null) {
                    setEnabled(false);
                    return;
                }
            }
            // 古柏离开场景之后就不再需要检查了,这里通过判断主角与古柏的距离
            // 当远离一定距离的时候就从场景移除古柏
            if (player.getSpatial().getWorldTranslation().distance(gb.getSpatial().getWorldTranslation()) > 50) {
                playNetwork.removeEntity(gb);
                setEnabled(false);
            }
        }
    }

    // 检测所有防御塔是否已经全部死亡
    private class TowerChecker extends AbstractGameLogic {
        private boolean allTowerDead;
        public TowerChecker() {
            interval = 1;
        }

        @Override
        protected void logicInit(Game game) {}
    
        @Override
        protected void logicUpdate(float tpf) {
            for (Entity tower : towers) {
                if (!gameService.isDead(tower)) {
                    allTowerDead = false;
                    return;
                }
            }
            allTowerDead = true;
        }
    }
    
    // 检测任务是否已经全部完成
    private class TaskOkChecker extends AbstractGameLogic {
        private boolean ok;
        private boolean noticed; // 是否已经提示任务完成
        public TaskOkChecker() {
            interval = 3;// 3秒检测一次
        }
        
        @Override
        protected void logicInit(Game game) {}
        
        @Override
        protected void logicUpdate(float tpf) {
            ok = taskPanel.isOk();
            
            // 如果任务完成，则弹出提示,并转到下一阶段
            if (ok && !noticed) {
                gameService.addMessage(getOther("taskSave.saveOk"), MessageType.item);
                noticed = true;
            }
        }
    }

}
