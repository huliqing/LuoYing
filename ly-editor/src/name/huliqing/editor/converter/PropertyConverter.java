/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import javafx.scene.Node;
import name.huliqing.editor.edit.JfxAbstractEdit;
import name.huliqing.luoying.xml.ObjectData;

/**
 * @author huliqing
 * @param <E>
 * @param <T>
 */
public interface PropertyConverter<E extends JfxAbstractEdit, T extends ObjectData> {
    
    /**
     * 初始化
     * @param editView
     * @param parent
     * @param property 
     */
    void initialize(E editView, DataConverter<E, T> parent, String property);
    
    /**
     * 判断是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理释放资源占用
     */
    void cleanup();
    
    /**
     * 获取界面节点
     * @return 
     */
    Node getLayout();
    
    /**
     * 通过父组件，当前字段转换器发生了变化
     */
    void notifyChangedToParent();
    
    /**
     * 将属性值更新到jfx组件中,该方法由当前组件的父组件（DataConverter）调用。
     * @param propertyValue 
     */
    void updateView(Object propertyValue);
}
