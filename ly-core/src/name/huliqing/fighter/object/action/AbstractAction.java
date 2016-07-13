/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.action;

import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.data.ActionData;

/**
 * 行为逻辑基础
 * @author huliqing
 */
public abstract class AbstractAction implements Action {
    protected ActionData data;
    protected Actor actor;
    // 行为是否正在执行
    protected boolean started;
    
    // 行为的锁定。停止行为的更新一定时间
    private boolean locked;
    private float lockTime;
    private float lockTimeUsed;
    
    public AbstractAction() {}

    @Override
    public void setData(ActionData data) {
        this.data = data;
    }

    @Override
    public ActionData getData() {
        return data;
    }
    
    /**
     * 启动行为，以下的任一情况会重启调用该方法。
     * 1.行为开始执行时或结束后重新执行时。
     * 2.从另一个行为切换到当前行为时
     */
    @Override
    public final void start() {
        started = true;
        locked = false;
        lockTime = 0;
        lockTimeUsed = 0;
        
        doInit();
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
    public final void setActor(Actor actor) {
        this.actor = actor;
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
        started = false;
    }
    
    /**
     * 标记行为已经完成，调用该方法来使行为自动退出。
     */
    protected void end() {
        started = false;
    }    
    
    /**
     * 初始化action
     */
    protected abstract void doInit();
    
    /**
     * 执行行为逻辑
     * @param tpf
     */
    protected abstract void doLogic(float tpf);

}
