/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.attribute;

/**
 *
 * @author huliqing
 */
public interface LimitAttribute {
    
    /**
     * 获取最大限制值
     * @return 
     */
    float getMaxLimit();
    
    /**
     * 设置为最高值
     */
    void setMax();
    
    /**
     * 获取最小限制值
     * @return 
     */
    float getMinLimit();
    
    /**
     * 设置为最小值
     */
    void setMin();
    
    
}
