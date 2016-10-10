/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.game;

import com.jme3.math.Vector3f;
import name.huliqing.ly.data.GameData;

/**
 *
 * @author huliqing
 */
public class StoryGbGame extends StoryGame {
    // 古柏的位置
    private Vector3f gbPosition;
    // 盗贼据点
    private Vector3f enemyPosition;
    
    // 敌人角色列表
    public String[] enemyActors;
    
    // ---- inner
    public int groupEnemy = 2;

    @Override
    public void setData(GameData data) {
        super.setData(data); //To change body of generated methods, choose Tools | Templates.
        gbPosition = data.getAsVector3f("gbPosition");
        enemyPosition = data.getAsVector3f("enemyPosition");
        if (gbPosition == null || enemyPosition == null) 
            throw new NullPointerException();
        enemyActors = data.getAsArray("enemyActors");
    }
    
    @Override
    protected void doStoryInitialize() {
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
