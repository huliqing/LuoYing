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
public class DataDefine {

    private static final Logger LOG = Logger.getLogger(DataDefine.class.getName());
    
    private final Map<String, String> attributes;
    /** 参数转换器 */
    private final Map<String, FieldDefine> fieldDefines = new LinkedHashMap();
    /** 特殊参数配置 */
    private final Map<String, Feature> features = new LinkedHashMap();
    
    private boolean extendsResolved;    
    
    public DataDefine(Map<String, String> attributes, Map<String, FieldDefine> fieldDefines, Map<String, Feature> features) {
        this.attributes = attributes;
        if (fieldDefines != null) {
            this.fieldDefines.putAll(fieldDefines);
        }
        if (features != null) {
            this.features.putAll(features);
        }
    }
    
    public String getName() {
        return attributes.get("name");
    }
    
    public String getConverter() {
        extendsResolve();
        return attributes.get("converter");
    }
    
    public Map<String, FieldDefine> getFieldDefines() {
        extendsResolve();
        return Collections.unmodifiableMap(fieldDefines);
    }
    
    public Map<String, Feature> getFeatures() {
        extendsResolve();
        return Collections.unmodifiableMap(features);
    }
        
    Map<String, String> getData() {
        extendsResolve();
        return attributes;
    }
    
    private void extendsResolve() {
        if (extendsResolved) {
            return;
        }
        String _extends = attributes.get("extends");
        if (_extends != null && !_extends.trim().isEmpty()) {
            DataDefine parent = ConverterManager.getDataDefine(_extends);
            if (parent == null) {
                LOG.log(Level.WARNING, "DataDefine not found by extends={0}, dataDefine={1}"
                        , new Object[] {_extends, this});
            } else {
                // Data属性继承自父引用
                Map<String, String> tempData = new LinkedHashMap();
                tempData.putAll(attributes);
                attributes.clear();
                attributes.putAll(parent.getData());
                attributes.putAll(tempData);
                
                // 先父类所定义的转换器，然后再用子类的定义覆盖父类
                Map<String, FieldDefine> tempConverter = new LinkedHashMap();
                tempConverter.putAll(fieldDefines);
                fieldDefines.clear();
                fieldDefines.putAll(parent.getFieldDefines());
                fieldDefines.putAll(tempConverter);

                Map<String, Feature> tempFeatures = new LinkedHashMap();
                tempFeatures.putAll(features);
                features.clear();
                features.putAll(parent.getFeatures());
                features.putAll(tempFeatures);
            }
        }
        extendsResolved = true;
    }
}
