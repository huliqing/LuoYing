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
 * 任务步骤,每个TaskStep定义一个简单的任何步骤
 * @author huliqing
 */
public interface TaskStep {

    /**
     * 启动任务
     * @param previous 前一个任务，如果没有则为null.一些任务可能需要前一个任务
     * 所创建的Object.
     */
    void start(TaskStep previous);
    
    /**
     * 更新任务逻辑
     * @param tpf
     */
    void update(float tpf);
    
    /**
     * 判断任务是否完成，每个任务都应该在某一种可能的情况下结束，否则任务会
     * 一直执行。
     * @return 
     */
    boolean isFinished();
    
    /**
     * 清理数据
     */
    void cleanup();
    
}
