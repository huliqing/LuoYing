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
package name.huliqing.ly.object.game.lan;

import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.luoying.data.TalentData;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.layer.service.LogicService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.SceneService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.logic.scene.ActorBuildLogic;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.game.Game;
import name.huliqing.luoying.object.logic.PositionLogic;
import name.huliqing.luoying.object.gamelogic.AbstractGameLogic;
import name.huliqing.luoying.object.skill.Skill;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.object.view.TextView;
import name.huliqing.ly.object.view.View;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 * @author huliqing
 */
public class SurvivalBoss extends AbstractGameLogic {
    private final ActorService actorService = Factory.get(ActorService.class);
    private final LogicService logicService = Factory.get(LogicService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    private final GameService gameService = Factory.get(GameService.class);
    private final EntityService entityService = Factory.get(EntityService.class);
    private final SceneService sceneService = Factory.get(SceneService.class);
    
    private final SurvivalGame _game;
    private final SurvivalLevelLogic levelLogic;
    private final ActorBuildLogic actorBuilder;
    
    private final int raptorLevel;
    private boolean raptorAdded;
    
    private final int sinbadLevel;
    private boolean sinbadAdded;
    
    private final int trexLevel;
    private boolean trexAdded;
    
    private final int bossLevel;
    private boolean bossAdded;
    
    private Entity boss;
    private boolean bossDead;
    
    public SurvivalBoss(SurvivalGame game, ActorBuildLogic actorBuilder, SurvivalLevelLogic levelLogic) {
        this.interval = 5;
        this._game = game;
        this.actorBuilder = actorBuilder;
        this.levelLogic = levelLogic;
        int bossLevelUp = levelLogic.getMaxLevel() / 4;
        raptorLevel = bossLevelUp;
        sinbadLevel = bossLevelUp * 2;
        trexLevel = bossLevelUp * 3;
        bossLevel = bossLevelUp * 4;
    }
    
    public boolean isBoosDead() {
        return bossDead;
    }

    @Override
    protected void logicInit(Game game) {}

    @Override
    protected void logicUpdate(float tpf) {
        
        if (bossAdded && !bossDead) {
            if (gameService.isDead(boss)) {
                killAllEnemy();
                killAllLogic();
                View successView = Loader.load(IdConstants.VIEW_TEXT_SUCCESS);
                successView.setUseTime(-1);
                playNetwork.addEntity(successView);
                gameNetwork.addMessage(ResConstants.TASK_SUCCESS, MessageType.item);
                bossDead = true;
            }
        } 
        
        if (levelLogic.getLevel() >= bossLevel) {
            if (!bossAdded) {
                boss = loadBoss();
                bossAdded = true;
            }
            return;
        }
        
        if (levelLogic.getLevel() >= trexLevel) {
            if (!trexAdded) {
                actorBuilder.addId(IdConstants.ACTOR_TREX);
                trexAdded = true;
            }
            return;
        }
        
        if (levelLogic.getLevel() >= sinbadLevel) {
            if (!sinbadAdded) {
                actorBuilder.addId(IdConstants.ACTOR_SINBAD);
                sinbadAdded = true;
            }
            return;
        }
         
        if (levelLogic.getLevel() >= raptorLevel) {
            if (!raptorAdded) {
                actorBuilder.addId(IdConstants.ACTOR_RAPTOR);
                raptorAdded = true;
            }
        }

    }
    
    // 查询出BOSS所有的小弟并杀死,结束游戏
    private void killAllEnemy() {
        List<Actor> enemies = findNearestFriendly(boss, Float.MAX_VALUE);
        if (!enemies.isEmpty()) {
            for (Entity a : enemies) {
                gameNetwork.kill(a);
            }
        }
    }
    
    private void killAllLogic() {
        _game.removeLogic(levelLogic);
        _game.removeLogic(actorBuilder);
        _game.removeLogic(this);
    }
    
    private Entity loadBoss() {
        Entity locBoss = Loader.load(IdConstants.ACTOR_FAIRY);
//        actorService.setLocation(locBoss, actorBuilder.getRandomPosition());
        gameService.setLevel(locBoss, levelLogic.getMaxLevel() + 5);
        gameService.setGroup(locBoss, _game.ENEMY_GROUP);
        gameService.setLocation(locBoss, actorBuilder.getRandomPosition());
        gameService.setOnTerrain(locBoss);
        
//        skillService.playSkill(locBoss, skillService.getSkillWaitDefault(locBoss), false);
        Skill waitSkill = skillService.getSkillWaitDefault(locBoss);
        if (waitSkill != null) {
            entityService.useObjectData(locBoss, waitSkill.getData().getUniqueId());
        }
        
        // 添加逻辑
        addPositionLogic(locBoss);
                
        // 为BOSS添加特殊天赋
        TalentData attack = Loader.loadData(IdConstants.TALENT_ATTACK);
        TalentData defence = Loader.loadData(IdConstants.TALENT_DEFENCE);
        TalentData defenceMagic = Loader.loadData(IdConstants.TALENT_DEFENCE_MAGIC);
        TalentData lifeRestore = Loader.loadData(IdConstants.TALENT_LIFE_RESTORE);
        TalentData moveSpeed = Loader.loadData(IdConstants.TALENT_MOVE_SPEED);
        attack.setLevel(attack.getMaxLevel());
        defence.setLevel(defence.getMaxLevel());
        defenceMagic.setLevel(defenceMagic.getMaxLevel());
        moveSpeed.setLevel(moveSpeed.getMaxLevel());
        lifeRestore.setMaxLevel(15);
        lifeRestore.setLevel(15);
        
        locBoss.addObjectData(attack, 1);
        locBoss.addObjectData(defence, 1);
        locBoss.addObjectData(defenceMagic, 1);
        locBoss.addObjectData(lifeRestore, 1);
        locBoss.addObjectData(moveSpeed, 1);
        
        // 添加BOSS，并让所有小弟都跟随BOSS
        
        playNetwork.addEntity(locBoss);
        List<Actor> xd = findNearestFriendly(locBoss, interval);
        if (!xd.isEmpty()) {
            for (Entity a : xd) {
                gameNetwork.setFollow(a, locBoss.getData().getUniqueId());
            }
        }
        
        TextView view = (TextView) Loader.load(IdConstants.VIEW_WARN);
        view.setText("Boss");
        playNetwork.addEntity(view);
        
        return locBoss;
    }
    
    private void addPositionLogic(Entity actor) {
        TempVars tv = TempVars.get();
        tv.vect1.set(_game.treasurePos);
        Vector3f terrainHeight = sceneService.getSceneHeight(_game.getScene(), tv.vect1.x, tv.vect1.z);
        if (terrainHeight != null) {
            tv.vect1.set(terrainHeight);
        }
        PositionLogic runLogic = (PositionLogic) Loader.load(IdConstants.LOGIC_POSITION);
        runLogic.setPosition(tv.vect1);
        runLogic.setNearestDistance(_game.nearestDistance);
        logicService.addLogic(actor, runLogic);
        tv.release();
    }
    
    // 查找指定角色周围的友军单位
    private List<Actor> findNearestFriendly(Entity actor, float maxDistance) {
        List<Actor> store = new ArrayList<Actor>();
        List<Actor> actors = actor.getScene().getEntities(Actor.class, actor.getSpatial().getWorldTranslation(), maxDistance, null);
        for (Actor a : actors) {
            if (gameService.isDead(a) || gameService.isEnemy(a, actor)) {
                continue;
            }
            if (gameService.getGroup(a) == gameService.getGroup(actor)) {
                store.add(a);
            }
        }
        return store;
    }
}
