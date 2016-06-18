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

/**
 *
 * @author huliqing
 */
public class StoryGbGame extends StoryGame {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActionService actionService = Factory.get(ActionService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    
    // 古柏的位置
    private Vector3f gbPosition;
    // 盗贼据点
    private Vector3f enemyPosition;
    
    // 敌人角色列表
    public String[] enemyActors;
    
    // ---- inner
    public int groupEnemy = 2;


    @Override
    public void initData(GameData data) {
        super.initData(data); //To change body of generated methods, choose Tools | Templates.
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
