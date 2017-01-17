/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author huliqing
 */
public class PropertyConverterDefine {
    
    private final String propertyName;
    private final Class<? extends PropertyConverter> propertyConverter;
        
    /** 特殊参数配置 */
    private final Map<String, Object> features = new LinkedHashMap<String, Object>();

    public PropertyConverterDefine(String propertyName, Class<? extends PropertyConverter> propertyConverter) {
        this.propertyName = propertyName;
        this.propertyConverter = propertyConverter;
    }
    
    /**
     * 添加一些配置参数。
     * @param key
     * @param value 
     * @return  返回当前实例
     */
    public PropertyConverterDefine addFeature(String key, Object value) {
        features.put(key, value);
        return this;
    }
    
    public String getPropertyName() {
        return propertyName;
    }
    
    public Class<? extends PropertyConverter> getPropertyConverter() {
        return propertyConverter;
    }

    /**
     * 获取参数配置，只能只读
     * @return 
     */
    public Map<String, Object> getUnmodifiableFeatures() {
        return Collections.unmodifiableMap(features);
    }
}
