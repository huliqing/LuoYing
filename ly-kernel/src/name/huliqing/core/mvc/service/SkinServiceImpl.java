/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Config;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.ActorConstants;
import name.huliqing.core.data.ArrayListWrap;
import name.huliqing.core.data.AttributeApply;
import name.huliqing.core.xml.ProtoData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.mvc.dao.SkinDao;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.SkinListener;
import name.huliqing.core.object.skin.Skin;
import name.huliqing.core.object.skin.WeaponSkin;
import name.huliqing.core.object.skin.WeaponStateUtils;

/**
 *
 * @author huliqing
 */
public class SkinServiceImpl implements SkinService {
    private static final Logger LOG = Logger.getLogger(SkinServiceImpl.class.getName());
    
    private ActionService actionService;
    private SkillService skillService;
    private AttributeService attributeService;
    private SkinDao skinDao;

    @Override
    public void inject() {
        skinDao = Factory.get(SkinDao.class);
        skillService = Factory.get(SkillService.class);
        actionService = Factory.get(ActionService.class);
        attributeService = Factory.get(AttributeService.class);
    }
    
    @Override
    public void attachSkin(Actor actor, SkinData skinData) {
        // 为了防止与takeOnWeapon/takeOffWeapon在异步上的冲突，这里必须限制在取
        // 摘武器的时候换装备
        if (skillService.isPlayingSkill(actor, SkillType.skin)) {
            return;
        }
        
        // 装备已经存在于身上
        if (isSkinAttached(actor.getModel(), skinData)) {
            return;
        }
        
        List<SkinListener> skinListeners = actor.getSkinListeners();
        
        // 1.====脱下排斥的装备
        int conflict = skinData.getType();
        conflict |= skinData.getConflictType();
        List<SkinData> conflictSkins = findSkinByType(actor.getModel(), conflict);
        if (!conflictSkins.isEmpty()) {
            for (SkinData sd : conflictSkins) {
                // 移除装备效果
                removeSkinApplyAttributes(actor, sd);
                // 装备御下
                Loader.loadSkin(sd).detach(actor);
                // 标记
                sd.setUsing(false);
                // 触发侦听器(脱)
                if (skinListeners != null) {
                    for (SkinListener sl : skinListeners) {
                        sl.onSkinDetached(actor, sd);
                    }
                }
            }
        }
        
        // 2.==== 换上装备
        // 为武器选择槽位(如果是武器）
        selectWeaponSlot(actor, skinData);
        // 装备模型
        Loader.loadSkin(skinData).attach(actor);
        // 装备效果
        addSkinApplyAttributes(actor, skinData);
        // 标记
        skinData.setUsing(true);
        // 触发侦听器(穿)
        if (skinListeners != null) {
            for (SkinListener sl : skinListeners) {
                sl.onSkinAttached(actor, skinData);
            }
        }
        
        // 3.====检查并补全基本装备
        // 注：因为基本皮肤为固有皮肤，不会在装备栏上显示，所以不触发skinListener的侦听。
        fixActorSkinBase(actor);
        
        // 4.====重新缓存武器状态及武器列表信息
        cacheWeaponsAndState(actor);
    }
    
    @Override
    public void detachSkin(Actor actor, SkinData skinData) {
        // 为了防止与takeOnWeapon/takeOffWeapon在异步上的冲突，这里必须限制在取
        // 摘武器的时候换装备
        if (skillService.isPlayingSkill(actor, SkillType.skin)) {
            return;
        }
        
        // 如果模型没有在使用中，则直接返回
        if (!skinData.isUsing()) {
            return;
        }
        
        // 1.====脱装备
        // 移除装备效果
        removeSkinApplyAttributes(actor, skinData);
        // 装备模型移除
        Loader.loadSkin(skinData).detach(actor);
        // 标记装备已经在使用
        skinData.setUsing(false);
        // 触发侦听器
        List<SkinListener> skinListeners = actor.getSkinListeners();
        if (skinListeners != null) {
            for (SkinListener sl : skinListeners) {
                sl.onSkinDetached(actor, skinData);
            }
        }
        
        // 2.====补上基本皮肤,同attach一样,不需要触发skinListener侦听。
        fixActorSkinBase(actor);
        
        // 3.====重新缓存武器状态及武器列表信息
        cacheWeaponsAndState(actor);
    }

    @Override
    public boolean isCanTakeOnWeapon(Actor actor) {
        // 如果角色当前正在执行换装技能，则直接返回。因为换装技能可能是异步的。
        // 在换装完成之前不能再执行换装，否则可能造成多把武器穿在身上的BUG。
        if (skillService.isPlayingSkill(actor, SkillType.skin)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isCanTakeOffWeapon(Actor actor) {
        if (skillService.isPlayingSkill(actor, SkillType.skin)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean takeOnWeapon(Actor actor, boolean force) {
        if (!force && !isCanTakeOnWeapon(actor)) {
            return false;
        }
        actor.getData().setWeaponTakeOn(true);
        List<SkinData> weaponSkins = getCurrentWeaponSkin(actor);
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
                ((WeaponSkin) skin).takeOn(actor, force);
                
                // 标记武器为using
                sd.setUsing(true);
            }
        }
        return true;
    }
    
    @Override
    public boolean takeOffWeapon(Actor actor, boolean force) {
        if (!force && !isCanTakeOffWeapon(actor)) {
            return false;
        }
        actor.getData().setWeaponTakeOn(false);
        List<SkinData> weaponSkins = getCurrentWeaponSkin(actor);
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
                ((WeaponSkin) skin).takeOff(actor, force);
            }
        }
        return true;
    }
    
    @Override
    public List<SkinData> getArmorSkins(Actor actor, List<SkinData> store) {
        return skinDao.getArmorSkins(actor.getData(), store);
    }

