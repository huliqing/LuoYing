/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 线性运行一系列的动画,每一个动画按顺序依次执行。
 * @author huliqing
 */
public class LinearGroup extends AbstractAnimation {
    private final static Logger logger = Logger.getLogger(LinearGroup.class.getName());

    private List<Animation> anims = new ArrayList<Animation>();
    private int index;
    
    public void addAnimation(Animation animation) {
        if (animation == this) {
            throw new IllegalArgumentException("Could not addAnimation self!");
        }
        if (!anims.contains(animation)) {
            this.anims.add(animation);
        }
    }
    
    public boolean removeAnimation(Animation animation) {
        return anims.remove(animation);
    }
    
    public void clearAnimations() {
        anims.clear();
    }
    
    /**
     * AnimationGroup不支持直接设置AnimateTime,必须为每一个子animation单
     * 独设置时间
     * @param useTime 
     */
    @Override
    public void setAnimateTime(float useTime) {
        // ignore;
        logger.log(Level.WARNING, "LinearGroup unsupported setAnimateTime!"
                + "set their sub animation by self!");
    }
    
    @Override
    public float getAnimateTime() {
        useTime = 0;
        for (Animation anim : anims) {
            useTime += anim.getAnimateTime();
        }
        return useTime;
    }
    
    @Override
    protected void doInit() {
        if (anims.isEmpty()) {
            return;
        }
        if (target != null) {
            for (Animation anim : anims) {
                anim.setTarget(target);
            }
        }
        index = 0;
        useTime = getAnimateTime();
        startAnim(index);
    }

    @Override
    protected void doAnimation(float tpf) {
        if (index >= anims.size()) {
            return;
        }
        Animation anim = anims.get(index);
        if (anim.isEnd()) {
            index++;
            anim = startAnim(index);
        }
        if (anim != null) {
            anim.update(tpf);
        }
    }

    protected Animation startAnim(int idx) {
        if (idx >= anims.size()) {
            return null;
        }
        Animation anim = anims.get(idx);
        anim.start();
        return anim;
    }

    @Override
    protected boolean checkEnd() {
        return index >= anims.size();
    }
    
    @Override
    public void cleanup() {
        for (Animation anim : anims) {
            if (!anim.isEnd()) {
                logger.log(Level.INFO, "Fix sub animation cleanup, anim={0}, time={1}, useTime={2}"
                        , new Object[] {anim, anim.getTime(), anim.getAnimateTime()});
                anim.cleanup();
            }
        }
        
        index = 0;
        super.cleanup();
    }
    
    
}
