///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.animation;
//
//import com.jme3.effect.Particle;
//import com.jme3.effect.ParticleEmitter;
//import com.jme3.math.ColorRGBA;
//import com.jme3.scene.Spatial;
//import com.jme3.util.TempVars;
//
///**
// * 专用于特效粒子的颜色的渐变动画
// * @author huliqing
// */
//public class ParticleColorAnim extends AbstractAnimation{
//
//    private ParticleEmitter emitter;
//    private ColorRGBA startColor;
//    private ColorRGBA endColor;
//    private float speed = 1;
//    private Loop loop = Loop.cycle;
//    
//    // ---- inner
//    private float factor;
//    private int dir = 1;
//    
//    public ParticleColorAnim() {}
//
//    public ColorRGBA getStartColor() {
//        return startColor;
//    }
//
//    public void setStartColor(ColorRGBA startColor) {
//        this.startColor = startColor;
//    }
//
//    public ColorRGBA getEndColor() {
//        return endColor;
//    }
//
//    public void setEndColor(ColorRGBA endColor) {
//        this.endColor = endColor;
//    }
//
//    public float getSpeed() {
//        return speed;
//    }
//
//    public void setSpeed(float speed) {
//        this.speed = speed;
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
//    @Override
//    public void setTarget(Spatial target) {
//        if (!(target instanceof ParticleEmitter)) {
//            throw new IllegalArgumentException("Only supported ParticleEmitter objects! target=" + target);
//        }
//        super.setTarget(target);
//        this.emitter = (ParticleEmitter) target;
//    }
//    
//    @Override
//    protected void doInit() {
//        if (startColor == null) {
//            startColor = ColorRGBA.DarkGray.clone();
//        }
//        if (endColor == null) {
//            endColor = ColorRGBA.White.clone();
//        }
//        
//        factor = 0;
//        setColor(startColor);
//    }
//
//    @Override
//    protected void doAnimation(float tpf) {
//        factor += tpf * speed * dir;
//        if (factor > 1 || factor < 0) {
//            if (loop == Loop.dontLoop) {
//                cleanup();
//                return;
//            } else if (loop == Loop.loop) {
//                factor = 0;
//            } else if (loop == Loop.cycle) {
//                dir = -dir;
//            }
//        }
//        if (factor > 1) {
//            factor = 1;
//        } else if (factor < 0) {
//            factor = 0;
//        }
//        TempVars tv = TempVars.get();
//        tv.color.set(startColor);
//        tv.color.interpolate(endColor, factor);
//        setColor(tv.color);
//        tv.release();
//    }
//    
//    private void setColor(ColorRGBA color) {
//        Particle[] ps = emitter.getParticles();
//        if (ps != null && ps.length > 0) {
//            for (int i = 0; i < ps.length; i++) {
//                ps[i].color.set(color);
//            }
//        }
//    }
//}
