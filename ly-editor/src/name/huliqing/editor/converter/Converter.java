/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import java.util.Map;
import javafx.scene.Node;
import name.huliqing.editor.converter.define.Feature;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 数据转换器，将给定的数据转换为可视的jfx组件节点。
 * @author huliqing
 * @param <T>
 * @param <C>
 */
public interface Converter<T extends ObjectData, C extends Converter> {
    
    /**
     * 设置要转换的数据类型
     * @param data 
     */
    void setData(T data);
    
    /**
     * 设置数据转换参数
     * @param features 
     */
    void setFeatures(Map<String, Feature> features);
    
    /**
     * 设置父转换器
     * @param parent 
     */
    void setParent(C parent);
    
    /**
     * 获取转换后的JFX组件节点。
     * @return 
     */
    Node getLayout();
    
    /**
     * 初始化转换器
     */
    void initialize();
    
    /**
     * 判断转换器是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理转换器
     */
    void cleanup();
    
    /**
     * 通过当前转换器发生了变化
     */
    void notifyChanged();
    
    
}
