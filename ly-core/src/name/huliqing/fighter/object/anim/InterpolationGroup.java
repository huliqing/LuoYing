/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.anim;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.data.AnimData;

/**
 * 动画组,使用插值方式，当前动画的插值位置将完全影响子动画的插值位置。
 * 并且子动画的loopMode和speed设置将被无视
 * @author huliqing
 */
public final class InterpolationGroup extends AbstractAnim {
//    private final static Logger logger = Logger.getLogger(InterpolationGroup.class.getName());
    
    private List<AnimWrap> anims = new ArrayList<AnimWrap>();
    
    public InterpolationGroup() {
        super();
    }
    
    public InterpolationGroup(AnimData data) {
        super(data);
    }
    
    public void addAnimation(Anim anim, float startInterpolation, float endInterpolation) {
        if (anim == null) {
            throw new NullPointerException("Anim could not be null!");
        }
        checkValid(startInterpolation);
        checkValid(endInterpolation);
        if (endInterpolation < startInterpolation) {
            throw new IllegalArgumentException("endInterpolation could not less than startInterpolation");
        }
        anims.add(new AnimWrap(anim, startInterpolation, endInterpolation));
    }
    
    public void clearAnimations() {
        for (AnimWrap aw : anims) {
            if (!aw.anim.isEnd()) {
                aw.anim.cleanup();
            }
        }
        anims.clear();
    }

    /**
     * 设置插值位置，该设置将影响并更新子动画的插值
     * @param interpolation 
     */
    @Override
    public void setInterpolation(float interpolation) {
        super.setInterpolation(interpolation);
        updateChildren(interpolation);
    }
    
    @Override
    protected void doInit() {
        for (int i = 0; i < anims.size(); i++) {
            anims.get(i).anim.start();
        }
    }

    @Override
    protected void doAnimation(float interpolation) {
        updateChildren(interpolation);
    }
    
    protected void updateChildren(float interpolation) {
        if (anims.isEmpty()) {
            return;
        }
        for (int i = 0; i < anims.size(); i++) {
            AnimWrap aw = anims.get(i);
            float inter = aw.calulateInterpolation(interpolation);
            // < 0表示不在区间内，则不更新
            if (inter < 0) {
                continue;
            }
            aw.anim.display(inter);
        }
    }

    @Override
    public void cleanup() {
        for (AnimWrap aw : anims) {
            if (!aw.anim.isEnd()) {
                aw.anim.cleanup();
            }
        }
        super.cleanup();
    }

    @Override
    public boolean isEnd() {
        if (!started) {
            return true;
        }
        // 如果有一个子动画还在执行，则返回false
        for (int i = 0; i < anims.size(); i++) {
            if (!anims.get(i).anim.isEnd()) {
                return false;
            }
        }
        return true;
    }
    
    private class AnimWrap {
        // 动画
        Anim anim;
        // 动画开始执行的插值点,[0,1] 
        float startInterpolation = 0.0f;
        // 动画结束的插值点：[0,1]
        // 注：endInterpolation 不能小于 startInterpolation
        float endInterpolation = 1.0f;
        // 是否已经启动
        boolean started;
        
        public AnimWrap(Anim anim, float startInterpolation, float endInterpolation) {
            this.anim = anim;
            this.startInterpolation = startInterpolation;
            this.endInterpolation = endInterpolation;
        }
        
        /**
         * 将父GROUP的插值位置转换为自身的插值位置，如果parentInterpolation
         * 不在插值位置之内，则返回-1
         * @param parentInterpolation
         * @return 
         */
        public float calulateInterpolation(float parentInterpolation) {
            if (parentInterpolation < startInterpolation 
                    || parentInterpolation > endInterpolation) {
                return -1;
            }
            return (parentInterpolation - startInterpolation) / (endInterpolation - startInterpolation);
        }
    }
}
