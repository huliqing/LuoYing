/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

/**
 * 属性值变化侦听器。
 * @author huliqing
 */
public interface ValueChangeListener {
    
    /**
     * 当属性值发生变化后该方法被调用。
     * @param attribute 属性
     */
    void onValueChanged(Attribute attribute);
    
}
