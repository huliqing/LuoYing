/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

/**
 * LimitAttribute主要用于定义一些可以限制值大小的属性类型。
 * @author huliqing
 * @param <T>
 */
public interface LimitAttribute<T extends Number> {
    
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
    
    /**
     * 获得当前值
     * @return 
     */
    Number getValue();
}
