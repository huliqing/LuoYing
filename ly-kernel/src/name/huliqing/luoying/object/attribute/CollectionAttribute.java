/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

import java.util.Collection;

/**
 * @author huliqing
 * @param <E>
 */
public interface CollectionAttribute<E> {
    
    /**
     * Collection监听接口,用于监听列表的变化
     * @param <E> 
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
    
    /**
     * 判断当前元素数量
     * @return 
     */
    int size();
    
    /**
     * 判断集合是否为空
     * @return 
     */
    boolean isEmpty();
    
    /**
     * 添加一个元素
     * @param e 
     */
    void add(E e);
    
    /**
     * 移除一个元素
     * @param e
     * @return 
     */
    boolean remove(E e);
    
    /**
     * 获取数据列表
     * @return 
     */
    Collection<E> values();
    
    boolean contains(E object);
    
    /**
     * 添加一个侦听器,用于监听元素的添加或删除
     * @param listener 
     */
    void addListener(CollectionChangeListener listener);
    
    /**
     * 移除一个侦听器。
     * @param listener
     * @return 
     */
    boolean removeListener(CollectionChangeListener listener);
    

    
}
