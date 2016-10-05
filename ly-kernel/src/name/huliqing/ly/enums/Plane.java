/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.enums;

/**
 * 定义几个常用的平面
 * @author huliqing
 */
public enum Plane {
    
    xy,
    
    xz, 
    
    yz;
    
    /**
     * 通过名称来识别Plane,名称列表：xy\xz\yz, 注意大小写
     * @param name
     * @return 
     */
    public static Plane identify(String name) {
       Plane[] values = Plane.values();
       for (Plane v : values) {
           if (v.name().equals(name)) {
               return v;
           }
       }
       throw new UnsupportedOperationException("Unknow type: name=" + name);
    }
}
