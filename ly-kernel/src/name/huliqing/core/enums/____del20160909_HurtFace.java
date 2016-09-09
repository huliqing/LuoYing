///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.enums;
//
///**
// * 受攻击方向
// * @author huliqing
// */
//public enum HurtFace {
//    
//    front,
//    
//    back,
//    
//    left,
//    
//    right;
//    
//    /**
//     * 通过名称来识别Face: front/back/left/right
//     * @param name
//     * @return 
//     */
//    public static HurtFace identify(String name) {
//       HurtFace[] values = HurtFace.values();
//       for (HurtFace v : values) {
//           if (v.name().equals(name)) {
//               return v;
//           }
//       }
//       throw new UnsupportedOperationException("Unknow type: name=" + name);
//    }
//}
