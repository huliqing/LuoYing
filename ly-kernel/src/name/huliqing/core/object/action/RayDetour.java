/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.action;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import name.huliqing.core.Factory;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.utils.MathUtils;

/**
 * 用射线检测方式避开障碍物
 * @author huliqing
 */
public class RayDetour extends Detour {
    // 遇到障碍物时的调转方向 =》　1:left; -1:right; 0: none;
    private int direction;
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    
    private final Vector3f tempDir = new Vector3f();
    
    public RayDetour() {}
    
    public RayDetour(Action action) {
        super(action);
    }

    @Override
    protected boolean isNeedDetour() {
        if (actorService.hasObstacleActor(actor, playService.findAllActor())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void tryDetour(int count) {
        if (count == 0) {
            direction = MathUtils.randomPON();
        }
        
        float angle = 30 * (count + 1) * FastMath.DEG_TO_RAD * direction;
        tempDir.set(actorService.getWalkDirection(actor)).normalizeLocal();
        MathUtils.rotate(tempDir, angle, Vector3f.UNIT_Y, tempDir);
        detour(tempDir);
        
        float lockTime = count * 0.25f + 0.25f;
        action.lock(lockTime);
    }
    
    
}
