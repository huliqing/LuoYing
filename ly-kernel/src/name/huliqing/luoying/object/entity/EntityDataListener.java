/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity;

import name.huliqing.luoying.xml.ObjectData;

/**
 * EntityDataListener用于监听实体的数据变化，当数据流转入实体时，如添加、移除、使用数据时都会触发数据侦听器。
 * 外部应用可以通过给实体添加EntityDataListener来监听数据的变化。
 * @author huliqing
 */
public interface EntityDataListener {
    
    /**
     * 当实体添加了数据时该方法被调用。
     * @param data
     * @param amount
     */
    void onDataAdded(ObjectData data, int amount);
    
    /**
     * 当实体移除了数据时该方法被调用。
     * @param data
     * @param amount 
     */
    void onDataRemoved(ObjectData data, int amount);
    
    /**
     * 当实体使用了一个数据时该方法被调用。
     * @param data 
     */
    void onDataUsed(ObjectData data);
}
