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
package name.huliqing.luoying.object.attribute;

import name.huliqing.luoying.data.AttributeData;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * 属性接口定义
 * @author huliqing
 * @param <T>
 */
public interface Attribute<T> extends DataProcessor<AttributeData> {

    @Override
    public void setData(AttributeData data);

    @Override
    public AttributeData getData();

    /**
     * 更新属性值到Data中，以便存档
     */
    @Override
    public void updateDatas();
    
     /**
     * 初始化属性
     * @param am
     */
    void initialize(AttributeManager am);
    
    /**
     * 判断属性是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理
     */
    void cleanup();
    
    /**
     * 获取属性ID
     * @return 
     */
    String getId();
    
    /**
     * 获取属性名称
     * @return 
     */
    String getName();
    
    /**
     * 获取属性值
     * @return 
     */
    T getValue();
    
    /**
     * 设置属性的值
     * @param value 
     */
    void setValue(T value);
    
    /**
     * 添加值变侦听器
     * @param listener 
     */
    void addListener(ValueChangeListener listener);
    
    /**
     * 移除值变侦听器
     * @param listener
     * @return 
     */
    boolean removeListener(ValueChangeListener listener);
}
