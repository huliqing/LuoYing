/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skin;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.data.SlotData;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.object.Loader;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.SkinModule;
import name.huliqing.core.object.skill.SkinSkill;

/**
 * @author huliqing
 */
public class WeaponSkin extends AbstractSkin {
    private final SkillService skillService = Factory.get(SkillService.class);

    private final HangControl hangControl = new HangControl();
    
    @Override
    public boolean isWeapon() {
        return true;
    }

    /**
     * 获取武器类型,Skin必须是武器时才有意义
     * @return 
     */
    public int getWeaponType() {
        return data.getWeaponType();
    }

    @Override
    public boolean isSkinning() {
        return hangControl.initialized;
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
        if (data.getSlot() == null) {
            data.setSlot(getWeaponSlot(actor));
        }
        
        // 如果找不到合适的槽位或者武器不支持槽位,直不处理
        if (data.getSlot() == null) {
            return;
        }
        
        SlotData slot = DataFactory.createData(data.getSlot());
        attach(actor, slot.getBindBone(), skinNode, slot.getLocalTranslation(), slot.getLocalRotation(), slot.getLocalScale());
    }
    
    /**
     * 把武器取出放到手上使用。
     * @param actor
     */
    public void takeOn(Actor actor) {
        // 这里要直接清理一下，以避免上一次的takeOn执行过程还未结束的情况下导致冲突。
        hangControl.cleanup();
        
        if (!data.isUsed()) {
            return;
        }
        
        if (data.getSlot() == null) {
            return;
        }
        
        // 根据武器的左右手属性确定要用哪一个手拿武器的技能。
        SlotData slot = DataFactory.createData(data.getSlot());
        String hangSkill = null;
        if (data.isLeftHandWeapon()) {
            hangSkill = slot.getLeftHandSkinSkill();
        } else if (data.isRightHandWeapon()) {
            hangSkill = slot.getRightHandSkinSkill();
        }
        
        if (hangSkill == null) {
            super.attach(actor);
            return;
        }
        
        // 动画逻辑处理
        SkinSkill skill = (SkinSkill) Loader.loadSkill(hangSkill);
        skill.setActor(actor);
        skillService.playSkill(actor, skill, false);

        // 标记装备正在”执行“,在武器实际取出之后才设置为false
        // hangTime：把武器节点添加到角色身上的时间点。
        hangControl.initialize(actor, skill.getTrueUseTime() * skill.getHangTimePoint(), true);
        actor.getSpatial().addControl(hangControl);
        
        // 武器取出后取消槽位占用
        data.setSlot(null);
    }
    
    /**
     * 把武器挂起，如挂在后背
     * @param actor
     */
    public void takeOff(Actor actor) {
        // 这里要直接清理一下，以防止上一次的执行过程还未结束的情况下导致冲突。
        hangControl.cleanup();
        
        if (!data.isUsed()) {
            return;
        }
        
        // 挂起武器时先为武器找一个合适的槽位，如果没有合适或不支持槽位，则什么也不处理
        String weaponSlot = getWeaponSlot(actor);
        data.setSlot(weaponSlot);
        if (data.getSlot() == null) {
            return;
        }
        
        // 根据武器的左右手属性确定要用哪一个手拿武器的技能。
        SlotData slot = DataFactory.createData(weaponSlot);
        String hangSkill = null;
        if (data.isLeftHandWeapon()) {
            hangSkill = slot.getLeftHandSkinSkill();
        } else if (data.isRightHandWeapon()) {
            hangSkill = slot.getRightHandSkinSkill();
        }
        
        // 使用一个动画技能来"取下"武器
        if (hangSkill != null) {
            // 这是一个动画执行过程，在执行前标记为true,在执行后才设置为false.
            SkinSkill skill = (SkinSkill) Loader.loadSkill(hangSkill);
            skill.setActor(actor);
            skillService.playSkill(actor, skill, false);
            
            // hangControl作为control用于判断时间点，根据时间点进行”取出“或”收起“装备
            hangControl.initialize(actor, skill.getTrueUseTime() * skill.getHangTimePoint(), false);
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
    private String getWeaponSlot(Actor actor) {
        SkinModule sm = actor.getModule(SkinModule.class);
        // supportedSlots角色可以支持的武器槽位列表
        List<String> supportedSlots = sm.getSupportedSlots();
        if (supportedSlots == null || supportedSlots.isEmpty()) {
            return null;
        }
        // 武器可以支持的槽位
        if (data.getSlots() == null || data.getSlots().isEmpty()) {
            return null;
        }
        
        // 已经被占用的槽位
        List<String> slotsInUsing = getUsingSlots(sm);
        
        // 从武器所支持的所有槽位中选择一个可用的。
        for (String slot : data.getSlots()) {
            // 槽位被占用
            if (slotsInUsing.contains(slot)) {
                continue;
            }
            // 角色不支持这个槽位
            if(!supportedSlots.contains(slot)) {
                continue;
            }
            return slot;
        }
        return null;
    }
    
    /**
     * 获取角色当前被占用的武器槽位id列表
     * @return 
     */
    private List<String> getUsingSlots(SkinModule sm) {
        List<Skin> usingSkins = sm.getUsingSkins();
        if (usingSkins == null || usingSkins.isEmpty()) {
            return null;
        }
        List<String> usingSlots = new ArrayList<String>(2);
        for (Skin s : usingSkins) {
            if (s.isWeapon() && s.getData().getSlot() != null) {
                usingSlots.add(s.getData().getSlot());
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
        private float timeUsed;
        private boolean initialized;
        
        private void initialize(Actor actor, float hangTime, boolean takeOn) {
            if (initialized) {
                // 防止bug
                throw new IllegalStateException("HangControl is already initialized!");
            }
            this.actor = actor;
            this.hangTime = hangTime;
            this.takeOn = takeOn;
            initialized = true;
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
            }
            initialized = false;
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {}
        
    }
}
