///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.mvc.dao;
//
//import java.util.List;
//import name.huliqing.core.Inject;
//import name.huliqing.core.data.ActorData;
//import name.huliqing.core.data.SkinData;
//
///**
// *
// * @author huliqing
// */
//public interface SkinDao extends Inject {
//    
//    /**
//     * 获取角色包裹中的所有skin,包含装备及武器（不含基本皮肤SkinBase）.
//     * @param actorData
//     * @param store
//     * @return 
//     */
//    List<SkinData> getAll(ActorData actorData, List<SkinData> store);
//    
//    /**
//     * 获取角色包裹中所有的装备(不包含武器,不包含基本皮肤skinBase)
//     * @param actorData
//     * @param store 存放结果
//     * @return 
//     */
//    List<SkinData> getArmorSkins(ActorData actorData, List<SkinData> store);
//    
//    /**
//     * 获取角色包裹中所有的武器(不包含基本皮肤skinBase)。
//     * @param actorData
//     * @param store
//     * @return 
//     */
//    List<SkinData> getWeaponSkins(ActorData actorData, List<SkinData> store);
//    
//    /**
//     * 获取角色所有的武器，包含包裹中和基本皮肤中的武器,包含使用和未使用中的。
//     * @param actorData
//     * @param store
//     * @return 
//     */
//    List<SkinData> getWeaponSkinsAll(ActorData actorData, List<SkinData> store);
//    
//    /**
//     * 获取角色当前正在使用的所有武器,包含包裹中和基本皮肤中正在使用的武器。
//     * @param actorData
//     * @param store
//     * @return 
//     */
//    List<SkinData> getWeaponSkinsAllInUsed(ActorData actorData, List<SkinData> store);
//}
