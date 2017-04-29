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
package name.huliqing.editor.converter;

import java.util.List;
import java.util.Map;
import name.huliqing.editor.converter.define.Feature;

/**
 * @author huliqing
 */
public class FeatureHelper {
    
    protected Map<String, Feature> features;
    
    public FeatureHelper(Map<String, Feature> features) {
        this.features = features;
    }
    
    public String getAsString(String key) {
        if (features == null)
            return null;
        
        Feature feature = features.get(key);
        if (feature != null) {
            return feature.getValue();
        }
        return null;
    }
    
    /**
     * 以boolean方式获取参数，如果不存在指定参数则返回false.
     * @param key
     * @return 
     */
    public boolean getAsBoolean(String key) {
        if (features == null)
            return false;
        
        Feature feature = features.get(key);
        return feature != null && feature.getAsBoolean();
    }
    
    public Float getAsFloat(String key) {
        if (features == null)
            return null;
        
        Feature feature = features.get(key);
        if (feature != null) {
            return feature.getAsFloat();
        }
        return null;
    }
    
    public Integer getAsInteger(String key) {
        if (features == null)
            return null;
        
        Feature feature = features.get(key);
        if (feature != null) {
            return feature.getAsInteger();
        }
        return null;
    }
    
    /**
     * 以数组方式获取参数，如果原数据是字符串形式，则该方法将使用半角逗号","来拆分为数组
     * @param key
     * @return 
     */
    public String[] getAsArray(String key) {
        if (features == null)
            return null;
        
        Feature feature = features.get(key);
        if (feature == null)
            return null;
        
        return feature.getAsArray();
    }
    
    /**
     * 以List方式获取参数，如果原数据是字符串形式，则该方法将使用半角逗号","来拆分为列表
     * @param key
     * @return 
     */
    public List<String> getAsList(String key) {
        if (features == null)
            return null;
        
        Feature feature = features.get(key);
        if (feature == null)
            return null;
        
        return feature.getAsList();
    }
    
}
