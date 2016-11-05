/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.el;

import javax.el.ELContext;

/**
 * 自定义的表达式,返回类型根据表达式实际情况而定，类型为Object.
 * 需要设置表达式及根据表达式设置参数值后才可以计算值。
 * @author huliqing
 * @param <T>
 */
public class CustomEl<T> extends AbstractEl<T> {

    private final SimpleElContext elContext = new SimpleElContext();
    
    @Override
    protected ELContext getELContext() {
        return elContext;
    }
    
    @Override
    public T getValue() {
        return super.getValue(); 
    }
    
    /**
     * 设置基本参数.
     * @param prop 参数名称
     * @param value 参数值
     */
    public void setProperty(String prop, Object value) {
        elContext.setBaseValue(prop, value);
    }
}
