/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game;

import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.ResConstants;
import name.huliqing.luoying.data.GameLogicData;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.luoying.layer.network.ActorNetwork;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.LogicService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.layer.service.TalentService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.logic.scene.ActorBuildLogic;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.logic.PositionLogic;
import name.huliqing.luoying.object.gamelogic.AbstractGameLogic;
import name.huliqing.luoying.object.talent.Talent;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.object.view.TextView;
import name.huliqing.ly.object.view.View;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 * @author huliqing
 * @param <T>
 */
public class SurvivalBoss<T extends GameLogicData> extends AbstractGameLogic<T> {
    private final ActorService actorService = Factory.get(ActorService.class);
    private final TalentService talentService = Factory.get(TalentService.class);
    private final LogicService logicService = Factory.get(LogicService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    
    private final SurvivalGame game;
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
        this.game = game;
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
    protected void doLogic(float tpf) {
        
        if (bossAdded && !bossDead) {
            if (actorService.isDead(boss)) {
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
                actorNetwork.kill(a);
            }
        }
    }
    
    private void killAllLogic() {
        game.removeLogic(levelLogic);
        game.removeLogic(actorBuilder);
        game.removeLogic(this);
    }
    
    private Entity loadBoss() {
        Entity locBoss = Loader.load(IdConstants.ACTOR_FAIRY);
        actorService.setLocation(locBoss, actorBuilder.getRandomPosition());
        actorService.setLevel(locBoss, levelLogic.getMaxLevel() + 5);
        actorService.setGroup(locBoss, game.GROUP_ENEMY);
        skillService.playSkill(locBoss, skillService.getSkillWaitDefault(locBoss), false);
        
        // 添加逻辑
        addPositionLogic(locBoss);
                
        // 为BOSS添加特殊天赋
        Talent attack = Loader.load(IdConstants.TALENT_ATTACK);
        Talent defence = Loader.load(IdConstants.TALENT_DEFENCE);
        Talent defenceMagic = Loader.load(IdConstants.TALENT_DEFENCE_MAGIC);
        Talent lifeRestore = Loader.load(IdConstants.TALENT_LIFE_RESTORE);
        Talent moveSpeed = Loader.load(IdConstants.TALENT_MOVE_SPEED);
        attack.setLevel(attack.getMaxLevel());
        defence.setLevel(defence.getMaxLevel());
        defenceMagic.setLevel(defenceMagic.getMaxLevel());
        moveSpeed.setLevel(moveSpeed.getMaxLevel());
        lifeRestore.setMaxLevel(15);
        lifeRestore.setLevel(15);
        
        talentService.addTalent(locBoss, attack);
        talentService.addTalent(locBoss, defence);
        talentService.addTalent(locBoss, defenceMagic);
        talentService.addTalent(locBoss, lifeRestore);
        talentService.addTalent(locBoss, moveSpeed);
        
        // 添加BOSS，并让所有小弟都跟随BOSS
        playNetwork.addEntity(locBoss);
        List<Actor> xd = findNearestFriendly(locBoss, interval);
        if (!xd.isEmpty()) {
            for (Entity a : xd) {
                actorNetwork.setFollow(a, locBoss.getData().getUniqueId());
            }
        }
        
        TextView view = (TextView) Loader.load(IdConstants.VIEW_WARN);
        view.setText("Boss");
        playNetwork.addEntity(view);
        
        return locBoss;
    }
    
    private void addPositionLogic(Entity actor) {
        TempVars tv = TempVars.get();
        tv.vect1.set(game.treasurePos);
        Vector3f terrainHeight = playService.getTerrainHeight(game.getScene(), tv.vect1.x, tv.vect1.z);
        if (terrainHeight != null) {
            tv.vect1.set(terrainHeight);
        }
        PositionLogic runLogic = (PositionLogic) Loader.load(IdConstants.LOGIC_POSITION);
        runLogic.setInterval(3);
        runLogic.setPosition(tv.vect1);
        runLogic.setNearestDistance(game.nearestDistance);
        logicService.addLogic(actor, runLogic);
        tv.release();
    }
    
    // 查找指定角色周围的友军单位
    private List<Actor> findNearestFriendly(Entity actor, float maxDistance) {
        List<Actor> store = new ArrayList<Actor>();
        List<Actor> actors = actor.getScene().getEntities(Actor.class, actor.getSpatial().getWorldTranslation(), maxDistance, store);
        for (Actor a : actors) {
            if (actorService.isDead(a) || actorService.isEnemy(a, actor)) {
                continue;
            }
            if (actorService.getGroup(a) == actorService.getGroup(actor)) {
                store.add(a);
            }
        }
        return store;
    }
}
