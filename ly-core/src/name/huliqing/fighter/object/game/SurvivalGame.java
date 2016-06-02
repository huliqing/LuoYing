/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import com.jme3.math.Vector3f;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.logic.scene.ActorCleanLogic;
import name.huliqing.fighter.object.scene.Scene;
import name.huliqing.fighter.utils.MathUtils;

/**
 * @author huliqing
 */
public class SurvivalGame extends Game {
    private final PlayService playService = Factory.get(PlayService.class);
    
    public Vector3f treasurePos = new Vector3f(0,0,-2f);
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

    public SurvivalGame() {}

    public SurvivalGame(GameData data) {
        super(data);
    }
    
    @Override
    protected void doInit() {
        // 生成敌人的刷新地点
        Scene scene = playService.getScene();
        enemyPositions = new Vector3f[buildTotal];
        for (int i = 0; i < buildTotal; i++) {
            do {
                enemyPositions[i] = new Vector3f();
                MathUtils.getRandomPosition(treasurePos, 50, 80, enemyPositions[i]);
            } while (!scene.checkIsEmptyZone(enemyPositions[i].x, enemyPositions[i].z, nearestDistance));
        }
        
        // task
        addTask(new SurvivalTask(this)); 
        
        // 角色清理器和恢复器
        ActorCleanLogic cleaner = new ActorCleanLogic();
        logics.add(cleaner);
    }
    
}
