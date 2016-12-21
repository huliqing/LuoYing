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
package name.huliqing.ly.view.talk;

import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public abstract class Speak {
    
    // 该逻辑所需要执行的时间,单位秒
    protected float useTime;
    // 当前已经使用的时间，单位秒
    private float time;
    // 是否开始
    private boolean started = false;
    
    public Speak() {}

    public float getUseTime() {
        return useTime;
    }

    public void setUseTime(float useTime) {
        this.useTime = useTime;
    }
    
    public void start() {
        started = true;
        time = 0;
        doInit();
    }

    public void update(float tpf) {
        if (!started) 
            return;
        
        time += tpf;
        if (time >= useTime) {
            cleanup();
        } else {
            doLogic(tpf);                
        }
    }

    /**
     * 结束当前speak并清理数据
     */
    public void cleanup() {
        started = false;
    }
    
    public boolean isEnd() {
        return !started;
    }
    
    /**
     * 获得说话者
     * @return 
     */
    public abstract Entity getActor();
    
    /**
     * 初始化
     */
    protected abstract void doInit();
    
    /**
     * 处理逻辑
     * @param tpf 
     */
    protected abstract void doLogic(float tpf);


}
