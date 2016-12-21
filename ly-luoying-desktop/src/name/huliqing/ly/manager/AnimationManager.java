/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.ly.manager;

import com.jme3.app.Application;
import com.jme3.util.SafeArrayList;
import name.huliqing.luoying.object.AbstractPlayObject;
import name.huliqing.ly.animation.Animation;

/**
 * @deprecated 准备移除
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
