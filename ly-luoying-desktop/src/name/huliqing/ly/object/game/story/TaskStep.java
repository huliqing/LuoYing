/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
