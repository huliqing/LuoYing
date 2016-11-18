/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game.lan;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.ActorNetwork;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.ly.object.game.SceneHelper;
import name.huliqing.ly.object.game.SimpleRpgGame;

/**
 * @author huliqing
 */
public class SurvivalGame extends SimpleRpgGame {
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final GameService gameService = Factory.get(GameService.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    
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
    
    private SceneHelper sceneHelper;

    @Override
    public void initialize(Application application) {
        super.initialize(application);
        
        sceneHelper = new SceneHelper(scene);
        
        // 生成敌人的刷新地点
        enemyPositions = new Vector3f[buildTotal];
        for (int i = 0; i < buildTotal; i++) {
            do {
                enemyPositions[i] = new Vector3f();
                MathUtils.getRandomPosition(treasurePos, 50, 80, enemyPositions[i]);
                
            } while (!sceneHelper.isInEmptyZone(enemyPositions[i], nearestDistance, Float.MAX_VALUE, nearestDistance));
        }
        
        // 主逻辑
        addLogic(new SurvivalLogic(this));
    }

    public void onPlayerSelected(Entity actor) {
//        super.onPlayerSelected(actor);
        // 设置角色分组
        gameNetwork.setGroup(actor, SELF_GROUP);
        gameNetwork.setLevel(actor, 1);
    }


    
}
