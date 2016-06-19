/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import com.jme3.math.Vector3f;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.logic.scene.ActorCleanLogic;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.utils.MathUtils;

/**
 * @author huliqing
 */
public class SurvivalGame extends Game {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    
    public Vector3f treasurePos = new Vector3f(0,0,-2f);
    // 友军单位的分组
    public int SELF_GROUP = 1;
    // 角色分组
    public int GROUP_ENEMY = 2;
    // 怪物数量
    public int buildTotal = 8;
    public float nearestDistance = 10;
    // 怪物刷新点
    public Vector3f[] enemyPositions;
    // 每隔多长时间提升敌军等级,单位秒
    public float levelUpBySec = 120;
//    public float levelUpBySec = 15; // for test
    // 最高20级
    public int maxLevel = 15;

    @Override
    public void initialize() {
        super.initialize(); 
        // 生成敌人的刷新地点
        enemyPositions = new Vector3f[buildTotal];
        for (int i = 0; i < buildTotal; i++) {
            do {
                enemyPositions[i] = new Vector3f();
                MathUtils.getRandomPosition(treasurePos, 50, 80, enemyPositions[i]);
            } while (!scene.checkIsEmptyZone(enemyPositions[i].x, enemyPositions[i].z, nearestDistance));
        }
        
        // 角色清理器
        addLogic(new ActorCleanLogic());
        // 主逻辑
        addLogic(new SurvivalLogic(this));
    }

    @Override
    public void onActorSelected(Actor actor) {
        super.onActorSelected(actor);
        // 设置角色分组
        actorNetwork.setGroup(actor, SELF_GROUP);
        actorNetwork.setLevel(actor, 1);
    }

    
    
    
}
