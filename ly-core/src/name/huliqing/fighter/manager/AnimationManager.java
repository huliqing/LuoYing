/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.manager;

import com.jme3.util.SafeArrayList;
import java.util.logging.Logger;
import name.huliqing.fighter.object.animation.Animation;

/**
 *
 * @author huliqing
 */
public class AnimationManager {
//    private final static Logger logger = Logger.getLogger(AnimationManager.class.getName());
    private final static SafeArrayList<Animation> animations = new SafeArrayList<Animation>(Animation.class);
    
    public static void init() {
        // ignore
    }
    
    public static void startAnimation(Animation animation) {
        if (!animations.contains(animation)) {
            animations.add(animation);
        }
        animation.start();
    }
    
    public static void update(float tpf) {
        if (animations.isEmpty()) {
            return;
        }
        
        for (Animation anim : animations) {
            if (anim.isEnd()) {
                animations.remove(anim);
            } else {
                anim.update(tpf);
            }
        }
    }
    
    public static void cleanup() {
        // 不要直接清理，要清理的话，先让列表中所有animation都cleanup（）后再
        // 进行clear(), 否则未执行完的animation都不会isEnd,不能重用，会造成
        // 资源浪费
//        for (Animation a : animations) {
//            a.cleanup();
//        }
//        animations.clear();
    }
}
