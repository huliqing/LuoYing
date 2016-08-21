/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.data.AttributeApply;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.data.module.SkinModuleData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.SkinListener;
import name.huliqing.core.object.skin.Skin;
import name.huliqing.core.object.skin.WeaponSkin;
import name.huliqing.core.object.skin.WeaponStateUtils;
import name.huliqing.core.xml.DataFactory;

/**
 * 角色的换装控制器
 * @author huliqing
 * @param <T>
 */
public class SkinModule<T extends SkinModuleData> extends AbstractModule<T> {
    private final AttributeService attributeService = Factory.get(AttributeService.class);

    private Actor actor;
    
    // 监听角色装备、武器等的穿脱
    private List<SkinListener> skinListeners;
    
    // 缓存当前正在使用的武器和武器状态, 武器状态用来标识不同的武器组合，不同组合的武器或者不同排列顺序的武器都会产生
    // 唯一的武器状态码。
    private List<SkinData> cacheWeaponInUsed;
    private int cacheWeaponState = -1;
    
    @Override
    public void initialize(Actor actor) {
        super.initialize(actor); 
        this.actor = actor;
        
        // 穿上普通装备
        List<SkinData> skinDatas =  actor.getData().getObjectDatas(SkinData.class, null);
        if (skinDatas != null && !skinDatas.isEmpty()) {
            for (SkinData sd : skinDatas) {
                if (sd.isUsing() && !sd.isBaseSkin()) {
                    attachSkin(sd);
                }
            }
        }
        
        // 再补上基本皮肤
        fixActorSkinBase();
    }
    
    /**
     * 添加装备到角色包裹.
     * @param skinId
     * @param amount 数量，必须大于0
     */
    public void addSkin(String skinId, int amount) {
        if (amount <= 0)
            return;

        SkinData skinData = getSkinData(skinId);
        if (skinData != null) {
            skinData.setTotal(skinData.getTotal() + amount);
        } else {
            skinData = DataFactory.createData(skinId);
            skinData.setTotal(amount);
            actor.getData().addObjectData(skinData);
        }
        
        if (skinListeners != null) {
            for (int i = 0; i < skinListeners.size(); i++) {
                skinListeners.get(i).onSkinAdded(actor, skinData);
            }
        }
    }
    
    /**
     * 从角色包裹上移除装备,
     * @param skinId
     * @param amount
     * @return  
     * @see #getSkinData(java.lang.String) 
     */
    public boolean removeSkin(String skinId, int amount) {
        SkinData skinData = getSkinData(skinId);
        if (skinData == null) 
            return false;
        
        skinData.setTotal(skinData.getTotal() - amount);
        if (skinData.getTotal() <= 0) {
            actor.getData().removeObjectData(skinData);
        }
        
        if (skinListeners != null) {
            for (int i = 0; i < skinListeners.size(); i++) {
                skinListeners.get(i).onSkinRemoved(actor, skinData);
            }
        }
        
        return true;
    }
    
    /**
     * 从角色包裹中查找指定ID的SkinData
     * @param skinId
     * @return 
     */
    public SkinData getSkinData(String skinId) {
        return actor.getData().getObjectData(skinId);
    }
    
