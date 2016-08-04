/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import com.jme3.network.serializing.Serializable;

/**
 * 等级计算公式
 * @author huliqing
 */
@Serializable
public class ElData extends ObjectData {
    
    // 计算公式
    private String expression;
    
    public ElData(){}

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

}
