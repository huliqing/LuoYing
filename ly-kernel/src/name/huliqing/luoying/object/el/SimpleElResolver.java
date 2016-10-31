/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
