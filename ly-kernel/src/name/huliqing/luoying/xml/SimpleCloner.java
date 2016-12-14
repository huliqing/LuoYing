/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.xml;

import com.jme3.util.clone.Cloner;

/**
 * 原始Cloner在克隆String类型的时候会报错。
 * @author huliqing
 */
public class SimpleCloner extends Cloner {
    
    public static <T> T deepClone( T object ) {
        return new SimpleCloner().clone(object);
    }
    
    @Override
    public <T> T clone(T object, boolean useFunctions) {
        // String类型不需要克隆
        if (object instanceof String) {
            return object;
        }
        return super.clone(object, useFunctions);
    }
}
