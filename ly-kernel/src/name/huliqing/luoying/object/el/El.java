/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.el;

import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * @author huliqing
 * @param <T>
 */
public interface El<T> extends DataProcessor<ObjectData> {
    
    /**
     * 获取表达式字符串形式
     * @return 
     */
    String getExpression();
    
    /**
     * 设置表达式,示例如：#{x+y}
     * @param elExpression 
     */
    void setExpression(String elExpression);
    
    /**
     * 获取表达式的值。
     * @return 
     */
    T getValue();
}
