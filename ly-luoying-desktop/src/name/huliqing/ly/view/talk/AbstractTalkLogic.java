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

/**
 *
 * @author huliqing
 */
public abstract class AbstractTalkLogic implements TalkLogic{
    
    // 该逻辑所需要执行的时间,单位秒
    protected float useTime;
    // 当前已经使用的时间，单位秒
    protected float time;
    
    protected boolean started = false;
    
    protected boolean network = false;
    
    public AbstractTalkLogic() {}

    @Override
    public void start() {
        if (started)
            return;
        
        started = true;
        time = 0;
        
        doInit();
    }

    @Override
    public void update(float tpf) {
        if (!started) 
            return;
        
        time += tpf;
        if (time >= useTime) {
            cleanup();
        } else {
            doTalkLogic(tpf);                
        }
    }

    @Override
    public boolean isEnd() {
        return !started;
    }

    @Override
    public void cleanup() {
        started = false;
    }

    public float getUseTime() {
        return useTime;
    }

    public void setUseTime(float useTime) {
        this.useTime = useTime;
    }

    @Override
    public void setNetwork(boolean network) {
        this.network = network;
    }
    
    /**
     * 初始化
     */
    protected abstract void doInit();
    
    /**
     * 处理谈话逻辑
     * @param tpf 
     */
    protected abstract void doTalkLogic(float tpf);

}
