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
package name.huliqing.luoying.object.action;

import name.huliqing.luoying.data.ActionData;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 行为逻辑基础
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractAction<T extends ActionData> implements Action<T> {
    
    private boolean initialized;
    
    protected T data;
    protected Entity actor;
    // 行为是否正在执行
    protected boolean started;
    
    // 行为的锁定。停止行为的更新一定时间
    private boolean locked;
    private float lockTime;
    private float lockTimeUsed;
    
    public AbstractAction() {}

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
    }
    
    /**
     * 启动行为，以下的任一情况会重启调用该方法。
     * 1.行为开始执行时或结束后重新执行时。
     * 2.从另一个行为切换到当前行为时
     */
    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("Action already initialized! action=" + this);
        }
        initialized = true;
        started = true;
        locked = false;
        lockTime = 0;
        lockTimeUsed = 0;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public final void update(float tpf) {
        if (!started) {
            return;
        }
        
        if (locked) {
            lockTimeUsed += tpf;
            if (lockTimeUsed >= lockTime) {
                locked = false;
                lockTimeUsed = 0;
            }
        } else {
            doLogic(tpf);
        }
    }
    
    /**
     * 行为执行后的清理操作。该方法在行为正常终止、退出、被打断、切换等都会执行。
     */
    @Override
    public void cleanup() {
        initialized = false;
        started = false;
    }
    
    /**
     * 判断行为是否完成,或不在执行。
     * @return 
     */
    @Override
    public final boolean isEnd() {
        return !started;
    }
    
    /**
     * 暂停当前行为一定时间
     * @param lockTime 单位秒
     */
    @Override
    public final void lock(float lockTime) {
        this.locked = true;
        this.lockTime = lockTime;
        this.lockTimeUsed = 0;
    }
    
    /**
     * 设置执行行为的角色
     * @param actor 
     */
    @Override
    public void setActor(Entity actor) {
        this.actor = actor;
    }
    
    /**
     * 标记行为已经完成，调用该方法来使行为自动退出。
     */
    protected void end() {
        started = false;
    }    
    
    /**
     * 执行行为逻辑
     * @param tpf
     */
    protected abstract void doLogic(float tpf);

}
