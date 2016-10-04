/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.state;

import name.huliqing.core.data.StateData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.xml.DataProcessor;

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
     * 状态的持有者，即受状态影响的角色
     * @return 
     */
    Actor getActor();
    
    /**
     * 设置状态的持有者，即受状态影响的角色，不能为null
     * @param actor 
     */
    void setActor(Actor actor);
    
    /**
     * 状态的产生者，也就是说，这个状态是哪一个角色发出的, 可能为null.
     * @return 
     */
    Actor getSourceActor();

    /**
     * 源角色，这个角色主要是指制造这个状态的源角色, 比如：角色A攻击了角色B, A的这个攻击技能对B产生
     * 了一个“流血”状态。这时A即可以设置为这个“流血”状态的sourceActor。这样状
     * 态在运行时就可以获得源角色的引用，以便知道谁产生了这个状态。对于一些状态
     * 效果非常有用，比如“流血”这类伤害效果状态，这些状态在运行时要计算伤害，并
     * 要知道是谁产生了这些伤害。
     * @param sourceActor 
     */
    void setSourceActor(Actor sourceActor);
}
