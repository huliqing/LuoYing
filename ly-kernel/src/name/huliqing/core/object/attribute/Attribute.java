/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.attribute;

import name.huliqing.core.data.AttributeData;
import name.huliqing.core.object.module.AttributeModule;
import name.huliqing.core.xml.DataProcessor;

/**
 *
 * @author huliqing
 */
public interface Attribute extends DataProcessor<AttributeData> {

    @Override
    public void setData(AttributeData data);

    @Override
    public AttributeData getData();
    
     /**
     * 初始化属性
     * @param module
     */
    void initialize(AttributeModule module);
    
    /**
     * 判断属性是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理
     */
    void cleanup();
    
    /**
     * 获取属性ID
     * @return 
     */
    String getId();
    
    /**
     * 获取属性名称
     * @return 
     */
    String getName();
    
//    /**
//     * 获取属性值。
//     * @return 
//     */
//    V getValue();
//
//    /**
//     * 设置属性值。
//     * @param value 
//     */
//    void setValue(V value);
//    
//    /**
//     * @param other
//     * @return 
//     */
//    boolean match(Object other);
    
//    /**
//     * 添加一个侦听器用于监听属性值的变化
//     * @param listener 
//     */
//    void addListener(ValueChangeListener<V> listener);
//    
//    /**
//     * 移除属性侦听器
//     * @param listener
//     * @return 
//     */
//    boolean removeListener(ValueChangeListener<V> listener);
}
