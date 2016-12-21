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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author huliqing
 */
public class TaskStepControl {
    
    // 当前正在执行的任务
    private TaskStep current;
    
    // 已经执行完的task数
    private int finishCount;
    
    // 任务列表
    protected final List<TaskStep> tasks = new ArrayList<TaskStep>();
    
    public void addTask(TaskStep task) {
        tasks.add(task);
    }
    
    public void update(float tpf) {
        // update tasks
        if (current.isFinished()) {
            finishCount++;
            if (hasNext()) {
                doNext();
            } else {
                // end
            }
        } else {
            current.update(tpf);
        }
    }
    
    public void cleanup() {
        for (TaskStep task : tasks) {
            if (!task.isFinished()) {
                task.cleanup();
            }
        }
        tasks.clear();
        finishCount = 0;
        current = null;
    }
    
     /**
     * 是否有下一个任务
     * @return 
     */
    public boolean hasNext() {
        return finishCount < tasks.size();
    }
    
    /**
     * 执行下一个任务
     */
    public void doNext() {
        TaskStep previous = current;
        if (current == null) {
            current = tasks.get(0);
        } else {
            int i = tasks.indexOf(current) + 1; // 由外部判断是否有hasNext
            current = tasks.get(i);
        }
        current.start(previous);
    }
}
