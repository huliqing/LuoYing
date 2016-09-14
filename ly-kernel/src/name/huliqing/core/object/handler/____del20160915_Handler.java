///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.object.handler;
//
//import name.huliqing.core.data.HandlerData;
//import name.huliqing.core.data.ObjectData;
//import name.huliqing.core.xml.DataProcessor;
//import name.huliqing.core.object.actor.Actor;
//
///**
// * 定义物品的使用方式，各种各样的ProtoData的使用方法可能都是不一样的。
// * @author huliqing
// * @param <T>
// */
//public interface Handler<T extends HandlerData> extends DataProcessor<T> {
//    
//    /**
//     * 移除指定的物品
//     * @param actor 目标角色
//     * @param data 物品id
//     * @param count 要移除的数量
//     * @return 
//     */
//    boolean remove(Actor actor, ObjectData data, int count);
//    
//    /**
//     * 是否能够使用该物品
//     * @param actor 使用物品的角色id
//     * @param data 
//     * @return 
//     */
//    boolean canUse(Actor actor, ObjectData data);
//    
//    /**
//     * 强制使用物品
//     * @param actor
//     * @param data
//     */
//    void useForce(Actor actor, ObjectData data);
//}
