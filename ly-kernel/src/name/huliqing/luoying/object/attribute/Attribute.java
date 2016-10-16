/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

import name.huliqing.luoying.data.AttributeData;
import name.huliqing.luoying.object.module.AttributeModule;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * 属性接口定义
 * @author huliqing
 */
public interface Attribute extends DataProcessor<AttributeData> {

    @Override
    public void setData(AttributeData data);

    @Override
    public AttributeData getData();

    @Override
    public void updateDatas();
    
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
    
}