    /**
     * 给角色换上装备,注：换装备的时候需要考虑冲突的装备，并把冲突的装备换
     * 下来
     * @param skinData
     */
    public void attachSkin(SkinData skinData) {
        // 装备已经存在于身上
        if (isSkinAttached(actor.getSpatial(), skinData)) {
            return;
        }
        
        // 1.====脱下排斥的装备
        int conflict = skinData.getType();
        conflict |= skinData.getConflictType();
        List<SkinData> conflictSkins = findSkinByType(actor.getSpatial(), conflict);
        if (!conflictSkins.isEmpty()) {
            for (SkinData sd : conflictSkins) {
                // 移除装备效果
                removeSkinApplyAttributes(actor, sd);
                // 装备御下
                Skin skin = Loader.loadSkin(sd);
                skin.detach(actor);
                // 触发侦听器(脱)
                if (skinListeners != null) {
                    for (SkinListener sl : skinListeners) {
                        sl.onSkinDetached(actor, skin);
                    }
                }
            }
        }
        
        // 2.==== 换上装备
        // 为武器选择槽位(如果是武器）
        selectWeaponSlot(actor, skinData);
        // 装备模型
        Skin skin = Loader.loadSkin(skinData);
        skin.attach(actor, isWeaponTakeOn());
        // 装备效果
        addSkinApplyAttributes(actor, skinData);
        // 触发侦听器(穿)
        if (skinListeners != null) {
            for (SkinListener sl : skinListeners) {
                sl.onSkinAttached(actor, skin);
            }
        }
        
        // 3.====检查并补全基本装备
        // 注：因为基本皮肤为固有皮肤，不会在装备栏上显示，所以不触发skinListener的侦听。
        fixActorSkinBase();
        
        // 4.====重新缓存武器状态及武器列表信息
        cacheWeaponsAndState();
    }
    
    public void detachSkin(SkinData skinData) {
        // 如果模型没有在使用中，则直接返回
        if (!skinData.isUsing()) {
            return;
        }
        
        // 1.====脱装备
        // 移除装备效果
        removeSkinApplyAttributes(actor, skinData);
        // 装备模型移除
        Skin skin = Loader.loadSkin(skinData);
        skin.detach(actor);
        // 触发侦听器
        if (skinListeners != null) {
            for (SkinListener sl : skinListeners) {
                sl.onSkinDetached(actor, skin);
            }
        }
        
        // 2.====补上基本皮肤,同attach一样,不需要触发skinListener侦听。
        fixActorSkinBase();
        
        // 3.====重新缓存武器状态及武器列表信息
        cacheWeaponsAndState();
    }
    
    /**
     * 判断一件装备是否为武器类型
     * @param skinData
     * @return 
     */
    public boolean isWeapon(SkinData skinData) {
        return skinData.isWeapon();
    }
    
    /**
     * 判断一件装备是否为普通装备（非武器），与isWeapon对应
     * @param skinData
     * @return 
     */
    public boolean isArmor(SkinData skinData) {
        return !skinData.isWeapon();
    }
    
    public boolean takeOnWeapon(boolean force) {
//        if (!force && !isCanTakeOnWeapon(actor)) {
//            return false;
//        }

        setWeaponTakeOn(true);
        
        List<SkinData> weaponSkins = getCurrentWeaponSkin();
        // 可用的要优先选择的槽位
        SafeArrayList<String> slotCandidate = null;
        if (actor.getData().getSlots() != null) {
            slotCandidate = new SafeArrayList<String>(String.class, actor.getData().getSlots());
        }
        for (SkinData sd : weaponSkins) {
            Skin skin = Loader.loadSkin(sd);
            if (skin instanceof WeaponSkin) {
                // 给武器查找一个合适的槽位
                selectSlots(slotCandidate, sd);
                
                // 装备上武器
                ((WeaponSkin) skin).takeOn(actor, force, isWeaponTakeOn());
                
                // 标记武器为using
                sd.setUsing(true);
            }
        }
        return true;
    }
    
    public boolean takeOffWeapon(boolean force) {
        // remove20160821
//        if (!force && !isCanTakeOffWeapon(actor)) {
//            return false;
//        }

        setWeaponTakeOn(false);
        
        List<SkinData> weaponSkins = getCurrentWeaponSkin();
        // 可用的要优先选择的槽位
        SafeArrayList<String> slotCandidate = null;
        if (actor.getData().getSlots() != null) {
            slotCandidate = new SafeArrayList<String>(String.class, actor.getData().getSlots());
        }
        for (SkinData sd : weaponSkins) {
            Skin skin = Loader.loadSkin(sd);
            if (skin instanceof WeaponSkin) {
                // 给武器查找一个合适的槽位
                selectSlots(slotCandidate, sd);
                
                // 取下武器
                ((WeaponSkin) skin).takeOff(actor, force, isWeaponTakeOn());
            }
        }
        return true;
    }
    
