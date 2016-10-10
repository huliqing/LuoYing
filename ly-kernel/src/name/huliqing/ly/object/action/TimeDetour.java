/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.action;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.service.ActorService;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.utils.MathUtils;

/**
 * 通过时间检测来判断及避障
 * @author huliqing
 */
public class TimeDetour extends Detour{
    private final ActorService actorService = Factory.get(ActorService.class);
    
    // 遇到障碍物时的调转方向 =》　1:left; -1:right; 0: none;
    private int direction;
    
    // 角色的最近一次的位置
    private final Vector3f lastPos = new Vector3f();
    private final float minDistanceSquared = 0.5f * 0.5f;
    
    public TimeDetour() {}
    
    public TimeDetour(Action action) {
        super(action);
    }

    @Override
    public void setActor(Entity actor) {
        super.setActor(actor);
        lastPos.set(actor.getSpatial().getWorldTranslation());
    }

    @Override
    protected boolean isNeedDetour() {
//        float lastDistance = actor.getModel().getWorldTranslation().distance(lastPos);
        float distanceSquared = actor.getSpatial().getWorldTranslation().distanceSquared(lastPos);
        if (distanceSquared <= minDistanceSquared) {
            return true;
        } else {
//            Logger.get(getClass()).log(Level.INFO, "TimeDetour ---- Not need detour ----");
            lastPos.set(actor.getSpatial().getWorldTranslation());
            return false;
        }
    }

    @Override
    protected void tryDetour(int count) {
        if (count == 0) {
            direction = MathUtils.randomPON();
        }
        
        TempVars tv = TempVars.get();
        float angle = 30 * (count + 1) * FastMath.DEG_TO_RAD * direction;
        tv.vect1.set(actorService.getWalkDirection(actor));
        MathUtils.rotate(tv.vect1.normalizeLocal(), angle, Vector3f.UNIT_Y, tv.vect2);
        
        detour(tv.vect2);
        
        tv.release();
        
        float lockTime = count * 0.25f + 0.25f;
        action.lock(lockTime);
    }
    
}
