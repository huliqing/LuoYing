/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import com.jme3.math.Vector3f;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.object.scene.Scene;
import name.huliqing.fighter.utils.MathUtils;

/**
 *
 * @author huliqing
 */
public class StoryTreasureGame extends StoryGame {
    private final PlayService playService = Factory.get(PlayService.class);
    // 检查主角是否死亡
    private PlayerDeadChecker playerChecker;
    
    public Vector3f treasurePos = new Vector3f();
    public int treasureLevel = 5;
    public int groupPlayer = 1;
    public int groupEnemy = 2;

    
    @Override
    protected void doInit() {
        // 随机生成宝箱位置,这里需要防止生成位置与树木冲突
        Scene scene = playService.getScene();
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
