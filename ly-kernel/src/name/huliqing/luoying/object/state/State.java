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
     * 状态的持有者，即受状态影响的角色
     * @return 
     */
    Entity getActor();
    
    /**
     * 设置状态的持有者，即受状态影响的角色，不能为null
     * @param actor 
     */
    void setActor(Entity actor);
    
    // remove201611xx
//    /**
//     * 状态的产生者，也就是说，这个状态是哪一个角色发出的, 可能为null.
//     * @return 
//     */
//    Entity getSourceActor();
//
//    /**
//     * 源角色，这个角色主要是指制造这个状态的源角色, 比如：角色A攻击了角色B, A的这个攻击技能对B产生
//     * 了一个“流血”状态。这时A即可以设置为这个“流血”状态的sourceActor。这样状
//     * 态在运行时就可以获得源角色的引用，以便知道谁产生了这个状态。对于一些状态
//     * 效果非常有用，比如“流血”这类伤害效果状态，这些状态在运行时要计算伤害，并
//     * 要知道是谁产生了这些伤害。
//     * @param sourceActor 
//     */
//    void setSourceActor(Entity sourceActor);
    
    /**
     * 获取获态的抵消值,这个值取值范围在[0.0~1.0].
     * @return 
     * @see #setResist(float) 
     */
    float getResist();
    
    /**
     * 设置状态削弱值，取值[0.0~1.0],该值主要用于削弱状态的作用，根据各
     * 种状态的实际情况各自实现该功能．0表示状态不削弱，1表示状态完全被削
     * 弱．0.5表示削弱一半，依此类推．示例：如实现一个击晕3秒的状态，如果
     * resist=0.3,则可实现最终的击晕时间为 3 * (1 - 0.3) = 2.1秒，换句话说，
     * 击晕效果被削弱了0.9秒．根据实现的不同，削弱方式可以完全不同，如实现
     * 一个中毒状态效果，你可以实现为削弱中毒时间，也可以实现为削弱中毒伤害
     * 等．
     * @param resist 
     */
    void setResist(float resist);
}
