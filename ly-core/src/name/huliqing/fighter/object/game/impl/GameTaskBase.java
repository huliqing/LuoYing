/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game.impl;

/**
 * 简单的游戏任务逻辑
 * @author huliqing
 */
public abstract class GameTaskBase implements GameTask{

    protected boolean started;
    
    public GameTaskBase() {}

    @Override
    public final void start(GameTask previous) {
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
    protected abstract void doInit(GameTask previous);
    
    /**
     * 执行任务逻辑
     * @param tpf 
     */
    protected abstract void doLogic(float tpf);

    
}