    /**
     * 获取当前正在使用的武器列表。
     * @return 
     */
    public List<SkinData> getCurrentWeaponSkin() {
        // 先从缓存中获取，如果没有就重新生成。
        if (cacheWeaponInUsed == null) {
            // 缓存武器状态及武器列表信息
            cacheWeaponsAndState();
        }
        return cacheWeaponInUsed;
    }
     
    /**
     * 获取角色当前包裹中的武器,"不包含"基本皮肤中的武器
     * @param store
     * @return 
     * @see #getWeaponSkinsAll(java.util.List) 
     */
    public List<SkinData> getWeaponSkins(List<SkinData> store) {
        List<SkinData> skinDatas = actor.getData().getObjectDatas(SkinData.class, null);
        if (skinDatas == null)
            return store;
        
        if (store == null) {
            store = new ArrayList<SkinData>();
        }
        for (SkinData sd : skinDatas) {
            if (isWeapon(sd)) {
                store.add(sd);
            }
        }
        return store;
    }
    
    public List<SkinData> getArmorSkins(List<SkinData> store) {
        List<SkinData> skinDatas = actor.getData().getObjectDatas(SkinData.class, null);
        if (skinDatas == null)
            return store;
        
        if (store == null) {
            store = new ArrayList<SkinData>();
        }
        for (SkinData sd : skinDatas) {
            if (isArmor(sd)) {
                store.add(sd);
            }
        }
        return store;
    }
    
    /**
     * 获取角色当前正在使用的武器状态。
     * @return 
     */
    public int getWeaponState() {
        if (cacheWeaponState <= -1) {
            cacheWeaponsAndState();
        }
        return cacheWeaponState;
    }
    
    public void addSkinListener(SkinListener skinListener) {
        if (skinListeners == null) {
            skinListeners = new ArrayList<SkinListener>();
        }
        if (!skinListeners.contains(skinListener)) {
            skinListeners.add(skinListener);
        }
    }

    public boolean removeSkinListener(SkinListener skinListener) {
        return skinListeners != null && skinListeners.remove(skinListener);
    }

    public List<SkinListener> getSkinListeners() {
        return skinListeners;
    }
   
    // remove20160821
//    /**
//     * 获取角色当前包裹中的所有武器，"包含"基本皮肤中的武器
//     * @param store
//     * @return 
//     */
//    public List<SkinData> getWeaponSkinsAll(List<SkinData> store) {
//        // 1.包裹中的武器
//        store = getWeaponSkins(store);
//        
//        // 2.加入基本皮肤中的武器
//        if (data.getSkinBase() != null && !data.getSkinBase().isEmpty()) {
//            for (SkinData sd : data.getSkinBase()) {
//                if (isWeapon(sd)) {
//                    store.add(sd);
//                }
//            }
//        }
//        return store;
//    }
    
    /**
     * 标记武器是处于takeOn或takeOff状态,即取出或挂起的状态。
     * 处于使用中(isUsing=true)的武器存在两种状态，“挂起（待机状态）”，“取出（或者叫战斗状态）”
     * @param takeOn 
     */
    public void setWeaponTakeOn(boolean takeOn) {
        data.setAttribute("weaponTakeOn", takeOn);
    }
    
    /**
     * 判断当前武器是“取出”的还是“挂起”的
     * @return 
     */
    public boolean isWeaponTakeOn() {
        return data.getAsBoolean("weaponTakeOn", false);
    }

    // --------------------------------------------------------------------------------------------------------------------------------
    
