///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.object.module;
//
//import com.jme3.util.SafeArrayList;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import name.huliqing.core.Factory;
//import name.huliqing.core.data.SkinData;
//import name.huliqing.core.data.ModuleData;
//import name.huliqing.core.object.Loader;
//import name.huliqing.core.mvc.service.AttributeService;
//import name.huliqing.core.object.actor.Actor;
//import name.huliqing.core.object.attribute.CollectionAttribute;
//import name.huliqing.core.object.skin.Skin;
//import name.huliqing.core.object.skin.WeaponSkin;
//import name.huliqing.core.object.skin.WeaponStateUtils;
//import name.huliqing.core.xml.DataFactory;
//
///**
// * 角色的换装控制器
// * @author huliqing
// */
//public class SkinModule extends AbstractModule {
//    private final AttributeService attributeService = Factory.get(AttributeService.class);
//    
//    private Actor actor;
//    // 监听角色装备、武器等的穿脱
//    private List<SkinListener> skinListeners;
//    
//    // 缓存当前正在使用的武器和武器状态, 武器状态用来标识不同的武器组合，不同组合的武器或者不同排列顺序的武器都会产生
//    // 唯一的武器状态码。
//    private List<SkinData> cacheWeaponInUsed;
//    private int cacheWeaponState = -1;
//    
//    // 角色所有的皮肤，包含装备、武器（含outifs,weapons）
//    private List<Skin> skins;
//    // 当前穿在身上的装备
//    private List<Skin> outfits;
//    // 当前穿在身上的武器
//    private List<Skin> weapons;
//    
//    // 绑定一个用于存放武器槽位的属性，必须是CollectionAttribute,这个属性中存放着角色优先支持的武器槽位列表（id)
//    private String bindWeaponSlotAttribute;
//    
//    // 武器槽位属性
//    private CollectionAttribute<String> weaponSlotAttribute;
//
//    @Override
//    public void setData(ModuleData data) {
//        super.setData(data);
//        bindWeaponSlotAttribute = data.getAsString("bindWeaponSlotAttribute");
//    }
//    
//    @Override
//    public void initialize(Actor actor) {
//        super.initialize(actor); 
//        this.actor = actor;
//        
//        if (bindWeaponSlotAttribute != null) {
//            weaponSlotAttribute = attributeService.getAttributeByName(actor, bindWeaponSlotAttribute);
//        }
//        
//        // 穿上普通装备
//        List<SkinData> skinDatas =  actor.getData().getObjectDatas(SkinData.class, null);
//        if (skinDatas != null && !skinDatas.isEmpty()) {
//            for (SkinData sd : skinDatas) {
//                if (sd.isUsed() && !sd.isBaseSkin()) {
//                    attachSkin(sd);
//                }
//            }
//        }
//    }
//    
//    /**
//     * 添加装备到角色包裹.
//     * @param skinId
//     * @param amount 数量，必须大于0
//     */
//    public void addSkin(String skinId, int amount) {
//        if (amount <= 0)
//            return;
//
//        SkinData skinData = getSkinData(skinId);
//        if (skinData != null) {
//            skinData.setTotal(skinData.getTotal() + amount);
//        } else {
//            skinData = DataFactory.createData(skinId);
//            skinData.setTotal(amount);
//            actor.getData().addObjectData(skinData);
//        }
//        
//        if (skinListeners != null) {
//            for (int i = 0; i < skinListeners.size(); i++) {
//                skinListeners.get(i).onSkinAdded(actor, skinData);
//            }
//        }
//    }
//    
//    /**
//     * 从角色包裹上移除装备,
//     * @param skinId
//     * @param amount
//     * @return  
//     * @see #getSkinData(java.lang.String) 
//     */
//    public boolean removeSkin(String skinId, int amount) {
//        SkinData skinData = getSkinData(skinId);
//        if (skinData == null) 
//            return false;
//        
//        skinData.setTotal(skinData.getTotal() - amount);
//        if (skinData.getTotal() <= 0) {
//            actor.getData().removeObjectData(skinData);
//        }
//        
//        if (skinListeners != null) {
//            for (int i = 0; i < skinListeners.size(); i++) {
//                skinListeners.get(i).onSkinRemoved(actor, skinData);
//            }
//        }
//        
//        return true;
//    }
//    
//    /**
//     * 从角色包裹中查找指定ID的SkinData
//     * @param skinId
//     * @return 
//     */
//    public Skin getSkinData(String skinId) {
//        if (skins == null)
//            return null;
//        for (Skin s : skins) {
//            if (s.getData().getId().equals(skinId)) {
//                return s;
//            }
//        }
//    }
//    
//    /**
//     * 给角色换上装备,注：换装备的时候需要考虑冲突的装备，并把冲突的装备换
//     * 下来
//     * @param skinData
//     */
//    public void attachSkin(Skin skin) {
//        
//        // 2.==== 换上装备
//        // 为武器选择槽位(如果是武器）
//        selectWeaponSlot(actor, skinData);
//        
//        // 装备模型
//        Skin skin = Loader.loadSkin(skinData);
//        skin.attach(actor, this, isWeaponTakeOn());
//
//        //TODO:REMOVE
//        cacheWeaponsAndState();
//        
//        // 触发侦听器(穿)
//        if (skinListeners != null) {
//            for (SkinListener sl : skinListeners) {
//                sl.onSkinAttached(actor, skin);
//            }
//        }
//        
//    }
//    
//    public void detachSkin(SkinData skinData) {
//        
//        Skin skin = Loader.loadSkin(skinData);
//        skin.detach(actor, this);
//        
//        //TODO:REMOVE
//        cacheWeaponsAndState();
//        
//        // 触发侦听器
//        if (skinListeners != null) {
//            for (SkinListener sl : skinListeners) {
//                sl.onSkinDetached(actor, skin);
//            }
//        }
//    }
//    
//    /**
//     * 判断一件装备是否为武器类型
//     * @param skinData
//     * @return 
//     */
//    public boolean isWeapon(SkinData skinData) {
//        return skinData.isWeapon();
//    }
//    
//    /**
//     * 判断一件装备是否为普通装备（非武器），与isWeapon对应
//     * @param skinData
//     * @return 
//     */
//    public boolean isArmor(SkinData skinData) {
//        return !skinData.isWeapon();
//    }
//    
//    public boolean takeOnWeapon(boolean force) {
//        setWeaponTakeOn(true);
//        
//        List<SkinData> weaponSkins = getCurrentWeaponSkin();
//        // 可用的要优先选择的槽位
//        SafeArrayList<String> slotCandidate = null;
//        if (weaponSlotAttribute != null && !weaponSlotAttribute.isEmpty()) {
//            slotCandidate = new SafeArrayList<String>(String.class, weaponSlotAttribute.values());
//        }
//        for (SkinData sd : weaponSkins) {
//            Skin skin = Loader.loadSkin(sd);
//            if (skin instanceof WeaponSkin) {
//                // 给武器查找一个合适的槽位
//                selectSlots(slotCandidate, sd);
//                
//                // 装备上武器
//                ((WeaponSkin) skin).takeOn(actor, force, isWeaponTakeOn());
//                
//                // 标记武器为using
//                sd.setUsed(true);
//            }
//        }
//        return true;
//    }
//    
//    public boolean takeOffWeapon(boolean force) {
//        setWeaponTakeOn(false);
//        
//        List<SkinData> weaponSkins = getCurrentWeaponSkin();
//        // 可用的要优先选择的槽位
//        SafeArrayList<String> slotCandidate = null;
//        if (weaponSlotAttribute != null && !weaponSlotAttribute.isEmpty()) {
//            slotCandidate = new SafeArrayList<String>(String.class, weaponSlotAttribute.values());
//        }
//        for (SkinData sd : weaponSkins) {
//            Skin skin = Loader.loadSkin(sd);
//            if (skin instanceof WeaponSkin) {
//                // 给武器查找一个合适的槽位
//                selectSlots(slotCandidate, sd);
//                
//                // 取下武器
//                ((WeaponSkin) skin).takeOff(actor, force, isWeaponTakeOn());
//            }
//        }
//        return true;
//    }
//    
//    /**
//     * 获取角色的基本皮肤
//     * @return 
//     */
//    public List<SkinData> getBaseSkins() {
//        List<SkinData> store = actor.getData().getObjectDatas(SkinData.class, null);
//        Iterator<SkinData> it = store.iterator();
//        while (it.hasNext()) {
//            if (!it.next().isBaseSkin()) {
//                it.remove();
//            }
//        }
//        return store;
//    }
//    
//    /**
//     * 获取当前正在使用的武器列表。
//     * @return 
//     */
//    public List<SkinData> getCurrentWeaponSkin() {
//        // 先从缓存中获取，如果没有就重新生成。
//        if (cacheWeaponInUsed == null) {
//            // 缓存武器状态及武器列表信息
//            cacheWeaponsAndState();
//        }
//        return cacheWeaponInUsed;
//    }
//     
//    /**
//     * 获取角色当前包裹中的武器,"不包含"基本皮肤中的武器
//     * @param store
//     * @return 
//     * @see #getWeaponSkinsAll(java.util.List) 
//     */
//    public List<SkinData> getWeaponSkins(List<SkinData> store) {
//        List<SkinData> skinDatas = actor.getData().getObjectDatas(SkinData.class, null);
//        if (skinDatas == null)
//            return store;
//        
//        if (store == null) {
//            store = new ArrayList<SkinData>();
//        }
//        for (SkinData sd : skinDatas) {
//            if (isWeapon(sd)) {
//                store.add(sd);
//            }
//        }
//        return store;
//    }
//    
//    public List<SkinData> getArmorSkins(List<SkinData> store) {
//        List<SkinData> skinDatas = actor.getData().getObjectDatas(SkinData.class, null);
//        if (skinDatas == null)
//            return store;
//        
//        if (store == null) {
//            store = new ArrayList<SkinData>();
//        }
//        for (SkinData sd : skinDatas) {
//            if (isArmor(sd)) {
//                store.add(sd);
//            }
//        }
//        return store;
//    }
//    
//    /**
//     * 获取角色当前正在使用的武器状态。
//     * @return 
//     */
//    public int getWeaponState() {
//        if (cacheWeaponState <= -1) {
//            cacheWeaponsAndState();
//        }
//        return cacheWeaponState;
//    }
//    
//    public void addSkinListener(SkinListener skinListener) {
//        if (skinListeners == null) {
//            skinListeners = new ArrayList<SkinListener>();
//        }
//        if (!skinListeners.contains(skinListener)) {
//            skinListeners.add(skinListener);
//        }
//    }
//
//    public boolean removeSkinListener(SkinListener skinListener) {
//        return skinListeners != null && skinListeners.remove(skinListener);
//    }
//
//    public List<SkinListener> getSkinListeners() {
//        return skinListeners;
//    }
//    
//    /**
//     * 标记武器是处于takeOn或takeOff状态,即取出或挂起的状态。
//     * 处于使用中(isUsing=true)的武器存在两种状态，“挂起（待机状态）”，“取出（或者叫战斗状态）”
//     * @param takeOn 
//     */
//    public void setWeaponTakeOn(boolean takeOn) {
//        data.setAttribute("weaponTakeOn", takeOn);
//    }
//    
//    /**
//     * 判断当前武器是“取出”的还是“挂起”的
//     * @return 
//     */
//    public boolean isWeaponTakeOn() {
//        return data.getAsBoolean("weaponTakeOn", false);
//    }
//
//    // --------------------------------------------------------------------------------------------------------------------------------
//    
//    /**
//     * 获取角色当前正在使用中的所有武器
//     * @param store
//     * @return 
//     */
//    private List<SkinData> getWeaponSkinsAllInUsed(List<SkinData> store) {
//        if (store == null) {
//            store = new ArrayList<SkinData>(2);
//        }
//        List<SkinData> allWeapon = getWeaponSkins(null);
//        if (allWeapon != null) {
//            for (SkinData sd : allWeapon) {
//                if (sd.isUsed()) {
//                    store.add(sd);
//                }
//            }
//        }
//        return store;
//    }
//    
//    /**
//     * 重新计算并缓存角色的武器状态和当前武器列表，该方法只有在角色切换装备 或者未初始化武器状态时调用，
//     * 因为比较耗性能，在战斗过程中可能需要频繁 调用查询角色的当前武器状态及武器信息，因此把这两个信息缓存起来。
//     * @param actor
//     */
//    private void cacheWeaponsAndState() {
//        if (cacheWeaponInUsed == null) {
//            cacheWeaponInUsed = new ArrayList<SkinData>(2);
//        }
//        // 正在使用的武器列表
//        cacheWeaponInUsed.clear();
//        cacheWeaponInUsed = getWeaponSkinsAllInUsed(cacheWeaponInUsed);
//
//        // 生成武器状态信息
//        cacheWeaponState = WeaponStateUtils.createWeaponState(cacheWeaponInUsed);
//    }
//    
//   /**
//     * 为角色指定的武器选择一个合适的槽位来装备武器
//     * @param actor
//     * @param skinData 
//     */
//    private void selectWeaponSlot(Actor actor, SkinData skinData) {
//        // 如果不是武器类型，则不需要判断是否有可用槽位
//        if (!isWeapon(skinData)) {
//            return;
//        }
//        
//        // 找出角色当前可用的用于存放武器的槽位
//        // 逻辑：从角色配置中的所有可用槽位中进行选择，需要移除当前正在使用中的武器的槽位.
//        SafeArrayList<String> slotCandidate = null;
//        if (weaponSlotAttribute != null && !weaponSlotAttribute.isEmpty()) {
//            slotCandidate = new SafeArrayList<String>(String.class, weaponSlotAttribute.values());
//            // 获取正在使用中的武器，以便移除这些槽位
//            List<SkinData> weaponSkins = getWeaponSkinsAllInUsed(null);
//            if (weaponSkins != null) {
//                for (SkinData sd : weaponSkins) {
//                    if (sd.getSlot() != null) {
//                        slotCandidate.remove(sd.getSlot());
//                    }
//                }
//            }
//        }
//        
//        // 给武器查找一个合适的槽位
//        selectSlots(slotCandidate, skinData);
//    }
//    
//    private void selectSlots(SafeArrayList<String> slotCandidate, SkinData sd) {
//        // 如果武器不支持任何槽
//        if (sd.getSlots() == null || sd.getSlots().isEmpty()) {
//            sd.setSlot(null);
//            return;
//        }
//        
//        // 如果没有任何可用槽位
//        if (slotCandidate == null || slotCandidate.isEmpty()) {
//            sd.setSlot(null);
//            return;
//        }
//        
//        // 从优先的候选列表中查找一个该武器可支持的槽位使用
//        for (String slotC : slotCandidate.getArray()) {
//            if (sd.getSlots().contains(slotC)) {
//                sd.setSlot(slotC);
//                slotCandidate.remove(slotC);
//                return;
//            }
//        }
//
//        sd.setSlot(null);
//    }
//    
////    /**
////     * 查找当前角色装备的所有skinType
////     */
////    private class FullSkinTypesTraversal implements SceneGraphVisitor {
////        public int fullSkinTypes;
////        @Override
////        public void visit(Spatial actorModel) {
////            ObjectData pd = actorModel.getUserData(ObjectData.USER_DATA);
////            if (pd != null && (pd instanceof SkinData)) {
////                SkinData sd = (SkinData) pd;
////                fullSkinTypes |= sd.getType();
////            }
////        }
////    }
//
//}
