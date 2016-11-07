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
    void addCollectionChangeListener(CollectionChangeListener listener);
    
    /**
     * 移除一个侦听器。
     * @param listener
     * @return 
     */
    boolean removeCollectionChangeListener(CollectionChangeListener listener);
    

    
}
