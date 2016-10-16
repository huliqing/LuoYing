/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

/**
 *
 * @author huliqing
 * @param <T>
 */
public interface ValueChangeListener<T> {
    
    /**
     * 当属性值发生变化后该方法被调用。
     * @param attribute 属性
     * @param oldValue 旧的属性值
     * @param newValue 新的属性值
     */
    void onValueChanged(Attribute attribute, T oldValue, T newValue);
    
}