    /**
     * 获取角色当前正在使用中的所有武器
     * @param store
     * @return 
     */
    private List<SkinData> getWeaponSkinsAllInUsed(List<SkinData> store) {
        if (store == null) {
            store = new ArrayList<SkinData>(2);
        }
        List<SkinData> allWeapon = getWeaponSkins(null);
        if (allWeapon != null) {
            for (SkinData sd : allWeapon) {
                if (sd.isUsing()) {
                    store.add(sd);
                }
            }
        }
        return store;
    }
    
    /**
     * 重新计算并缓存角色的武器状态和当前武器列表，该方法只有在角色切换装备 或者未初始化武器状态时调用，
     * 因为比较耗性能，在战斗过程中可能需要频繁 调用查询角色的当前武器状态及武器信息，因此把这两个信息缓存起来。
     * @param actor
     */
    private void cacheWeaponsAndState() {
        if (cacheWeaponInUsed == null) {
            cacheWeaponInUsed = new ArrayList<SkinData>(2);
        }
        // 正在使用的武器列表
        cacheWeaponInUsed = getWeaponSkinsAllInUsed(cacheWeaponInUsed);

        // 生成武器状态信息
        cacheWeaponState = WeaponStateUtils.createWeaponState(cacheWeaponInUsed);
    }
    
    /**
     * 部分装备换装后需要补上缺失的基本皮肤,角色必须全部拥有基本皮肤所指定的
     * skinType，如果缺失则应该从基本皮肤上取出来补上。
     * @param actor 
     */
    private void fixActorSkinBase() {
        // 获取当前角色已经装备的所有skinTypes
        FullSkinTypesTraversal traversal = new FullSkinTypesTraversal(); 
        actor.getSpatial().breadthFirstTraversal(traversal);
        int actorSkinTypes = traversal.fullSkinTypes;

        List<SkinData> skinDatas = actor.getData().getObjectDatas(SkinData.class, null);
        
        if (skinDatas != null && !skinDatas.isEmpty()) {
            for (SkinData sd : skinDatas) {
                if (!sd.isBaseSkin()) {
                    continue;
                }
                if ((actorSkinTypes & sd.getType()) == 0) {
                    // 如果是武器则尝试找一个优先的武器槽位
                    selectWeaponSlot(actor, sd); 
                    Loader.loadSkin(sd).attach(actor, isWeaponTakeOn());
                    addSkinApplyAttributes(actor, sd);
                    sd.setUsing(true);
                }
            }
        }
    }
    
     /**
     * 为角色指定的武器选择一个合适的槽位来装备武器
     * @param actor
     * @param skinData 
     */
    private void selectWeaponSlot(Actor actor, SkinData skinData) {
        // 如果不是武器类型，则不需要判断是否有可用槽位
        if (!isWeapon(skinData)) {
            return;
        }
        
        // 找出角色当前可用的用于存放武器的槽位
        // 逻辑：从角色配置中的所有可用槽位中进行选择，需要移除当前正在使用中的武器的槽位.
        SafeArrayList<String> slotCandidate = null;
        if (actor.getData().getSlots() != null) {
            slotCandidate = new SafeArrayList<String>(String.class, actor.getData().getSlots());
            // 获取正在使用中的武器，以便移除这些槽位
            List<SkinData> weaponSkins = getWeaponSkinsAllInUsed(null);
            if (weaponSkins != null) {
                for (SkinData sd : weaponSkins) {
                    if (sd.getSlot() != null) {
                        slotCandidate.remove(sd.getSlot());
                    }
                }
            }
        }
        
        // 给武器查找一个合适的槽位
        selectSlots(slotCandidate, skinData);
    }
    
    private void selectSlots(SafeArrayList<String> slotCandidate, SkinData sd) {
        // 如果武器不支持任何槽
        if (sd.getSlots() == null || sd.getSlots().isEmpty()) {
            sd.setSlot(null);
            return;
        }
        
        // 如果没有任何可用槽位
        if (slotCandidate == null || slotCandidate.isEmpty()) {
            sd.setSlot(null);
            return;
        }
        
        // 从优先的候选列表中查找一个该武器可支持的槽位使用
        for (String slotC : slotCandidate.getArray()) {
            if (sd.getSlots().contains(slotC)) {
                sd.setSlot(slotC);
                slotCandidate.remove(slotC);
                return;
            }
        }

        sd.setSlot(null);
    }
    
