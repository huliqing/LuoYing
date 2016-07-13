/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.anim;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import name.huliqing.fighter.data.AnimData;

/**
 * @param <T>
 * @author huliqing
 */
public final class RotationAnim<T extends AnimData> extends SpatialAnim<T> {
//    private final static Logger logger = Logger.getLogger(RotationAnim.class.getName());
    
    // 旋转
    private Vector3f axis = Vector3f.UNIT_Y.clone();
    // 旋转弧度
    private float angle = FastMath.TWO_PI;
    // 是否反转方向
    private boolean invert;
    
    // 当动画结束后是否复原旋转位置
    private boolean restore;
    private final Quaternion origin = new Quaternion();
    
    public RotationAnim() {
        super();
    }
    
    @Override
    public void initData(T data) {
        super.initData(data);
        this.axis = data.getAsVector3f("axis", axis);
        Float degree = data.getAsFloat("degree");
        if (degree != null) {
            angle = degree * FastMath.DEG_TO_RAD;
        }
        this.invert = data.getAsBoolean("invert", invert);
        this.restore = data.getAsBoolean("restore", restore);
    }

    /**
     * @see #setAxis(com.jme3.math.Vector3f) 
     * @return 
     */
    public Vector3f getAxis() {
        return axis;
    }

    /**
     * 设置旋转轴
     * @param axis 
     */
    public void setAxis(Vector3f axis) {
        this.axis.set(axis);
    }

    /**
     * 获取要旋转的弧度数
     * @return 
     */
    public float getAngle() {
        return angle;
    }

    /**
     * 设置要旋转的<b>弧度</b>数
     * @param angle 
     */
    public void setAngle(float angle) {
        this.angle = angle;
    }
    
    /**
     * 设置要旋转的角度数，与 {@link #setAngle(float) } 一样，但是参数是以
     * <b>角度</b>传入的。
     * @param degree 
     */
    public void setAngleDegree(float degree) {
        this.angle = degree * FastMath.DEG_TO_RAD;
    }

    public boolean isInvert() {
        return invert;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    @Override
    protected void doInit() {
        this.axis.normalizeLocal();
        origin.set(target.getLocalRotation());
    }

    @Override
    protected void doAnimation(float interpolation) {
//        logger.log(Level.INFO, "interpolation={0}", interpolation);
        TempVars tv = TempVars.get();
        tv.quat1.fromAngleNormalAxis(interpolation * angle * (invert ? -1 : 1), axis);
        origin.mult(tv.quat1, tv.quat2);
        target.setLocalRotation(tv.quat2);
        tv.release();
    }

    @Override
    public void cleanup() {
        if (restore && origin != null) {
            target.setLocalRotation(origin);
        }
        super.cleanup();
    }
    
    
}
