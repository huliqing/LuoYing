/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skin;

import com.jme3.animation.Bone;
import com.jme3.animation.SkeletonControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ProtoData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.data.SlotData;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.loader.AssetLoader;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.object.DataFactory;
import name.huliqing.core.object.IntervalLogic;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.skill.SkinSkill;

/**
 * @author huliqing
 * @param <T>
 */
public class WeaponSkin<T extends SkinData> extends AbstractSkin<T> {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    
    @Override
    public void attach(Actor actor) {
        // 对于武器的attach不能用动画,直接attach就可以
        boolean takeOn = actor.getData().isWeaponTakeOn();
        if (takeOn) {
            super.attach(actor);
        } else {
            takeOffDirect(actor);
        }
    }
    
    /**
     * 把武器取出放到手上使用。
     * @param actor
     * @param force
     */
    public void takeOn(Actor actor, boolean force) {
        String weaponSlot = data.getSlot();
        if (weaponSlot == null) {
            super.attach(actor);
            return;
        }
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
        
//        float skillUseTime = skill.getSkillData().getTrueUseTime(); // remove20160503
        skill.setActor(actor);
        float skillUseTime = skill.getTrueUseTime();
        
        float hangTimePoint = skill.getHangTimePoint();
        // 这里必须使用skill实例，而不用SkillData，因为同一个skillData id执行的时候
        // 可能使用的是同一个Skill实例来处理，当角色同时有两把武器在切换时，就不能同
        // 一时刻用同一个SkillData，而应该特别指定用不同实例来同时执行。
        skillService.playSkill(actor, skill, force);

        // 动画逻辑处理
        TOAnimProcessLogic processor = new TOAnimProcessLogic(actor, 1, skillUseTime, hangTimePoint);
        playService.addObject(processor, false);
        
    }
    
    /**
     * 把武器挂起，如挂在后背
     * @param actor
     * @param force
     */
    public void takeOff(Actor actor, boolean force) {
        String weaponSlot = data.getSlot();
        if (weaponSlot == null) {
            super.attach(actor);
            return;
        }
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
        
//        float skillUseTime = skill.getSkillData().getTrueUseTime(); // remove
        skill.setActor(actor);
        float skillUseTime = skill.getTrueUseTime();
        
        float hangTimePoint = skill.getHangTimePoint();
        // 这里必须使用skill实例，而不用SkillData，因为同一个skillData id执行的时候
        // 可能使用的是同一个Skill实例来处理，当角色同时有两把武器在切换时，就不能同
        // 一时刻用同一个SkillData，而应该特别指定用不同实例来同时执行。
        skillService.playSkill(actor, skill, force);

        // 动画逻辑处理
        TOAnimProcessLogic processor = new TOAnimProcessLogic(actor, 0, skillUseTime, hangTimePoint);
        playService.addObject(processor, false);
        
    }
    
    private void takeOffDirect(Actor actor) {
        Spatial skinNode = findSkinNodes(actor.getModel(), data);
        if (skinNode == null) {
            String modelFile = data.getProto().getFile();
            if (modelFile == null) {
                return;
            }
            skinNode = AssetLoader.loadModel(modelFile);
            skinNode.setUserData(ProtoData.USER_DATA, data);
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
            SkeletonControl sc = actor.getModel().getControl(SkeletonControl.class);
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
    
    private class TOAnimProcessLogic extends IntervalLogic {

        private final Actor actor;
        private final int type; // 0:takeOff; 1 : takeOn
        private final float fullUseTime;
        private final float hangTimePoint;
        private float timeUsed;
        private boolean isOk;
        
        public TOAnimProcessLogic(Actor actor, int type, float fullUseTime, float hangTimePoint) {
            super(0);
            this.actor = actor;
            this.type = type;
            this.fullUseTime = fullUseTime;
            this.hangTimePoint = hangTimePoint;
        }

        @Override
        protected void doLogic(float tpf) {
            timeUsed += tpf;
           
            if (!isOk && timeUsed >= fullUseTime * hangTimePoint) {
                if (type == 1) {
                    WeaponSkin.super.attach(actor);
                } else {
                    takeOffDirect(actor);
                }
                isOk = true;
            }
            
            // 执行完要从全局移除动画逻辑
            if (timeUsed > fullUseTime) {
                playService.removeObject(this);
            }
        }
        
    }
}
