/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.define;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author huliqing
 */
public class Feature {
    
    private final Map<String, String> data;
    private final String name;
    private final String value;
    
    public Feature(Map<String, String> data) {
        this.data = data;
        this.name = data.get("name");
        this.value = data.get("value");
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
    
    /**
     * 以boolean方式获取参数，如果不存在指定参数则返回false.
     * @return 
     */
    public boolean getAsBoolean() {
        if (value == null) 
            return false;
        return Boolean.valueOf((String) value);
    }
    
    /**
     * 以数组方式获取参数，如果原数据是字符串形式，则该方法将使用半角逗号","来拆分为数组
     * @return 
     */
    public String[] getAsArray() {
        if (value == null) 
            return null;
        return value.split(",");
    }
    
    /**
     * 以List方式获取参数，如果原数据是字符串形式，则该方法将使用半角逗号","来拆分为列表
     * @return 
     */
    public List<String> getAsList() {
        if (value == null) 
            return null;
        return Arrays.asList((value).split(","));
    }
}
