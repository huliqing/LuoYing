/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.task;

import name.huliqing.core.data.TaskData;
import name.huliqing.core.xml.DataProcessor;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.ui.UI;

/**
 * 任务
 * @author huliqing
 * @param <T>
 */
public interface Task<T extends TaskData> extends DataProcessor<T> {

    @Override
    public T getData();
    
    /**
     * 初始化任务
     */
    void initialize();
    
    /**
     * 清理任务
     */
    void cleanup();
    
    /**
     * 设置任务的执行角色
     * @param actor 
     */
    void setActor(Actor actor);
    
    /**
     * 获取当前任务的执行者
     * @return 
     */
    Actor getActor();
    
    /**
     * 获取任务ID
     * @return 
     */
    String getId();
    
    /**
     * 检查任务是否结束
     * @return 
     */
    boolean checkCompletion();
    
    /**
     * 执行任务“完成”，比如奖励角色经验，任务物品等。。。
     */
    void doCompletion();
    
    /**
     * 获取任务情报,返回一个UI内容包含任务详细信息，如任务说明，奖励情况，
     * 任务进度等
     * @return 
     */
    UI getTaskDetail();
}
