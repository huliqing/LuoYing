/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.anim;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.utils.MathUtils;

/**
 * 简单的动画组,该组允许指定子动画在什么插值点开始执行，在子动画启动后，当
 * 前插值点即不再影响子动画的插值。当子动画全部结束后，当前动画才算完全结束。
 * @author huliqing
 * @param <E>
 */
public final class SimpleGroup<E> extends AbstractAnim<E> {
    private final static Logger LOG = Logger.getLogger(SimpleGroup.class.getName());
    private final List<AnimWrap> anims = new ArrayList<AnimWrap>();
    
    // 记住tpf，以便在update子动画时使用
    private float tpf; 
    
    public SimpleGroup() {
        super();
        this.loop = Loop.loop;
    }

    @Override
    public void setLoop(Loop loop) {
        // 不能使用dontLoop, 因为当前动画要负责更新子动画，所在在子动画未全部结束时
        // 当前动画不能结束。
        LOG.log(Level.WARNING, "SimpleGroup unsupported setLoop, it auto set to Loop or Cycle");
    }
    
    /**
     * 添加一个子动画，并指定子动画在哪一个插值点开始执行。如果一开始就执行，
     * 则设置startAtInterpolation=0
     * @param anim 
     * @param startInterpolation 
     */
    public void addAnimation(Anim anim, float startInterpolation) {
        anims.add(new AnimWrap(anim, startInterpolation));
    }
    
    public void clearAnimations() {
        for (AnimWrap aw : anims) {
            if (!aw.anim.isEnd()) {
                aw.anim.cleanup();
            }
        }
        anims.clear();
    }

    @Override
    public void update(float tpf) {
        this.tpf = tpf;
        super.update(tpf);
    }
    
    @Override
    protected void doAnimInit() {}
    
    @Override
    protected void doAnimUpdate(float interpolation) {
        if (anims.isEmpty()) {
            return;
        }
        for (int i = 0; i < anims.size(); i++) {
            AnimWrap aw = anims.get(i);
            if (aw.checkToStart(interpolation)) {
                aw.anim.update(tpf);
            }
        }
    }
    
    @Override
    public void cleanup() {
        for (int i = 0; i < anims.size(); i++) {
            AnimWrap aw = anims.get(i);
            if (!aw.anim.isEnd()) {
                aw.anim.cleanup();
            }
            aw.started = false;
        }
        super.cleanup();
    }

    @Override
    public boolean isEnd() {
         if (!initialized) {
            return true;
        }
        // 如果有一个子动画还在执行，则返回false
        for (int i = 0; i < anims.size(); i++) {
            if (!anims.get(i).anim.isEnd()) {
                return false;
            }
        }
        return super.isEnd();
    }
    
    private class AnimWrap {
        Anim anim;
        float startInterpolation;
        boolean started = false;
        public AnimWrap(Anim anim, float startInterpolation) {
            this.anim = anim;
            this.startInterpolation = MathUtils.clamp(startInterpolation, 0f, 1f);
        }
        public boolean checkToStart(float parentInterpolation) {
            if (!started && parentInterpolation >= startInterpolation) {
                anim.start();
                // 标记为已经启动，不要使用(!anim.isEnd())，与其无关,不要把是否启动过
                // 与anim运行状态相关联。
                started = true; 
            }
            return started;
        }
    }
    
}