    @Override
    public List<SkinData> getWeaponSkins(Actor actor, List<SkinData> store) {
        return skinDao.getWeaponSkins(actor.getData(), store);
    }
    
    @Override
    public List<SkinData> getCurrentWeaponSkin(Actor actor) {
        // 先从缓存中获取，如果没有就重新生成。
        ArrayListWrap currentWeapons = actor.getModel()
                .getUserData(ActorConstants.USER_DATA_TEMP_WEAPONS);
        if (currentWeapons == null) {
            // 缓存武器状态及武器列表信息
            cacheWeaponsAndState(actor);
        }
        currentWeapons = actor.getModel().getUserData(ActorConstants.USER_DATA_TEMP_WEAPONS);
        return currentWeapons.getInnerData();
    }

    @Override
    public int getWeaponState(Actor actor) {
        Integer state = actor.getModel().getUserData(ActorConstants.USER_DATA_TEMP_WEAPON_STATE);
        if (state == null) {
            // 缓存武器状态及武器列表信息
            cacheWeaponsAndState(actor);
        }
        state = actor.getModel().getUserData(ActorConstants.USER_DATA_TEMP_WEAPON_STATE);
        return state;
    }
    
    @Override
    public boolean isWeaponTakeOn(Actor actor) {
        return actor.getData().isWeaponTakeOn();
    }
    
    @Override
    public boolean isWeapon(SkinData skinData) {
//        return skinData.getWeaponType() > 0;
        return skinData.isWeapon();
    }
    
    /**
     * 重新计算并缓存角色的武器状态和当前武器列表，该方法只有在角色切换装备
     * 或者未初始化武器状态时调用，因为比较耗性能，在战斗过程中可能需要频繁
     * 调用查询角色的当前武器状态及武器信息，因此把这两个信息缓存起来。
     * @param actor 
     */
    private void cacheWeaponsAndState(Actor actor) {
        ArrayListWrap weaponInUsed = actor.getModel().getUserData(ActorConstants.USER_DATA_TEMP_WEAPONS);
        if (weaponInUsed == null) {
            weaponInUsed = new ArrayListWrap<SkinData>(2);
        }
        weaponInUsed.clear();
        skinDao.getWeaponSkinsAllInUsed(actor.getData(), weaponInUsed.getInnerData());
        
        // 生成武器状态信息
        int weaponState = WeaponStateUtils.createWeaponState(weaponInUsed.getInnerData());
        
        // 缓存武器状态和列表
        actor.getModel().setUserData(ActorConstants.USER_DATA_TEMP_WEAPON_STATE, weaponState);
        actor.getModel().setUserData(ActorConstants.USER_DATA_TEMP_WEAPONS, weaponInUsed);
        
        if (Config.debug) {
//            LOG.info("skinService, recreate weaponState, weaponState=" + weaponState 
//                    + ", weaponInUsed=" + weaponInUsed 
//                    + ", weaponStateToBinary=" + Integer.toBinaryString(weaponState));
            LOG.log(Level.INFO, "skinService, recreate weaponState, weaponState={0}, weaponInUsed={1}, weaponStateToBinary={2}", new Object[]{weaponState, weaponInUsed, Integer.toBinaryString(weaponState)});
        }
    }
    
    // -------------------------------------------------------------------------
    
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
            List<SkinData> weaponSkins = skinDao.getWeaponSkinsAllInUsed(actor.getData(), null);
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
        
        // remove20160701以后不要随便给配置
//        // 如果候选列表不存在则直接使用武器配置中的第一个
//        if (slotCandidate == null) {
//            sd.setSlot(sd.getSlots().get(0));
//            return;
//        }
        
        // 从优先的候选列表中查找一个该武器可支持的槽位使用
        for (String slotC : slotCandidate.getArray()) {
            if (sd.getSlots().contains(slotC)) {
                sd.setSlot(slotC);
                slotCandidate.remove(slotC);
                return;
            }
        }
        
        // remove20160701以后不要随便给配置
//        // 如果候选列表没有合适的，则使用配置中的第一个
//        sd.setSlot(sd.getSlots().get(0));

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
     * 部分装备换装后需要补上缺失的基本皮肤,角色必须全部拥有基本皮肤所指定的
     * skinType，如果缺失则应该从基本皮肤上取出来补上。
     * @param actor 
     */
    private void fixActorSkinBase(Actor actor) {
        // 获取当前角色已经装备的所有skinTypes
        FullSkinTypesTraversal traversal = new FullSkinTypesTraversal(); 
        actor.getModel().breadthFirstTraversal(traversal);
        int actorSkinTypes = traversal.fullSkinTypes;
        
        List<SkinData> skinBases = actor.getData().getSkinBase();
        if (skinBases != null) {
            for (SkinData sd : skinBases) {
                if ((actorSkinTypes & sd.getType()) == 0) {
                    // 如果是武器则尝试找一个优先的武器槽位
                    selectWeaponSlot(actor, sd); 
                    Loader.loadSkin(sd).attach(actor);
                    addSkinApplyAttributes(actor, sd);
                    sd.setUsing(true);
                }
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
                ProtoData pd = spatial.getUserData(ProtoData.USER_DATA);
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
            ProtoData pd = actorModel.getUserData(ProtoData.USER_DATA);
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
        private SkinData targetSkinData;
        
        public SkinFinder(SkinData targetSkinData) {
            this.targetSkinData = targetSkinData;
        }
        
        @Override
        public void visit(Spatial spatial) {
            ProtoData pd = spatial.getUserData(ProtoData.USER_DATA);
            if (pd != null && pd == targetSkinData) {
                skinNode = spatial;
            }
        }
        
        public Spatial getResult() {
            return skinNode;
        }
    }
}
