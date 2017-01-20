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
 * @param <T> DataConverter所要转换的数据类型
 */
public interface DataConverter<E extends JfxAbstractEdit, T extends ObjectData> {
    
    /** 指定要隐藏的字段, 格式:"field1,field2,..." */
    public final static String FEATURE_HIDE_FIELDS = "hideFields";
    
    /**
     * 获取数据
     * @return 
     */
    T getData();
    
    void setData(T data);
    
    void setPropertyConverterDefines(Map<String, PropertyConverterDefine> propertyConvertDefines);
    
    void setFeatures(Map<String, Object> features);
    
    void setEdit(E edit);
    
    /**
     * 初始化转换器
     * @param parent 
     */
    void initialize(PropertyConverter parent);
    
    /**
     * 判断是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理释放资源
     */
    void cleanup();
    
    Node getLayout();
    
    /**
     * 通知父组件，当前Data转换器的值发生了变化
     */
    void notifyChangedToParent();
    
    void addUndoRedo(String property, Object beforeValue, Object afterValue);
}
