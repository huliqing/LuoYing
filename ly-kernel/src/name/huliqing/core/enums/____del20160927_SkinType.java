///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.enums;
//
///**
// * 定义角色皮肤类型
// * @author huliqing
// */
//public enum SkinType {
//    
//    /**
//     * 人物皮肤
//     */
//    body(0), 
//    
//    /**
//     * 人物衣服或装甲
//     */
//    armor(1);
//    
//    private int value;
//    
//    private SkinType(int value) {
//        this.value = value;
//    }
//
//    public int getValue() {
//        return value;
//    }
//    
//    public static SkinType identify(String value) {
//       return identify(Integer.parseInt(value));
//    }
//    
//    public static SkinType identify(int value) {
//       SkinType[] sts = SkinType.values();
//       for (SkinType st : sts) {
//           if (st.getValue() == value) {
//               return st;
//           }
//       }
//       throw new UnsupportedOperationException("不支持的SkinType:" + value);
//    }
//}
