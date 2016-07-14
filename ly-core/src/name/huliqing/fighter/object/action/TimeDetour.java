/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.action;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.action.Action;
import name.huliqing.fighter.utils.MathUtils;

/**
 * 通过时间检测来判断及避障
 * @author huliqing
 */
public class TimeDetour extends Detour{
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
    public void setActor(Actor actor) {
        super.setActor(actor);
        lastPos.set(actor.getModel().getWorldTranslation());
    }

    @Override
    protected boolean isNeedDetour() {
//        float lastDistance = actor.getModel().getWorldTranslation().distance(lastPos);
        float distanceSquared = actor.getModel().getWorldTranslation().distanceSquared(lastPos);
        if (distanceSquared <= minDistanceSquared) {
            return true;
        } else {
//            Logger.get(getClass()).log(Level.INFO, "TimeDetour ---- Not need detour ----");
            lastPos.set(actor.getModel().getWorldTranslation());
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
        tv.vect1.set(actor.getWalkDirection());
        MathUtils.rotate(tv.vect1.normalizeLocal(), angle, Vector3f.UNIT_Y, tv.vect2);
        
        // remove20160420,交由父类处理
//        skillNetwork.playRun(actor, null, tv.vect2, autoFacing, false);
        detour(tv.vect2);
        
        tv.release();
        
        float lockTime = count * 0.25f + 0.25f;
        action.lock(lockTime);
    }
    
}
