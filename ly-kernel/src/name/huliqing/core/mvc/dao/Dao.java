/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.dao;

import name.huliqing.core.Factory;

/**
 *
 * @author huliqing
 * @deprecated 
 */
public class Dao {
    
    public static <T extends Object> T get(Class<T> cla) {
        return Factory.get(cla);
    }
}
