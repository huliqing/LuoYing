/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game;

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
