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
package name.huliqing.ly.animation;

import com.jme3.scene.Spatial;
import java.util.logging.Logger;

/**
 * 基本的UI动画控制v1
 * @deprecated use Anim instead
 * @author huliqing
 */
public abstract class AbstractAnimation implements Animation{
    private final static Logger logger = Logger.getLogger(AbstractAnimation.class.getName());

    // 动画使用的总时间
    protected float useTime = 0.3f;
    // 目标要执行动画的UI
    protected Spatial target;
    
    private boolean started;
    private boolean init;
    // 当前已经使用的时间，单位秒
    protected float time;
    
    @Override
    public void setAnimateTime(float useTime) {
        this.useTime = useTime;
    }

    @Override
    public float getAnimateTime() {
        return this.useTime;
    }

    @Override
    public Spatial getTarget() {
        return this.target;
    }
    
    @Override
    public void setTarget(Spatial target) {
        this.target = target;
    }
    
    @Override
    public void start() {
        this.started = true;
    }

    @Override
    public void update(float tpf) {
        if (!started) {
            return;
        }
        
        if (!init) {
            doInit();
            init = true;
        }
        
        time += tpf;
        doAnimation(tpf);
        
        if (checkEnd()) {
            cleanup();
        }
    }
    
    /**
     * 检查是否动画已经结束
     * @return 
     */
    protected boolean checkEnd() {
        return time >= useTime;
    }
    
    @Override
    public boolean isEnd() {
        return !started;
    }
    
    @Override
    public void cleanup() {
        this.started = false;
        this.init = false;
        this.target = null;
        this.time = 0;
    }
    
    @Override
    public float getTime() {
        return time;
    }
    
    /**
     * 初始化UIAnimation
     */
    protected abstract void doInit();
    
    /**
     * 执行UIAnimation逻辑
     * @param tpf 
     */
    protected abstract void doAnimation(float tpf);

}
