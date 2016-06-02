/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.dao;

import java.util.HashMap;
import java.util.Map;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.Inject;

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
