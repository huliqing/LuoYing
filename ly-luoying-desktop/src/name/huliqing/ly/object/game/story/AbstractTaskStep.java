/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game.story;

import name.huliqing.ly.object.game.story.TaskStep;

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
