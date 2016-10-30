/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.el2;

import javax.el.ELContext;
import javax.el.ValueExpression;
import name.huliqing.luoying.data.ObjectData;
import name.huliqing.luoying.el.ELFactory;

/**
 * EL抽象类
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractEL<T> implements EL<T>{
    private final static String ATTR_EXPRESSION = "expression";
    
    protected ObjectData data;
    protected String expression;
    
    // veel
    protected ValueExpression ve;
    /** 标记当前表达式是否有效，如无效则在获取表达式的值之前必须先重新创建ValueExpression */
    protected boolean valid;
    
    @Override
    public void setData(ObjectData data) {
        this.data = data;
        expression = data.getAsString(ATTR_EXPRESSION);
    }

    @Override
    public ObjectData getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        data.setAttribute(ATTR_EXPRESSION, expression);
    }

    @Override
    public String getExpression() {
        return expression;
    }

    @Override
    public void setExpression(String expression) {
        this.expression = expression;
        valid = false;
    }
    
    @Override
    public T getValue() {
        if (!valid) {
            ve = ELFactory.createValueExpression(getELContext(), expression, Object.class);
            valid = true;
        }
        return (T) ve.getValue(getELContext());
    }
    
    /**
     * 获取ELContext
     * @return 
     */
    protected abstract ELContext getELContext();
}
