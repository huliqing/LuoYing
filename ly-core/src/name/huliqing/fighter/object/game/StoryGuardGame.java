/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import com.jme3.math.Vector3f;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.logic.scene.ActorCleanLogic;
import name.huliqing.fighter.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public class StoryGuardGame extends Game {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private PlayerDeadChecker playerChecker;
    
    public final static int GROUP_PLAYER = 1;
    /** 敌军分组 */
    public final static int GROUP_ENEMY = 2;    // 敌军
    /** 妖精分组 */
    public final static int GROUP_FAIRY = 3;    // 妖精
    
    // 古柏的位置
    private Vector3f selfPosition;
    // 盗贼据点
    private Vector3f enemyPosition;
    // 妖精位置
    private Vector3f fairyPosition;
    
    public int fairyLevel = 25;
    public int servantLevel = 20;
    
    public StoryGuardGame() {
        super();
    }

    public StoryGuardGame(GameData data) {
        super(data);
        selfPosition = data.getAsVector3f("selfPosition");
        enemyPosition = data.getAsVector3f("enemyPosition");
        fairyPosition = data.getAsVector3f("fairyPosition");
        fairyLevel = data.getAsInteger("fairyLevel", fairyLevel);
        servantLevel = data.getAsInteger("servantLevel", servantLevel);
        
        if (selfPosition == null || enemyPosition == null || fairyPosition == null) 
            throw new NullPointerException();
    }

    @Override
    protected void doInit() {
        
        addTask(new StoryGuardTask1(this));
        addTask(new StoryGuardTask2(this));
        
        Actor player = playService.getPlayer();
        actorNetwork.setGroup(player, GROUP_PLAYER);
        
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

    public Vector3f getSelfPosition() {
        return selfPosition;
    }

    public Vector3f getEnemyPosition() {
        return enemyPosition;
    }
    
    public Vector3f getFairyPosition() {
        return fairyPosition;
    }

    public int getFairyLevel() {
        return fairyLevel;
    }

    public int getServantLevel() {
        return servantLevel;
    }

}
