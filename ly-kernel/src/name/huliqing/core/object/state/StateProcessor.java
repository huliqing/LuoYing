/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.state;

import java.util.List;

/**
 * 角色状态管理器
 * @author huliqing
 */
public interface StateProcessor {
    
    /**
     * 更新状态逻辑.
     * @param tpf 
     */
    void update(float tpf);

    /**
     * 给指定角色添加一个状态,如果添加成功则返回true,否则返回false.
     */
    void addState(State state);
    
    /**
     * 移除角色身上指定ID的状态，如果状态不存在，则返回null,否则返回移除后的
     * 状态。
     * @param stateId
     */
    State removeState(String stateId);
    
    /**
     * 从角色身上找到指定ID的状态。
     * @param stateId
     * @return 
     */
    State findState(String stateId);
    
//    /**
//     * 获取当前所有状态
//     * @return 
//     */
//    List<State> getStates();
    
    /**
     * 清理数据释放资源
     */
    void cleanup();
}
