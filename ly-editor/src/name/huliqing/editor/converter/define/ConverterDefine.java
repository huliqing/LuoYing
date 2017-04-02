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
