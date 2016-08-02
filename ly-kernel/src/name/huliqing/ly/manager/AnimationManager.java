/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.manager;

import com.jme3.app.Application;
import com.jme3.util.SafeArrayList;
import name.huliqing.core.object.AbstractPlayObject;
import name.huliqing.core.object.animation.Animation;

/**
 *
 * @author huliqing
 */
public class AnimationManager extends AbstractPlayObject {
//    private final static Logger logger = Logger.getLogger(AnimationManager.class.getName());
    
    private final static AnimationManager INSTANCE = new AnimationManager();
    private final static SafeArrayList<Animation> ANIMATIONS = new SafeArrayList<Animation>(Animation.class);
    
    private AnimationManager() {}
    
    public static AnimationManager getInstance() {
        return INSTANCE;
    }
    
    @Override
    public void initialize(Application app) {
        super.initialize(app);
    }
    
    public void startAnimation(Animation animation) {
        if (!isInitialized()) 
            return;
        
        if (!ANIMATIONS.contains(animation)) {
            ANIMATIONS.add(animation);
        }
        animation.start();
    }
    
    @Override
    public void update(float tpf) {
        if (ANIMATIONS.isEmpty()) {
            return;
        }
        
        for (Animation anim : ANIMATIONS) {
            if (anim.isEnd()) {
                ANIMATIONS.remove(anim);
            } else {
                anim.update(tpf);
            }
        }
    }
    
    @Override
    public void cleanup() {
        // 不要直接清理，要清理的话，先让列表中所有animation都cleanup（）后再
        // 进行clear(), 否则未执行完的animation都不会isEnd,不能重用，会造成
        // 资源浪费
//        for (Animation a : animations) {
//            a.cleanup();
//        }
//        animations.clear();

        super.cleanup();
    }
}
