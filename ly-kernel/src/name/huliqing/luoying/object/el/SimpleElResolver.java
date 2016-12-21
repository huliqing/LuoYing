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
package name.huliqing.luoying.object.el;

import java.beans.FeatureDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.MapELResolver;

/**
 * 简单的ELResolver
 * @author huliqing
 */
public class SimpleElResolver extends ELResolver {

    private final ELResolver delegate = new MapELResolver();
    private final Map<String, Object> baseMap;

    public SimpleElResolver() {
        this(4);
    }
    
    public SimpleElResolver(int initialCapacity) {
        baseMap = new HashMap<String, Object>(initialCapacity);
    }
    
    /**
     * 获取基本变量值，如果不存在则返回null.
     * @param key
     * @return 
     */
    public Object getBaseValue(String key) {
        return baseMap.get(key);
    }
    
    /**
     * 设置基本变量
     * @param key
     * @param value 
     */
    public void setBaseValue(String key, Object value) {
        baseMap.put(key, value);
    }
    
    /**
     * 获取基本变量值窗器
     * @return 
     */
    public Map<String, Object> getBaseMap() {
        return baseMap;
    }
    
    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        if (base == null) {
            base = baseMap;
        }
        return delegate.getValue(context, base, property);
    }

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base == null) {
            base = baseMap;
        }
        return delegate.getCommonPropertyType(context, base);
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        if (base == null) {
            base = baseMap;
        }
        return delegate.getFeatureDescriptors(context, base);
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        if (base == null) {
            base = baseMap;
        }
        return delegate.getType(context, base, property);
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (base == null) {
            base = baseMap;
        }
        return delegate.isReadOnly(context, base, property);
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
        if (base == null) {
            base = baseMap;
        }
        delegate.setValue(context, base, property, value);
    }

}
