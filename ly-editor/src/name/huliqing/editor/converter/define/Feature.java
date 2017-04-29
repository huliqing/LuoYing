/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
    
    public Float getAsFloat() {
        if (value == null) 
            return null;
        
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    public Integer getAsInteger() {
        if (value == null) 
            return null;
        
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    public Long getAsLong() {
        if (value == null) 
            return null;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
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
