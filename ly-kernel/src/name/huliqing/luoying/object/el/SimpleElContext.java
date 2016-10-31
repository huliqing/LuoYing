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

/**
 *
 * @author huliqing
 */
public class SimpleElContext extends ELContext {

    private final SimpleElResolver elResolver = new SimpleElResolver();
    private final SimpleFunctionMapper functionMapper = new SimpleFunctionMapper();
    private final SimpleVariableMapper variableMapper = new SimpleVariableMapper();
    
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
     * 给ELContext设置基本变量
     * @param key
     * @param value 
     */
    public void setBaseValue(String key, Object value) {
        elResolver.setBaseValue(key, value);
    }
}
