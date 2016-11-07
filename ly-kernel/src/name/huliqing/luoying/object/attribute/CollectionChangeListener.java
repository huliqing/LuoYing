/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

/**
 * Collection监听接口,用于监听列表的变化
 * @author huliqing
 */
public interface CollectionChangeListener<E> {
    
    /**
     * 当列表中添加了一个新元素时该方法被调用。
     * @param added 新添加的元素
     */
    void onAdded(E added);

    /**
     * 当列表移除了一个元素时该方法被调用。
     * @param removed 已被移除的元素
     */
    void onRemoved(E removed);
    
}
