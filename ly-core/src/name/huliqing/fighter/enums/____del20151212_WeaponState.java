///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.enums;
//
///**
// * 定义武器类型
// * @author huliqing
// */
//public enum WeaponState {
//
//    /**
//     * 针对一些没有实质武器的模型(如古柏、蜘蛛、狼、熊等)和一些本身已经绑定了
//     * 武器的模型（如ninja），给它们配置一把假的、空的武器。这样可以指定武器的
//     * 材质，这使得在战斗中可以根据这把武器的材质来产生不同的碰撞声音，
//     * 如ninja可以给予一把假的铁质武器，而古柏可以给予一把假的木材质武器，
//     * 这使得战斗中可以识别应该使用什么碰撞声音。甚至可以指定额外的武器伤害。
//     */
//    mock(0),
//    
//    /**
//     * 右手刀剑
//     */
//    x_sword(1),
//    
//    /**
//     * 右手匕首
//     */
//    x_knife(2),
//    
//    /**
//     * 二刀流
//     */
//    sword_sword(3),
//    
//    /**
//     * 左手弓
//     */
//    bow(4);
//    
//    
//    private int value;
//    
//    private WeaponState(int value) {
//        this.value = value;
//    }
//    
//    public int getValue() {
//        return this.value;
//    }
//    
//    public static WeaponState identify(String value) {
//       return identify(Integer.parseInt(value));
//    }
//    
//    public static WeaponState identify(int value) {
//       WeaponState[] wts = WeaponState.values();
//       for (WeaponState wt : wts) {
//           if (wt.getValue() == value) {
//               return wt;
//           }
//       }
//       throw new UnsupportedOperationException("不支持的武器类型:" + value);
//    }
//}
