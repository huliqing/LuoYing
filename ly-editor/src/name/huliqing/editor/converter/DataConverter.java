/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import java.util.List;
import javafx.scene.layout.Pane;
import name.huliqing.luoying.xml.ObjectData;

/**
 *
 * @author huliqing
 * @param <T>
 */
public interface DataConverter<T extends ObjectData> {
    
    /**
     * 获取数据
     * @return 
     */
    T getData();
    
    Pane getLayout();
    
    void initialize(T data, List<PropertyConverterDefine> propertyConverterDefines, PropertyConverter parent);

    /**
     * 通知父组件，当前Data转换器的值发生了变化
     */
    void notifyChangedToParent();
}
