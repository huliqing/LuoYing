/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.enums;

/**
 * 该类主要用于指定ObjectDef中的材质类型，不同的材质在碰撞过程中发出的声音
 * 会不一样。
 * @author huliqing
 */
public enum Mat {
    
    /** 不需要材质 */
    none(0),
    
    /** 金属,金银铜铁之类 */
    metal(1),
    
    /** 木质 */
    wood(2),
    
    /** 石类，宝石，矿石之类 */
    stone(3),
    
    /** 皮肤，肌肤 */
    body(4),
    
    /** 钙质，如牙齿，爪子之类 */
    calcium(5),
    ;
    private int value;
    
    private Mat(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public static Mat identify(int value) {
        Mat[] ss = Mat.values();
        for (Mat s : ss) {
            if (s.value == value) {
                return s;
            }
        }
        throw new UnsupportedOperationException("不支持的Sex类型:" + value);
    }
    
    public static Mat identify(String value) {
       return identify(Integer.parseInt(value));
    }
}
