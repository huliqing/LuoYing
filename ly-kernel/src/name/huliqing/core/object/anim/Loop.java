/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.anim;

/**
 * @author huliqing
 */
public enum Loop {

    /** 只执行一次，模式：A -> B */
    dontLoop,
    
    /** 始终循环,循环模式：A -> B, A -> B, A -> B */
    loop,
    
    /** 周期性循环，循环模式： A -> B -> A -> B -> ... */
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
