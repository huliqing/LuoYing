/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.enums;

/**
 * 跟随的位置类型
 * @author huliqing
 */
public enum TracePositionType {
    
    /** 以原点作为跟随位置(默认方式) */
    origin,
    
    /** 以原点但是y值使用的是包围盒中心点处的值作为跟随位置。 */
    origin_bound_center,

    /** 以原点但是y值使用的是包围盒顶部最高位置点处的y值作为跟随位置 */
    origin_bound_top,
    
    // 暂不支持bound_bottom，好像没有意义
//    /** 以包含围盒的“底部”中心点作为跟随位置 */
//    bound_bottom,

    /** 以包围盒的“中心点”作为跟随位置 */
    bound_center,

    /** 以包围盒的“顶部”中心点作为跟随位置  */
    bound_top;

    public static TracePositionType identify(String name) {
        TracePositionType[] types = values();
        for (TracePositionType type : types) {
            if (type.name().equals(name)) {
                return type;
            }
        }
        throw new UnsupportedOperationException("Unknow TraceOffsetType:" + name);
    }
}
