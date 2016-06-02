///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.enums;
//
///**
// * 各种技能对角色的影响
// * @author huliqing
// */
//public enum AffectType {
//    
//    /** 其它 */
//    other(0),
//    
//    /** 对角色生命值造成影响,比如增加或降低 */
//    health(1),
//    
//    /** 对魔法值造成影响 */
//    magic(2),
//    
//    /** 防御属性 */
//    defence(3),
//    
//    /** 移动速度 */
//    moveSpeed(4),
//    
//    ;
//    private int value;
//    
//    private AffectType(int value) {
//        this.value = value;
//    }
//    
//    public int getValue() {
//        return value;
//    }
//    
//    public static AffectType identify(String value) {
//       return identify(Integer.parseInt(value));
//    }
//    
//    public static AffectType identify(int value) {
//       AffectType[] ots = AffectType.values();
//       for (AffectType ot : ots) {
//           if (ot.getValue() == value) {
//               return ot;
//           }
//       }
//       throw new UnsupportedOperationException("不支持的AffectType类型:" + value);
//    }
//}
//
