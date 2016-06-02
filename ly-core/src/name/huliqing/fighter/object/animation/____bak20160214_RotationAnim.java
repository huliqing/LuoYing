///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.animation;
//
//import com.jme3.math.Vector3f;
//import com.jme3.util.TempVars;
//
///**
// * @deprecated use v2 instead
// * @author huliqing
// */
//public class RotationAnim extends AbstractAnimation {
//
//    // 旋转速度
//    private float speed = 1;
//    // 旋转
//    private Vector3f axis = Vector3f.UNIT_Y.clone();
//    // 是否反转方向
//    private boolean reverse;
//    
//    public Vector3f getAxis() {
//        return axis;
//    }
//    
//    /**
//     * 设置旋转轴
//     * @param axis 
//     */
//    public void setAxis(Vector3f axis) {
//       this.axis.set(axis).normalizeLocal();
//    }
//
//    public float getSpeed() {
//        return speed;
//    }
//
//    /**
//     * 设置旋转速度倍率，大小1提高速度，小于1则降低速度
//     * @param speed 
//     */
//    public void setSpeed(float speed) {
//        this.speed = speed;
//    }
//
//    public boolean isReverse() {
//        return reverse;
//    }
//
//    public void setReverse(boolean reverse) {
//        this.reverse = reverse;
//    }
//    
//    @Override
//    protected void doInit() {
//        // nothing
//    }
//
//    @Override
//    protected void doAnimation(float tpf) {
//        TempVars tv = TempVars.get();
//        tv.quat1.fromAngleNormalAxis(tpf * speed * (reverse ? -1 : 1), axis);
//        target.rotate(tv.quat1);
//        tv.release();
//    }
//    
//}
