/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import com.jme3.math.Vector3f;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.service.ActionService;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.logic.scene.ActorCleanLogic;
import name.huliqing.fighter.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public class StoryGbGame extends Game {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActionService actionService = Factory.get(ActionService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    
    // 检查主角是否死亡
    private PlayerDeadChecker playerChecker;
    
    // 古柏的位置
    private Vector3f gbPosition;
    // 盗贼据点
    private Vector3f enemyPosition;
    
    // 敌人角色列表
    public String[] enemyActors;
    
    public int groupPlayer = 1;
    public int groupEnemy = 2;
    
    public StoryGbGame() {
        super();
    }

    public StoryGbGame(GameData data) {
        super(data);
        gbPosition = data.getAsVector3f("gbPosition");
        enemyPosition = data.getAsVector3f("enemyPosition");
        if (gbPosition == null || enemyPosition == null) 
            throw new NullPointerException();
        enemyActors = data.getAsArray("enemyActors");
    }

    @Override
    protected void doInit() {
        
        addTask(new StoryGbTask1(this));
        addTask(new StoryGbTask2(this));
        
        // 设置player分组
        Actor player = playService.getPlayer();
        actorNetwork.setGroup(player, groupPlayer);
        
        // 角色清理器和恢复器
        ActorCleanLogic cleaner = new ActorCleanLogic();
        logics.add(cleaner);
        
        playerChecker = new PlayerDeadChecker(player);
        playService.addObject(playerChecker, false);
    }

    @Override
    protected void doLogic(float tpf) {
        // 如果主角已经死亡，则不再处理逻辑
        if (playerChecker.isDead()) {
            return;
        }
        
        super.doLogic(tpf); 
    }
    
    public Vector3f getGbPosition() {
        return gbPosition;
    }

    public Vector3f getEnemyPosition() {
        return enemyPosition;
    }

    public String[] getEnemyActors() {
        return enemyActors;
    }
    
}
