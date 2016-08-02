/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.anim;

/**
 *
 * @author huliqing
 */
public enum Loop {
    
    loop,
    
    dontLoop,
    
    cycle;
    
    public static Loop identify(String name) {
       Loop[] values = Loop.values();
       for (Loop v : values) {
           if (v.name().equals(name)) {
               return v;
           }
       }
       throw new UnsupportedOperationException("Unknow type: name=" + name);
    }
}
