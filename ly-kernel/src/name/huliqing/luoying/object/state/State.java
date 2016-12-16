/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.state;

import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * 状态接口，一个角色可以拥有一个或多个状态。状态的特点如下：<br>
 * 1.状态只作用于接受状态的角色自身
 * 2.状态可以用来改变角色的各种属性或行为。
 * 3.状态有一定的时间限制，当时间结束之后状态会从角色身上移除。
 * @author huliqing
 * @param <T>
 */
public interface State<T extends StateData> extends  DataProcessor<T>{

    @Override
    public T getData();
    
    @Override
    public void setData(T data);
    
    /**
     * 初始化状态。
     */
    void initialize();
    
    /**
     * 判断状态是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 更新状态逻辑
     * @param tpf 
     */
    void update(float tpf);
    
    /**
     * 清理状态产生的数据，这个方法会在状态被移除时调用,以清理并释放状态产生的资源。
     */
    void cleanup();
    
    /**
     * 让状态时间重置回原点,这意味着当前状态会继续运行一个useTime的周期时间。
     */
    void rewind();
    
    /**
     * 判断状态是否已经结束
     * @return 
     */
    boolean isEnd();
    
    /**
     * 设置状态的持有者，即受状态影响的角色，不能为null
     * @param actor 
     */
    void setActor(Entity actor);
  
}
