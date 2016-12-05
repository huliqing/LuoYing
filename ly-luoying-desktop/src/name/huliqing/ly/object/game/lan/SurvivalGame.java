/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game.lan;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.ly.object.game.LanServerNetworkRpgGame;
import name.huliqing.ly.object.game.SceneHelper;

/**
 * @author huliqing
 */
public class SurvivalGame extends LanServerNetworkRpgGame {
    private final GameService gameService = Factory.get(GameService.class);
    
    public final Vector3f treasurePos = new Vector3f(0,0,-2f);
    /** 玩家所在友军单位的分组 */
    public final int PLAYER_GROUP = 1;
    /** 玩家所在队伍 */
    public final int PLAYER_TEAM = 1;
    /** 玩家开始等级*/
    public final int PLAYER_LEVEL_INIT = 1;
    
    /** 敌军角色分组 */
    public final int ENEMY_GROUP = 2;
    
    // 怪物数量
    public final int buildTotal = 8;
    public final float nearestDistance = 10;
    // 怪物刷新点
    public Vector3f[] enemyPositions;
    // 每隔多长时间提升敌军等级,单位秒
    public float levelUpBySec = 120;
//    public float levelUpBySec = 15; // for test
    // 最高等级
    public int maxLevel = 15;
    
    private SceneHelper sceneHelper;

    @Override
    public void initialize(Application application) {
        super.initialize(application);
        // 主逻辑
        addLogic(new SurvivalLogic(this));
    }

    @Override
    public void onSceneLoaded(Scene scene) {
        super.onSceneLoaded(scene); 
        sceneHelper = new SceneHelper(scene);
        
        // 生成敌人的刷新地点
        enemyPositions = new Vector3f[buildTotal];
        for (int i = 0; i < buildTotal; i++) {
            do {
                enemyPositions[i] = new Vector3f();
                MathUtils.getRandomPosition(treasurePos, 50, 80, enemyPositions[i]);
                
            } while (!sceneHelper.isInEmptyZone(enemyPositions[i], nearestDistance, Float.MAX_VALUE, nearestDistance));
        }
    }

    @Override
    protected void onAddServerPlayer(Entity actor) {
        gameService.setGroup(actor, PLAYER_GROUP);
        gameService.setTeam(actor, PLAYER_TEAM);
        gameService.setLevel(actor, PLAYER_LEVEL_INIT);
        super.onAddServerPlayer(actor); 
    }
    
    @Override
    protected void onAddClientPlayer(ConnData connData, Entity actor) {
        gameService.setGroup(actor, PLAYER_GROUP);
        gameService.setTeam(actor, PLAYER_TEAM);
        gameService.setLevel(actor, PLAYER_LEVEL_INIT);
        super.onAddClientPlayer(connData, actor); 
    }

    
}
