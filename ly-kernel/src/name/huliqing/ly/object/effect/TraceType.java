/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.effect;

/**
 * 跟随类型
 * @author huliqing
 */
public enum TraceType {
    
    /** 不跟随 */
    no,
    
    /** 跟随一次 */
    once,
    
    /** 始终、持续跟随 */
    always;
    
    public static TraceType identity(String name) {
       for (TraceType tt : values()) {
           if (tt.name().equals(name)) {
               return tt;
           }
       }
       throw new UnsupportedOperationException("不支持的TraceType, name=" + name);
    }
}
