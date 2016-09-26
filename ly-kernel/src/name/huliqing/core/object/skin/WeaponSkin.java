/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skin;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.SkinModule;
import name.huliqing.core.object.skill.SkinSkill;
import name.huliqing.core.object.slot.Slot;

/**
 * 武器装备处理。武器有一个比较特殊的地方，即武器除了"attach","detach"之外还有“取出(takeOn)” 和“收起(takeOff)”
 * 取出和收起是包含于attach状态的，只有武器处于attach时才可以进行takeOn和takeOff
 * @author huliqing
 */
public class WeaponSkin extends AbstractSkin implements Weapon {
    private static final Logger LOG = Logger.getLogger(WeaponSkin.class.getName());
    private final SkillService skillService = Factory.get(SkillService.class);
    
    // 武器类型
    private String weaponType;
    // 武器所有可支持的槽位
    private Slot[] slots;
    // 标记当前武器所在的槽位
    private Slot slot;
    
    // 这个Control是用来处理武器的“取出”和“收起”这个装配过程。
    private final HangControl hangControl = new HangControl();

    @Override
    public void setData(SkinData data) {
        super.setData(data);
        weaponType = data.getAsString("weaponType");
        String[] tempSlots = data.getAsArray("slots");
        if (tempSlots != null) {
            slots = new Slot[tempSlots.length];
            for (int i = 0; i < tempSlots.length; i++) {
                slots[i] = Loader.load(tempSlots[i]);
            }
        }
        if (weaponType == null) {
            LOG.log(Level.WARNING, "weaponType could not be null! weaponSkin={0}", data.getId());
        }
    }
    
    /**
     * 获取武器类型
     * @return 
     */
    @Override
    public String getWeaponType() {
        return weaponType;
    }
    
    /**
     * 获取当前武器占用的槽位,如果没有则返回null.
     * @return 
     */
    @Override
    public Slot getUsingSlot() {
        return slot;
    }

    @Override
    public boolean isSkinning() {
        // 武器的“取出”和“收起”是一个装配过程，所以这里当hangControl在处理过程中这个方法应该始终返回true.
        return hangControl.initialized;
    }

    @Override
    public void endSkinning() {
        // 调用hangControl来提前结束,由hangControl去自动判断。
        hangControl.cleanup();
    }
    
    @Override
    public void attach(Actor actor) {
        super.attach(actor);
        // 如果当前角色的武器状态是"挂起"的，则应该把武器放到指定的槽位上。
        SkinModule sm = actor.getModule(SkinModule.class);
        if (!sm.isWeaponTakeOn()) {
            attachWeaponOff(actor);
        }
    }
    
    /**
     * 武器收起
     */
    private void attachWeaponOff(Actor actor) {
        // 为武器找一个合适的槽位
        slot = getWeaponSlot(actor);
        
        // 如果找不到合适的槽位或者武器不支持槽位,则不处理
        if (slot == null) {
            return;
        }
        
        attach(actor, slot.getBindBone(), skinNode, slot.getLocalTranslation(), slot.getLocalRotation(), slot.getLocalScale());
    }
    
    /**
     * 把武器取出放到手上使用。
     * @param actor
     */
    @Override
    public void takeOn(Actor actor) {
        // 这里要直接清理一下，以避免上一次的takeOn执行过程还未结束的情况下导致冲突。
        hangControl.cleanup();
        
        if (!data.isUsed()) {
            return;
        }
        
        if (slot == null) {
            return;
        }
        
        // 根据武器类型从slot中获得“动画”技能.
        // 如果没有技能则直接attach, 并取消槽位占用
        String hangSkill = slot.getHangSkill(weaponType);
        if (hangSkill == null) {
            super.attach(actor);
            slot = null; 
            return;
        }
        
        // 标记装备正在”执行“,在武器实际取出之后才设置为false
        // hangTime：把武器节点添加到角色身上的时间点。
        hangControl.initialize(actor, hangSkill, true);
        actor.getSpatial().addControl(hangControl);
        
        // 武器取出后取消槽位占用
        slot = null;
    }
    
