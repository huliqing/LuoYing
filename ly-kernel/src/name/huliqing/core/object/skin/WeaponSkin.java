/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skin;

import com.jme3.animation.Bone;
import com.jme3.animation.SkeletonControl;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.data.SlotData;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.object.AssetLoader;
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

    @Override
    public boolean isWeapon() {
        return true;
    }

    @Override
    public int getWeaponType() {
        return data.getWeaponType();
    }
        
    @Override
    public void attach(Actor actor) {
        super.attach(actor);
        // 对于武器的attach不能用动画,直接attach就可以
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
        if (!attached) {
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

        // hangTime：把武器节点添加到角色身上的时间点。
        actor.getSpatial().addControl(new HangProcessor(actor, skill.getTrueUseTime() * skill.getHangTimePoint(), true));
        
        // 武器取出后取消槽位占用
        data.setSlot(null);
    }
    
    /**
     * 把武器挂起，如挂在后背
     * @param actor
     */
    public void takeOff(Actor actor) {
        if (!attached) {
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
            SkinSkill skill = (SkinSkill) Loader.loadSkill(hangSkill);
            skill.setActor(actor);
            skillService.playSkill(actor, skill, false);
            actor.getSpatial().addControl(new HangProcessor(actor, skill.getTrueUseTime() * skill.getHangTimePoint(), false));
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
    
    private class HangProcessor extends AbstractControl {

        private final Actor actor;
        private final float hangTime;
        private float timeUsed;
        // 取出、收起
        private final boolean takeOn;
        
        public HangProcessor(Actor actor, float hangTime, boolean takeOn) {
            this.actor = actor;
            this.hangTime = hangTime;
            this.takeOn = takeOn;
        }
        
        @Override
        protected void controlUpdate(float tpf) {
            timeUsed += tpf;
           
            if (timeUsed >= hangTime) {
                if (takeOn) {
                    WeaponSkin.super.attach(actor);
                } else {
                    attachWeaponOff(actor);
                }
                actor.getSpatial().removeControl(this);
            }
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {}
        
    }
}
