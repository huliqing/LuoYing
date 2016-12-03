/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

import name.huliqing.luoying.data.AttributeData;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * 属性接口定义
 * @author huliqing
 * @param <T>
 */
public interface Attribute<T> extends DataProcessor<AttributeData> {

    @Override
    public void setData(AttributeData data);

    @Override
    public AttributeData getData();

    /**
     * 更新属性值到Data中，以便存档
     */
    @Override
    public void updateDatas();
    
     /**
     * 初始化属性
     * @param am
     */
    void initialize(AttributeManager am);
    
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
    
    /**
     * 获取属性值
     * @return 
     */
    T getValue();
    
    /**
     * 设置属性的值
     * @param value 
     */
    void setValue(T value);
    
    /**
     * 添加值变侦听器
     * @param listener 
     */
    void addListener(ValueChangeListener listener);
    
    /**
     * 移除值变侦听器
     * @param listener
     * @return 
     */
    boolean removeListener(ValueChangeListener listener);
}