    /**
     * 把武器挂起，如挂在后背
     * @param actor
     */
    @Override
    public void takeOff(Actor actor) {
        // 这里要直接清理一下，以防止上一次的执行过程还未结束的情况下导致冲突。
        hangControl.cleanup();
        
        if (!data.isUsed()) {
            return;
        }
        
        // 挂起武器时先为武器找一个合适的槽位，如果没有合适或不支持槽位，则什么也不处理
        slot = getWeaponSlot(actor);
        if (slot == null) {
            return;
        }
        
        // 根据武器的左右手属性确定要用哪一个手拿武器的技能。
        String hangSkill = slot.getHangSkill(weaponType);
        // 使用一个动画技能来"取下"武器
        if (hangSkill != null) {
            hangControl.initialize(actor, hangSkill,  false);
            actor.getSpatial().addControl(hangControl);
        } else {
            attach(actor, slot.getBindBone(), skinNode, slot.getLocalTranslation(), slot.getLocalRotation(), slot.getLocalScale());
        }
    }
    
    /**
     * 为角色指定的武器选择一个合适的槽位来装备武器
     * @param actor
     * @param skinData 
     */
    private Slot getWeaponSlot(Actor actor) {
        SkinModule sm = actor.getModule(SkinModule.class);
        // supportedSlots角色可以支持的武器槽位列表
        List<String> supportedSlots = sm.getSupportedSlots();
        if (supportedSlots == null || supportedSlots.isEmpty()) {
            return null;
        }
        
        // 如果当前武器不支持任何槽位则直接返回null.
        if (slots == null || slots.length <= 0) {
            return null;
        }
        
        // 已经被占用的槽位
        List<String> slotsInUsing = getActorUsingSlots(sm);
        
        // 从武器所支持的所有槽位中选择一个可用的。
        for (Slot weaponSlot : slots) {
            // 槽位被占用
            if (slotsInUsing.contains(weaponSlot.getData().getId())) {
                continue;
            }
            // 角色不支持这个槽位
            if(!supportedSlots.contains(weaponSlot.getData().getId())) {
                continue;
            }
            return weaponSlot;
        }
        return null;
    }
    
    /**
     * 获取角色当前被占用的武器槽位列表
     * @return 
     */
    private List<String> getActorUsingSlots(SkinModule sm) {
        List<Skin> usingSkins = sm.getUsingSkins();
        if (usingSkins == null || usingSkins.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<String> usingSlots = new ArrayList<String>(2);
        for (Skin s : usingSkins) {
            if (!(s instanceof Weapon)) {
                continue;
            }
            Slot usingSlot = ((Weapon)s).getUsingSlot();
            if (usingSlot != null) {
                usingSlots.add(usingSlot.getData().getId());
            }
        }
        return usingSlots;
    }
    
    /**
     * 这个Processor用来计算时间点，并在指定的时间点取出或收起武器模型。
     */
    private class HangControl extends AbstractControl {

        private Actor actor;
        private float hangTime;
        private boolean takeOn;
        
        SkinSkill skinSkill;
        
        private float timeUsed;
        private boolean initialized;
        
        private void initialize(Actor actor, String hangSkill, boolean takeOn) {
            if (initialized) {
                // 防止bug
                throw new IllegalStateException("HangControl is already initialized!");
            }
            // 这是一个动画执行过程，在执行前标记为true,在执行后才设置为false.
            skinSkill = (SkinSkill) Loader.loadSkill(hangSkill);
            skinSkill.setActor(actor);
            skillService.playSkill(actor, skinSkill, false);
            
            this.actor = actor;
            this.hangTime = skinSkill.getTrueUseTime() * skinSkill.getHangTimePoint();
            this.takeOn = takeOn;
            
            initialized = true;
            timeUsed = 0;
        }
        
        @Override
        protected void controlUpdate(float tpf) {
            if (!initialized) 
                return;
            
            timeUsed += tpf;
            if (timeUsed >= hangTime) {
                cleanup();
            }
        }
        
        public void cleanup() {
            if (initialized) {
                if (takeOn) {
                    WeaponSkin.super.attach(actor);
                } else {
                    attachWeaponOff(actor);
                }
                spatial.removeControl(this);
                if (!skinSkill.isEnd()) {
                    skinSkill.cleanup();
                }
                skinSkill = null;
            }
            initialized = false;
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {}
        
    }
}
