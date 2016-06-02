///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.animation;
//
//import com.jme3.math.FastMath;
//import com.jme3.math.Vector3f;
//import com.jme3.util.TempVars;
//
///**
// * @deprecated use ScaleAnim instead (v2)
// * 富有弹性的缩放效果
// * @author huliqing
// */
//public class CurveScale extends AbstractAnimation {
//    private Vector3f originScale = new Vector3f();
//    // 是否复原缩放值，在动画结束后
//    private boolean restoreScale;
//    // 缩放值
//    private Vector3f startScale = new Vector3f(1,1,1);
//    private Vector3f endScale = new Vector3f(2,2,2);
//    private float boundFactor = 0.5f;
//    // 动画循环方式
//    private Loop loop = Loop.dontLoop;
//    // 动画速度倍率
//    private float speed = 1.0f;
//    
//    // ---- 内部
//    protected float iFactor; // interpolation factor [0.0, 1.0]
//    private int dir = 1; // add or subtract
//    
//    public CurveScale() {}
//
//    public boolean isRestoreScale() {
//        return restoreScale;
//    }
//
//    /**
//     * 设置是否在动画完成后复原原始缩放比例
//     * @param restoreScale 
//     */
//    public void setRestoreScale(boolean restoreScale) {
//        this.restoreScale = restoreScale;
//    }
//
//    public Vector3f getStartScale() {
//        return startScale;
//    }
//
//    public void setStartScale(Vector3f startScale) {
//        this.startScale.set(startScale);
//    }
//    
//    public void setStartScale(float scale) {
//        this.startScale.set(scale, scale, scale);
//    }
//
//    public Vector3f getEndScale() {
//        return endScale;
//    }
//
//    public void setEndScale(Vector3f endScale) {
//        this.endScale.set(endScale);
//    }
//    
//    public void setEndScale(float scale) {
//        this.endScale.set(scale, scale, scale);
//    }
//
//    public Loop getLoop() {
//        return loop;
//    }
//
//    public void setLoop(Loop loop) {
//        this.loop = loop;
//    }
//
//    public float getSpeed() {
//        return speed;
//    }
//
//    /**
//     * 设置缩放的速度比率，大于1.0的值提高缩放速度
//     * ，小于1.0的值则降低速度，默认1
//     * @param speed 
//     */
//    public void setSpeed(float speed) {
//        this.speed = speed;
//    }
//
//    public float getBoundFactor() {
//        return boundFactor;
//    }
//
//    /**
//     * 设置缩放的弹性效果比例，默认0.5， 值越大，弹性效果越明显，
//     * 设置为0则按默认标准的startScale和endScale进行缩放。
//     * @param boundFactor 
//     */
//    public void setBoundFactor(float boundFactor) {
//        this.boundFactor = boundFactor;
//    }
//    
//    @Override
//    protected void doInit() {
//        iFactor = 0;
//        // 记住原始缩放
//        originScale.set(target.getLocalScale());
//        target.setLocalScale(startScale);
//    }
//
//    @Override
//    protected void doAnimation(float tpf) {
//        iFactor += tpf * speed * dir;
//        
//        float maxFactor =  1 + boundFactor;
//        boolean end = false;
//        if (iFactor > maxFactor) {
//            iFactor = maxFactor;
//            end = true;
//        } else if (iFactor < 0) {
//            iFactor = 0;
//            end = true;
//        }
//        if (end) {
//            if (loop == Loop.dontLoop) {
//                cleanup();
//                return;
//            } else if (loop == Loop.loop) {
//                iFactor = 0;
//            } else if (loop == Loop.cycle) {
//                dir = -dir;
//            }
//        }
//        
//        float sineFactor = FastMath.sin(iFactor * FastMath.HALF_PI);
//        
//        TempVars tv = TempVars.get();
//        Vector3f scale = tv.vect1;
//        FastMath.interpolateLinear(sineFactor, startScale, endScale, scale);
//        target.setLocalScale(scale);
//        tv.release();
//    }
//
//    @Override
//    public void cleanup() {
//        if (restoreScale) {
//            target.setLocalScale(originScale);
//        }
//        super.cleanup();
//    }
//}