    /**
     * 添加装备效果到角色身上
     * @param actor
     * @param skinData 
     */
    private void addSkinApplyAttributes(Actor actor, SkinData skinData) {
        List<AttributeApply> aas = skinData.getApplyAttributes();
        if (aas != null) {
            for (AttributeApply aa : aas) {
                attributeService.applyDynamicValue(actor, aa.getAttribute(), aa.getAmount());
                attributeService.applyStaticValue(actor, aa.getAttribute(), aa.getAmount());
                attributeService.clampDynamicValue(actor, aa.getAttribute());
            }
        }
    }
    
    /**
     * 移除指定角色身上某个装备的效果
     * @param actor
     * @param skinData 
     */
    private void removeSkinApplyAttributes(Actor actor, SkinData skinData) {
        List<AttributeApply> aas = skinData.getApplyAttributes();
        if (aas != null) {
            for (AttributeApply aa : aas) {
                attributeService.applyDynamicValue(actor, aa.getAttribute(), -aa.getAmount());
                attributeService.applyStaticValue(actor, aa.getAttribute(), -aa.getAmount());
                attributeService.clampDynamicValue(actor, aa.getAttribute());
            }
        }
    }
    
    /**
     * 通过skinType找出所有skinNode
     * @param actorModel
     * @param skinTypes 二制位表示的skinTypes
     * @return 
     */
    private List<SkinData> findSkinByType(Spatial actorModel, final int skinTypes) {
        final List<SkinData> results = new ArrayList<SkinData>(2); 
        SceneGraphVisitor sgv = new SceneGraphVisitor() {
            @Override
            public void visit(Spatial spatial) {
                ObjectData pd = spatial.getUserData(ObjectData.USER_DATA);
                if (pd != null && (pd instanceof SkinData)) {
                    SkinData sd = (SkinData) pd;
                    if (((sd.getType() | sd.getConflictType()) & skinTypes) != 0) {
                        if (!results.contains(sd)) {
                            results.add(sd);
                        }
                    }
                }
            }
        };
        // 根据actorModel的结构，在这里使用广度优先查询的速度会快一些。
        actorModel.breadthFirstTraversal(sgv);
        return results;
    }
    
    /**
     * 查找当前角色装备的所有skinType
     */
    private class FullSkinTypesTraversal implements SceneGraphVisitor {
        public int fullSkinTypes;
        @Override
        public void visit(Spatial actorModel) {
            ObjectData pd = actorModel.getUserData(ObjectData.USER_DATA);
            if (pd != null && (pd instanceof SkinData)) {
                SkinData sd = (SkinData) pd;
                fullSkinTypes |= sd.getType();
            }
        }
    }
    
    /**
     * 检查装备模型是否已经穿在身上。
     * @param actorModel
     * @param skinData
     * @return 
     */
    private boolean isSkinAttached(Spatial actorModel, SkinData skinData) {
        SkinFinder finder = new SkinFinder(skinData);
        actorModel.breadthFirstTraversal(finder);
        return finder.getResult() != null;
    }
    
    /**
     * 用于协助从actorModel中获取指定skinData的模型。
     */
    private class SkinFinder implements SceneGraphVisitor {

        private Spatial skinNode;
        private final SkinData targetSkinData;
        
        public SkinFinder(SkinData targetSkinData) {
            this.targetSkinData = targetSkinData;
        }
        
        @Override
        public void visit(Spatial spatial) {
            ObjectData pd = spatial.getUserData(ObjectData.USER_DATA);
            if (pd != null && pd == targetSkinData) {
                skinNode = spatial;
            }
        }
        
        public Spatial getResult() {
            return skinNode;
        }
    }

}
