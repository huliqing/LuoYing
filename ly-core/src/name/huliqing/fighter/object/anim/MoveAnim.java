/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.anim;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.logging.Logger;
import name.huliqing.fighter.data.AnimData;

/**
 * 直线移动的动画
 * @author huliqing
 */
public final class MoveAnim extends SpatialAnim {
//    private final static Logger logger = Logger.getLogger(MoveAnim.class.getName());

    // 开始位置及结束位置
    private Vector3f startPos = new Vector3f();
    private Vector3f endPos = new Vector3f();
    // 是否朝向目标
    private boolean facing;
    // 增加一定的反弹（反向移动）
    private float boundFactor;
    // 使用正弦曲线来增强运动效果,匀速的运动很难看
    private boolean useSine;
    
    public MoveAnim() {
        super();
    }
    
    public MoveAnim(AnimData data) {
        super(data);
        this.startPos = data.getAsVector3f("startPos", startPos);
        this.endPos = data.getAsVector3f("endPos", endPos);
        this.facing = data.getAsBoolean("facing", facing);
        this.boundFactor = data.getAsFloat("boundFactor", boundFactor);
        this.useSine = data.getAsBoolean("useSine", useSine);
    }
    
    @Override
    protected void doInit() {}

    @Override
    protected void doAnimation(float interpolation) {
        if (facing) {
            target.lookAt(startPos, endPos);
        }
        if (useSine) {
            interpolation = FastMath.sin(interpolation * FastMath.HALF_PI);
        }
        
        interpolation += interpolation * boundFactor;
        float sineFactor = FastMath.sin(interpolation * FastMath.HALF_PI);
        
        TempVars tv = TempVars.get();
        FastMath.interpolateLinear(sineFactor, startPos, endPos, tv.vect1);
//        logger.log(Level.INFO, "move to location, start={0}, end={1}, now={2}", new Object[] {startPos, endPos, tv.vect1});
        target.setLocalTranslation(tv.vect1);
        tv.release();
    }

    @Override
    public void cleanup() {
        // remove20160225
//        if (target != null) {
//            target.setLocalTranslation(endPos);
//        }
        super.cleanup(); 
    }

    public Vector3f getStartPos() {
        return startPos;
    }

    public void setStartPos(Vector3f startPos) {
        this.startPos.set(startPos);
    }

    public Vector3f getEndPos() {
        return endPos;
    }

    public void setEndPos(Vector3f endPos) {
        this.endPos.set(endPos);
    }

    public boolean isFacing() {
        return facing;
    }

    public void setFacing(boolean facing) {
        this.facing = facing;
    }

    public float getBoundFactor() {
        return boundFactor;
    }

    public void setBoundFactor(float boundFactor) {
        this.boundFactor = boundFactor;
    }

    public boolean isUseSine() {
        return useSine;
    }

    public void setUseSine(boolean useSine) {
        this.useSine = useSine;
    }
    
    
}
