/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
