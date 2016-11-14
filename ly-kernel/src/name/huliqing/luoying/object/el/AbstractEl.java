/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.el;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.el.ValueExpression;
import name.huliqing.luoying.Config;
import name.huliqing.luoying.xml.ObjectData;

/**
 * El抽象类
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractEl<T> implements El<T>{
    private static final Logger LOG = Logger.getLogger(AbstractEl.class.getName());
    
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
            ve = ElFactory.createValueExpression(getELContext(), expression, Object.class);
            valid = true;
        }
        T result = (T) ve.getValue(getELContext());
//        if (Config.debug) {
//            LOG.log(Level.INFO, "El getValue, result={0}, expression={1}, el={2}", new Object[] {result, expression, getData().getId()});
//        }
        return result;
    }
    
    /**
     * 获取ELContext
     * @return 
     */
    protected abstract ELContext getELContext();
}
