///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.state;
//
//import name.huliqing.fighter.data.StateData;
//import name.huliqing.fighter.object.DataProcessor;
//import name.huliqing.fighter.object.actor.Actor;
//
///**
// * 角色“状态”接口.状态主要作用如下
// * 1.属性状态：主要改变角色的属性能力，如提升或降低角色的防御力、攻击力、移除速度、
// * 攻击速度、连招能力、AI、视角范围等等只要是属于角色属性的都可以进行改变。
// * 2.非属性状态，主要指除角色属性能力状态之外的其它状态，
// * 如产生持续伤害的中毒状态；产生不能移动效果的晕眩状态；
// * @author huliqing
// */
//public interface State<T extends StateData> extends DataProcessor<T>{
//    
//    /**
//     * 设置状态所作用的角色
//     * @param actor 
//     */
//    void setActor(Actor actor);
//    
//    /**
//     * 获取状态所作用的角色
//     * @return 
//     */
//    Actor getActor();
//    
//    /**
//     * 判断状态是否已经结束
//     * @return 
//     */
//    boolean isEnd();
//    
//    /**
//     * 获得状态类型ID（是类型id,非唯一id）
//     * @return 
//     */
//    String getId();
//    
//    /**
//     * 开始运行状态
//     */
//    void start();
//    
//    /**
//     * 更新状态逻辑
//     * @param tpf 
//     */
//    void update(float tpf);
//    
//    /**
//     * 清理状态所产生的效果，非常重要，当状态时间结束后应该清理掉状态所产生的效果。
//     * 如增加了移动速度的效果，在状态时间结束时应该恢复移动速度。
//     */
//    void cleanup();
//    
//    /**
//     * 让状态时间重置回原点,这意味着当前状态会继续运行一个useTime的周期时间。
//     */
//    void rewind();
//}
