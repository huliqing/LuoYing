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
package name.huliqing.ly.object.game.story;

/**
 * 简单的游戏任务逻辑
 * @author huliqing
 */
public abstract class AbstractTaskStep implements TaskStep{

    protected boolean started;
    
    public AbstractTaskStep() {}

    @Override
    public final void start(TaskStep previous) {
        doInit(previous);
        started = true;
    }

    @Override
    public void update(float tpf) {
        if (!started) return;
        doLogic(tpf);
    }
    
    @Override
    public void cleanup() {
        started = false;
    }

    /**
     * 初始化任务
     * @param previous 前一个任务，部分任务可能需要前一个任务的信息。
     */
    protected abstract void doInit(TaskStep previous);
    
    /**
     * 执行任务逻辑
     * @param tpf 
     */
    protected abstract void doLogic(float tpf);

    
}
