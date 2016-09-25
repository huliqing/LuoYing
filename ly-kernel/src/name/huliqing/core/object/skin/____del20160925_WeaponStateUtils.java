///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.object.skin;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import name.huliqing.core.Config;
//import name.huliqing.core.data.SkinData;
//
///**
// * 该类主要为武器的所有可能组合生成一个唯一的武器状态。主要用于在角色进行攻击时判定所使用
// * 的武器是否匹配使用的技能。武器状态表示角色所使用的武器类型，举例来说，比如
// * 把 "左手剑" + "右手剑" 定义为状态1; "左手匕首" + "右手剑"定义为状态2。
// * 那么当技能的武器状态限制为2时，即要求角色在使用该技能时必须是：左手拿匕
// * 首和右手拿剑才可以执行。
// * 注意：默认约定的生成武器状态的顺序是： 左手+右手 + 。。。
// * 暂时只支持2把武器
// * @author huliqing
// */
//public class WeaponStateUtils {
//    
//    // 定义最少可以同时配备多少把武器
//    private final static int MIN_WEAPONS = 4;
// 
//    /**
//     * 武器状态,key的格式为"weaponType1|weaponType2|..."
//     */
//    private final static Map<String, Integer> weaponStates = new HashMap<String, Integer>();
//    static {
//        // 占位,TODO:这里需要重构，开放到xml中进行配置。
//        weaponStates.put("0|0|0|0", 0); // 空手无任何武器。
//        weaponStates.put("0|1|0|0", 1); // 右手剑
//        weaponStates.put("1|1|0|0", 2); // 左右手剑
//        weaponStates.put("3|0|0|0", 3); // 左手弓
//        weaponStates.put("6|0|0|0", 4); // 左手魔杖
//        
//    }
//    
//    
//    /**
//     * 生成一个武器状态,注：weaponTypes的值入顺序是：左手，右手。
//     * 使用"0"来表示空手，示例，如果"1"表示剑，"2"表示匕首，如下代码：
//     * <code>
//     * getWeaponState(1, 2)
//     * </code>
//     * 将为左手剑 + 右手匕首生成一个武器状态，
//     * 如：
//     * <code>
//     * getWeaponState(0, 1)
//     * </code>
//     * 将为左手空 + 右手剑生成另一个武器状态。
//     * @param weaponTypes
//     * @return 
//     */
//    public static int createWeaponState(int... weaponTypes) {
//        StringBuilder sb = new StringBuilder();
//        for (int wt : weaponTypes) {
//            sb.append(wt).append("|");
//        }
//        // 如果少于必要的武器则后面的补0,比如：
//        // "0|1|0|0" 表示为右手剑生成一个武器状态，其它部位必须空，即不能拿武器
//        if (weaponTypes.length < MIN_WEAPONS) {
//            for (int i = weaponTypes.length; i < MIN_WEAPONS; i++) {
//                sb.append("0|");
//            }
//        }
//        
//        String key = sb.substring(0, sb.length() - 1);
//        Integer weaponState = weaponStates.get(key);
//        if (weaponState == null) {
//            weaponState = weaponStates.size(); // 递增生成一个int类型代表武器状态
//            weaponStates.put(key, weaponState);
//            
//            if (Config.debug) {
//                debugAllWeaponStates();
//            }
//        }
//        
//        return weaponState;
//    }
//    
//    public static void debugAllWeaponStates() {
//        Logger.getLogger(WeaponStateUtils.class.getName())
//                .log(Level.INFO, "AllWeaponStates => {0}", weaponStates.toString());
//    }
//    
//    /**
//     * 根据装备的武器列表计算一个武器状态,该方法会对列表中的武器按从：
//     * 左武器，右武器，其它武器。。进行排序，然后再计算出一个武器状态。
//     * @param weaponSkins 需要确保该列表中的skin都为武器类型,否则报错
//     * @return 
//     */
//    public static int createWeaponState(List<SkinData> weaponSkins) {
//        if (weaponSkins.isEmpty()) {
//            return createWeaponState(0); // 无装备武器
//        }
//        
//        // 注：索引0,1依次表示左武器，右武器,排在最前面，如果没有武器则默认值为0
//        int leftWeaponType = 0;
//        int rightWeaponType = 0;
//        List<Integer> otherWeapon = new ArrayList<Integer>(weaponSkins.size());
//        for (SkinData sd : weaponSkins) {
//            if (sd.getWeaponType() == 0) {
//                throw new IllegalArgumentException("Not a weapon, SkinData=" + sd.getId());
//            }
//            if ((sd.getType() & (1 << SkinConstants.TYPE_WEAPON_LEFT)) != 0) {
//                // 确定是一把左手武器
//                leftWeaponType = sd.getWeaponType();
//            } else if ((sd.getType() & (1 << SkinConstants.TYPE_WEAPON_RIGHT)) != 0) {
//                // 确定是一把右手武器
//                rightWeaponType = sd.getWeaponType();
//            } else {
//                otherWeapon.add(sd.getWeaponType());
//            }
//        }
//        
//        // 除了左右武器，其它武器按type的值依次排序。
//        if (!otherWeapon.isEmpty()) {
//            Collections.sort(otherWeapon);
//        }
//        otherWeapon.add(0,rightWeaponType);
//        otherWeapon.add(0,leftWeaponType);
//        int[] weaponTypes = new int[otherWeapon.size()];
//        for (int i = 0; i < weaponTypes.length; i++) {
//            weaponTypes[i] = otherWeapon.get(i);
//        }
//        int weaponState = createWeaponState(weaponTypes);
//        return weaponState;
//    }
//    
//}
