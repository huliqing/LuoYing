/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import java.util.Map;
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
     * 设置要处理的属性名称
     * @param property 
     */
    void setProperty(String property);
    
    /**
     * 返回property name.
     * @return 
     */
    String getProperty();
    
    /**
     * 设置转换器的特殊参数
     * @param features 
     */
    void setFeatures(Map<String, Object> features);
    
    /**
     * 设置JFX编辑器
     * @param edit 
     */
    void setEdit(E edit);
    
    /**
     * 初始化
     * @param parent
     */
    void initialize(DataConverter<E, T> parent);
    
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
     * 更新属性转换器的JFX UI界面，该方法的调用源来自JME中的3D场景编辑事件。
     * 在更新JFX UI界面时不需要、也不应该再回调更新JME，以避免事件无限死循环。
     * @param propertyValue 
     */
    void updateView(Object propertyValue);
    
    /**
     * 更新属性转换器的值回3D场景物体, 这个方法由PropertyConverter发起调用，
     * 当JFX编辑界面编辑了属性的值之后可以调用这个方法来更新物体的参数值加3D场景的物体中。
     * @param propertyValue
     */
    void updateAttribute(Object propertyValue);
}
