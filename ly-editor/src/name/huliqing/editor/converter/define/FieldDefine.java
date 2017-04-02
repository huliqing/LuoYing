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
