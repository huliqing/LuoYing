/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author huliqing
 */
public class FeatureHelper {
    
    private final Map features;
    
    public FeatureHelper(Map features) {
        this.features = features;
    }
    
    public Map getFeatures() {
        return features;
    }
    
    /**
     * 以boolean方式获取参数，如果不存在指定参数则返回false.
     * @param key
     * @return 
     */
    public boolean getAsBoolean(String key) {
        Object value = get(key);
        if (value == null) 
            return false;
        return Boolean.valueOf((String) value);
    }
    
    /**
     * 以数组方式获取参数，如果原数据是字符串形式，则该方法将使用半角逗号","来拆分为数组
     * @param key
     * @return 
     */
    public String[] getAsArray(String key) {
        Object value = get(key);
        if (value == null) 
            return null;
        return ((String) value).split(",");
    }
    
    /**
     * 以List方式获取参数，如果原数据是字符串形式，则该方法将使用半角逗号","来拆分为列表
     * @param key
     * @return 
     */
    public List<String> getAsList(String key) {
        Object value = get(key);
        if (value == null) 
            return null;
        return Arrays.asList(((String) value).split(","));
    }
    
    private Object get(String key) {
        if (features == null)
            return null;
        return features.get(key);
    }
}
