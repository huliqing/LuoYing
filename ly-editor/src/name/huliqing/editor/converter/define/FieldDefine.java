/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.define;

import java.util.Collections;
import java.util.Map;

/**
 *
 * @author huliqing
 */
public class FieldDefine {
    private final Map<String, String> attributes;
    /** 特殊参数配置 */
    private final Map<String, Feature> features;
    
    public FieldDefine(Map<String, String> attributes, Map<String, Feature> features) {
        this.attributes = attributes;
        this.features = features != null ? Collections.unmodifiableMap(features) : null;
    }
    
    /**
     * 获取转换的属性名称
     * @return 
     */
    public String getField() {
        return attributes.get("name");
    }
    
    /**
     * 获取转换器名称
     * @return 
     */
    public String getConverter() {
        return attributes.get("converter");
    }

    public Map<String, Feature> getFeatures() {
        return features;
    }
    
}
