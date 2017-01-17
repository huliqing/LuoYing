/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.manager.ConverterManager;

/**
 *
 * @author huliqing
 */
public class ConverterDefine {

    private static final Logger LOG = Logger.getLogger(ConverterDefine.class.getName());
    
    /** 匹配的tagName */
    private final String tagName;
    
    /** 主转换器 */
    private final Class<? extends DataConverter> converter;
    
    /** 参数转换器 */
    private final Map<String, PropertyConverterDefine> propertyConverters = new LinkedHashMap();
    
    /** 特殊参数配置 */
    private final Map<String, Object> features = new LinkedHashMap<String, Object>();
    
    /** extendsFrom super tagName */
    private String extendsTag;
    private boolean extendsResolved;

    public ConverterDefine(String tagName, Class<? extends DataConverter> converterClass) {
        this.tagName = tagName;
        this.converter = converterClass;
    }
    
    public String getTagName() {
        return tagName;
    }
    
    public Class<? extends DataConverter> getConverter() {
        return converter;
    }

    public Map<String, PropertyConverterDefine> getPropertyConverters() {
        extendsResolve();
        return Collections.unmodifiableMap(propertyConverters);
    }

    public Map<String, Object> getFeatures() {
        extendsResolve();
        return Collections.unmodifiableMap(features);
    }
    
    /**
     * 添加属性字段转换器
     * @param property 字段名
     * @param propertyConverter 转换器类型
     * @return 
     */
    public PropertyConverterDefine addPropertyConverter(String property, Class<? extends PropertyConverter> propertyConverter) {
        PropertyConverterDefine pcd = new PropertyConverterDefine(property, propertyConverter);
        propertyConverters.put(property, pcd);
        return pcd;
    }

    /**
     * 添加一些配置参数。
     * @param key
     * @param value 
     * @return  
     */
    public ConverterDefine addFeature(String key, Object value) {
        features.put(key, value);
        return this;
    }

    /**
     * 继承父级设置，继承的时候会同时继承父级的字段转换器和features设置。
     * @param parentTagName
     * @return 
     */
    public ConverterDefine extendsFrom(String parentTagName) {
        this.extendsTag = parentTagName;
        return this;
    }


    private void extendsResolve() {
        if (extendsResolved) {
            return;
        }
        if (extendsTag != null) {
            ConverterDefine parent = ConverterManager.getConverterDefine(extendsTag);
            if (parent == null) {
                LOG.log(Level.WARNING, "ConverterDefine not found by extendsTag={0}, tag={1}"
                        , new Object[] {extendsTag, tagName});
            } else {
                // 先父类所定义的转换器，然后再用子类的定义覆盖父类
                Map<String, PropertyConverterDefine> tempConverter = new LinkedHashMap();
                tempConverter.putAll(propertyConverters);
                propertyConverters.clear();
                propertyConverters.putAll(parent.getPropertyConverters());
                propertyConverters.putAll(tempConverter);

                Map<String, Object> tempFeatures = new LinkedHashMap<String, Object>();
                tempFeatures.putAll(features);
                features.clear();
                features.putAll(parent.getFeatures());
                features.putAll(tempFeatures);
            }
        }
        extendsResolved = true;
    }
}
