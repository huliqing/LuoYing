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

import java.util.Map;
import javafx.scene.layout.Region;
import name.huliqing.editor.converter.define.Feature;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 数据转换器，将给定的数据转换为可视的jfx组件节点。
 * @author huliqing
 * @param <T>
 * @param <C>
 */
public interface Converter<T extends ObjectData, C extends Converter> {
    
    /**
     * 设置要转换的数据类型
     * @param data 
     */
    void setData(T data);
    
    T getData();
    
    /**
     * 设置数据转换参数
     * @param features 
     */
    void setFeatures(Map<String, Feature> features);
    
    /**
     * 设置父转换器
     * @param parent 
     */
    void setParent(C parent);
    
    C getParent();
    
    /**
     * 获取转换后的JFX组件节点。
     * @return 
     */
    Region getLayout();
    
    /**
     * 初始化转换器
     */
    void initialize();
    
    /**
     * 判断转换器是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理转换器
     */
    void cleanup();
    
    /**
     * 通过当前转换器发生了变化
     */
    void notifyChanged();
    
    
}
