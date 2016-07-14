/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.actorlogic;

/**
 * @author huliqing
 */
public interface LogicProcessor {
    
    /**
     * 更新逻辑
     * @param tpf 
     */
    void update(float tpf);
    
    /**
     * 添加一个逻辑
     * @param logicData 
     */
    void addLogic(ActorLogic logic);
    
    /**
     * 移除指定的逻辑
     * @param logic 
     */
    boolean removeLogic(ActorLogic logic);
    
    /**
     * 清理掉当前所有逻辑
     */
    void cleanup();
}
