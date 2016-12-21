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

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.attribute.AttributeManager;

/**
 * 这个ELContext用于处理实体(Entity)的属性值。
 * @author huliqing
 */
public class AttributeElContext extends ELContext{
    
    private final AttributeELResolver elResolver = new AttributeELResolver();
    private final SimpleFunctionMapper functionMapper = new SimpleFunctionMapper();
    private final SimpleVariableMapper variableMapper = new SimpleVariableMapper();

    private El el;
    
    public AttributeElContext(El el) {
        this.el = el;
    }
    
    @Override
    public ELResolver getELResolver() {
        return elResolver;
    }

    @Override
    public VariableMapper getVariableMapper() {
        return variableMapper;
    }

    @Override
    public FunctionMapper getFunctionMapper() {
        return functionMapper;
    }
    
    /**
     * 添加、设置基本变量值。
     * @param key
     * @param value 
     */
    public void setAttributeManager(String key, AttributeManager value) {
        elResolver.setBaseValue(key, value);
    }
    
    /**
     * 给ELContext设置基本变量
     * @param key
     * @param value 
     */
    public void setBaseValue(String key, Object value) {
        elResolver.setBaseValue(key, value);
    }
    
    private final class AttributeELResolver extends SimpleElResolver {
        
        public AttributeELResolver() {
            super(2);
        }
        
        @Override
        public Object getValue(ELContext context, Object base, Object property) {
             if (base instanceof AttributeManager) {
                AttributeManager am = (AttributeManager) base;
                context.setPropertyResolved(true);
                Attribute attribute = am.getAttribute(property.toString());
                if (attribute == null) {
                    throw new NullPointerException("Attribute not found from AttributeManager, AttributeManager=" + am 
                            + ", attribute=" + property 
                            + ", expression=" + el.getExpression() 
                            + ", el=" + el.getData().getId()); 
                }
                return attribute.getValue();
            }
            Object value = super.getValue(context, base, property);
            if (value == null) {
                throw new NullPointerException("Could find property value from baseValue, check if the property exists"
                        + ", base=" + base + ", property=" + property 
                        + ", expression=" + el.getExpression() 
                        + ", el=" + el.getData().getId());
            }
            return value;
        }
    }
    
}
