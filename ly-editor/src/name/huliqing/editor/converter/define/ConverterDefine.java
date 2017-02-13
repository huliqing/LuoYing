/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.define;

import java.util.Collections;
import java.util.Map;
import name.huliqing.editor.converter.Converter;

/**
 *
 * @author huliqing
 */
public class ConverterDefine {
    private final Map<String, String> attributes;
//    private final Map<String, Feature> features;
    private final String name; // 转换器名称
    private final String value; // 转换器全限定类名
    private Class converterClass;
    
    public ConverterDefine(Map<String, String> attributes, Map<String, Feature> features) {
        this.attributes = attributes;
//        this.features = features != null ? Collections.unmodifiableMap(features) : null;
        this.name = attributes.get("name");
        this.value = attributes.get("value");
    }
    
    /**
     * 获取转换器的名称
     * @return 
     */
    public String getName() {
        return name;
    }
    
    /**
     * 创建Converter实例
     * @param <T>
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    public <T extends Converter> T createConverter() throws ClassNotFoundException
            , InstantiationException, IllegalAccessException {
        if (converterClass == null) {
            converterClass = Class.forName(value);
        }
        return (T) converterClass.newInstance();
    }
    
//    /**
//     * 获取转换器的配置
//     * @return 
//     */
//    public Map<String, Feature> getFeatures() {
//        return features;
//    }
}
