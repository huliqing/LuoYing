/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skin;

import com.jme3.animation.Bone;
import com.jme3.animation.SkeletonControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.Factory;
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
    public void attach(Actor actor) {
        data.setUsed(true);
        // 对于武器的attach不能用动画,直接attach就可以
        SkinModule sm = actor.getModule(SkinModule.class);
        if (sm.isWeaponTakeOn()) {
            super.attach(actor);
        } else {
            takeOffDirect(actor);
        }
    }

    @Override
    public boolean isWeapon() {
        return true;
    }

    @Override
    public int getWeaponType() {
        return data.getWeaponType();
    }
    
    /**
     * 把武器取出放到手上使用。
     * @param actor
     */
    public void takeOn(Actor actor) {
        String weaponSlot = data.getSlot();
        if (weaponSlot == null) {
            super.attach(actor);
            return;
        }
        
        // 武器取出后取消槽位占用
        data.setSlot(null);
        
        // 根据武器的左右手属性确定要用哪一个手拿武器的技能。
        SlotData sd = DataFactory.createData(weaponSlot);
        String hangSkill = null;
        if (data.isLeftHandWeapon()) {
            hangSkill = sd.getLeftHandSkinSkill();
        } else if (data.isRightHandWeapon()) {
            hangSkill = sd.getRightHandSkinSkill();
        }
        if (hangSkill == null) {
            super.attach(actor);
            return;
        }
        
        // 动画逻辑处理
        SkinSkill skill = (SkinSkill) Loader.loadSkill(hangSkill);
        skill.setActor(actor);

        // hangTime：把武器节点添加到角色身上的时间点。
        float hangTime = skill.getTrueUseTime() * skill.getHangTimePoint();
        HangProcessor processor = new HangProcessor(actor, hangTime, true);
        actor.getSpatial().addControl(processor);
        
        skillService.playSkill(actor, skill, false);
    }
    
    /**
     * 把武器挂起，如挂在后背
     * @param actor
     */
    public void takeOff(Actor actor) {
        String weaponSlot = getWeaponSlot(actor);
        if (weaponSlot == null) {
            data.setSlot(null);
            super.attach(actor);
            return;
        }
        data.setSlot(weaponSlot);
        // 根据武器的左右手属性确定要用哪一个手拿武器的技能。
        SlotData sd = DataFactory.createData(weaponSlot);
        String hangSkill = null;
        if (data.isLeftHandWeapon()) {
            hangSkill = sd.getLeftHandSkinSkill();
        } else if (data.isRightHandWeapon()) {
            hangSkill = sd.getRightHandSkinSkill();
        }
        if (hangSkill == null) {
            super.attach(actor);
            return;
        }
        
        SkinSkill skill = (SkinSkill) Loader.loadSkill(hangSkill);
        skill.setActor(actor);

        float hangTime = skill.getTrueUseTime() * skill.getHangTimePoint();
        HangProcessor processor = new HangProcessor(actor, hangTime, false);
        actor.getSpatial().addControl(processor);
        
        // 动画逻辑处理
        skillService.playSkill(actor, skill, false);
    }
    
    private void takeOffDirect(Actor actor) {
        if (skinNode == null) {
            String modelFile = data.getFile();
            if (modelFile == null) {
                return;
            }
            skinNode = AssetLoader.loadModel(modelFile);
        }
        
        String weaponSlot = data.getSlot();
        // 如果找不到合适的槽位或者武器根据不支持槽位，则直接attach到角色身上。
        // 不作takeOff处理
        if (weaponSlot == null) {
            super.attach(actor);
            return;
        }
        
        SlotData sd = DataFactory.createData(weaponSlot);
        String toBindBone = sd.getBindBone();
        Vector3f toLocalTranslation = sd.getLocalTranslation();
        float[] toLocalRotation = sd.getLocalRotation();
        Vector3f toLocalScale = sd.getLocalScale();
        
        // 如果指定了骨头，则将skin绑定到目标骨头
        if (toBindBone != null) {
            SkeletonControl sc = actor.getSpatial().getControl(SkeletonControl.class);
            Node boneNode = sc.getAttachmentsNode(toBindBone);
            
            // 如果没有指定本地变换，则直接从bone中获取
            Bone bone = sc.getSkeleton().getBone(toBindBone);
            if (toLocalRotation == null) {
                toLocalRotation = bone.getModelBindInverseRotation().toAngles(toLocalRotation);
            }
            if (toLocalScale == null) {
                toLocalScale = bone.getModelBindInverseScale();
            }
            // 因为大部分情况下Skin并不是以原点（0,0,0)作为模型的中心点，而是以模型
            // 的其中某一个位置，通常这个位置刚好是被绑定的骨头的位置，当模型attach到骨头
            // 位置时由于受到骨头的初始位置，旋转，缩放的影响，这个时候有必要把
            // 该点重新移到骨头所在的位置处。下面默认以被绑定的骨骼点作为模型原始点
            // 进行处理。
            if (toLocalTranslation == null) {
                // 骨骼点的位置
                toLocalTranslation = bone.getModelBindInversePosition().negate();
                // 被缩放后的位置
                bone.getModelBindInverseScale().mult(toLocalTranslation, toLocalTranslation);
                // 被旋转后的位置
                bone.getModelBindInverseRotation().mult(toLocalTranslation, toLocalTranslation);
                // 移动回骷髅点的位置
                toLocalTranslation.negateLocal();
            } 
            
            boneNode.attachChild(skinNode);
        }
        
        // 初始坐标变换
        if (toLocalTranslation != null) {
            skinNode.setLocalTranslation(toLocalTranslation);
        }
        if (toLocalRotation != null) {
            Quaternion rot = new Quaternion();
            skinNode.setLocalRotation(rot.fromAngles(toLocalRotation));
        }
        if (toLocalScale != null) {
            skinNode.setLocalScale(toLocalScale);
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
                    takeOffDirect(actor);
                }
                actor.getSpatial().removeControl(this);
            }
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {}
        
    }
}
