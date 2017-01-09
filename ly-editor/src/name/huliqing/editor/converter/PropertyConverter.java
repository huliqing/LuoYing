/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import javafx.scene.Node;
import name.huliqing.luoying.xml.ObjectData;

/**
 * @author huliqing
 * @param <T>
 */
public interface PropertyConverter<T extends ObjectData> {
    
    /**
     * 初始化
     * @param parent
     * @param property 
     */
    void initialize(DataConverter<T> parent, String property);
    
    /**
     * 获取界面节点
     * @return 
     */
    Node getNode();
    
    /**
     * 通过父组件，当前字段转换器发生了变化
     */
    void notifyChangedToParent();
}
