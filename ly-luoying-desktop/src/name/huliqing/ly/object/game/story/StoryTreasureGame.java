/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game.story;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.Config;
import name.huliqing.luoying.utils.MathUtils;
import name.huliqing.ly.object.game.SceneHelper;
import name.huliqing.ly.object.game.StoryServerNetworkRpgGame;

/**
 *
 * @author huliqing
 */
public class StoryTreasureGame extends StoryServerNetworkRpgGame {
    
    public Vector3f treasurePos = new Vector3f(0, 0, 0);
    public int treasureLevel = 5;
    public int groupPlayer = 1;
    public int groupEnemy = 2;
    
    private SceneHelper sceneHelper;
     
    @Override
    protected void doStoryInitialize() {
        
        sceneHelper = new SceneHelper(scene);
        
        do {
            MathUtils.getRandomPosition(75, 75, new Vector3f(), 50, treasurePos);
        } while (!sceneHelper.isInEmptyZone(treasurePos, 5, Float.MAX_VALUE, 5));
        
        addTask(new StoryTreasureTask1(this));
        addTask(new StoryTreasureTask2(this)); 
        
        if (Config.debug) {
            treasurePos = new Vector3f(-14,0.5f,62);
        }
    }

}
