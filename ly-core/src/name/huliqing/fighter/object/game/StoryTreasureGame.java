/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import com.jme3.math.Vector3f;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.utils.MathUtils;

/**
 *
 * @author huliqing
 */
public class StoryTreasureGame extends StoryGame {
    
    public Vector3f treasurePos = new Vector3f();
    public int treasureLevel = 5;
    public int groupPlayer = 1;
    public int groupEnemy = 2;
    
    @Override
    protected void doInit() {

        do {
            MathUtils.getRandomPosition(75, 75, new Vector3f(), 50, treasurePos);
        } while (!scene.checkIsEmptyZone(treasurePos.x, treasurePos.z, 10));
        
        addTask(new StoryTreasureTask1(this));
        addTask(new StoryTreasureTask2(this)); 
        
        if (Config.debug) {
            treasurePos = new Vector3f(-14,0.5f,62);
        }
    }

}
