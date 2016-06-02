///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.skin;
//
//import com.jme3.animation.Bone;
//import com.jme3.animation.SkeletonControl;
//import com.jme3.math.Quaternion;
//import com.jme3.math.Vector3f;
//import com.jme3.scene.Node;
//import com.jme3.scene.Spatial;
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.data.ProtoData;
//import name.huliqing.fighter.data.SkillData;
//import name.huliqing.fighter.data.SkinData;
//import name.huliqing.fighter.game.service.ActorService;
//import name.huliqing.fighter.game.service.PlayService;
//import name.huliqing.fighter.game.service.SkillService;
//import name.huliqing.fighter.loader.AssetLoader;
//import name.huliqing.fighter.loader.Loader;
//import name.huliqing.fighter.logic.AbstractLogic;
//import name.huliqing.fighter.object.actor.Actor;
//import name.huliqing.fighter.object.skill.Skill;
//
///**
// * @author huliqing
// */
//public class WeaponSkin extends AbstractSkin {
//    private final PlayService playService = Factory.get(PlayService.class);
//    private final ActorService actorService = Factory.get(ActorService.class);
//    private final SkillService skillService = Factory.get(SkillService.class);
//    
//    // ==== 武器的挂靠设置 ====
//    
//    // 是否开始武器挂靠动画,只在打开这个选项后面的其它TO设置才有效。
//    private boolean toEnabled;
//    // 挂靠的位置变换
//    private String toBindBone;
//    private Vector3f toLocalTranslation;
//    private float[] toLocalRotation;
//    private Vector3f toLocalScale;
//    
//    // 换装技能
//    private String toSkill;
//    
//    // 需要配置TOSkill. 挂靠时的动画名称，该名称会替换TOSkill使用时的动画名称
//    private String toAnim;
//    
//    // 需要配置TOSkill.
//    // 挂靠动画执行时要使用的动画通道，绑定到animChannel.xml, 格式："animChannel1,animChannel2,..."
//    // 注：如果指定了该值，角色必须配置有相关的animChannels才有用。如果没有指定这个值或者角色没有
//    // 配置animChannels,则将使用默认通道（包含所有骨骼的动画）
//    private String[] toAnimChannels;
//    
//    // 需要配置TOSkill.挂靠动画的时间长度，即取武器或挂武器的动画时间长度,单位秒
//    private float toAnimUseTime;
//    
//    // 需要配置TOSkill.挂靠动画的时间点，这个时间点取值[0.0~1.0] 即角色取武器或挂武器的时间点，
//    // 也即武器出现在手上或在挂靠点上（如背上，腿侧）的时间点。
//    // 这个时间点是相对于toAnimUseTime而定的。
//    private float toAnimHangTimePoint;
//
//    public WeaponSkin(SkinData data) {
//        super(data);
//        toEnabled = data.getProto().getAsBoolean("TOEnabled", toEnabled);
//        if (toEnabled) {
//            toBindBone = data.getProto().getAttribute("TOBindBone");
//            toLocalTranslation = data.getProto().getAsVector3f("TOLocalTranslation");
//            toLocalRotation = data.getProto().getAsFloatArray("TOLocalRotation");
//            toLocalScale = data.getProto().getAsVector3f("TOLocalScale");
//            toSkill = data.getProto().getAttribute("TOSkill");
//            toAnim = data.getProto().getAttribute("TOAnim");
//            toAnimChannels = data.getProto().getAsArray("TOAnimChannels");
//            toAnimUseTime = data.getProto().getAsFloat("TOAnimUseTime", 0.5f);
//            toAnimHangTimePoint = data.getProto().getAsFloat("TOAnimHangTimePoint", 0.5f);
//        }
//    }
//
//    @Override
//    public void attach(Actor actor) {
////        if (actor.getData().getAttributeStore().getName().equals("樱")) {
////            System.out.println("测试WeaponSkin");
////        }
//        boolean takeOn = actor.getData().isWeaponTakeOn();
//        if (takeOn) {
//            takeOn(actor);
//        } else {
//            takeOff(actor);
//        }
//    }
//    
//    /**
//     * 把武器取出放到手上使用。
//     */
//    public void takeOn(Actor actor) {
//        // 注：如果没有打开该功能则直接使用父类方法。
//        if (!toEnabled || toSkill == null || toAnim == null) {
//            super.attach(actor);
//            return;
//        }
//        
//        // 执行技能
//        Skill skill = Loader.loadSkill(toSkill);
//        SkillData sd = skill.getSkillData();
//        sd.setAnimation(toAnim);
//        sd.setChannels(toAnimChannels);
//        sd.setUseTime(toAnimUseTime);
//        // 这里必须使用skill实例，而不用SkillData，因为同一个skillData id执行的时候
//        // 可能使用的是同一个Skill实例来处理，当角色同时有两把武器在切换时，就不能同
//        // 一时刻用同一个SkillData，而应该特别指定用不同实例来同时执行。
//        skillService.playSkill(actor, skill, false);
//
//        // 动画逻辑处理
//        TOAnimProcessLogic processor = new TOAnimProcessLogic(actor, 1);
//        playService.addLogic(processor);
//        
//    }
//    
//    /**
//     * 把武器挂起，如挂在后背
//     */
//    public void takeOff(Actor actor) {
//        if (!toEnabled) {
//            // 注：如果没有打开，则不处理。注意不要把武器detach掉
//            return;
//        }
//        
//        if (toSkill == null || toAnim == null) {
//            // 如果没有设置技能及动画，则直接挂起武器。
//            takeOffInner(actor);
//        } else {
//            // 执行挂起技能
//            Skill skill = Loader.loadSkill(toSkill);
//            SkillData sd = skill.getSkillData();
//            sd.setAnimation(toAnim);
//            sd.setChannels(toAnimChannels);
//            sd.setUseTime(toAnimUseTime);
//            // 这里必须使用skill实例，而不用SkillData，因为同一个skillData id执行的时候
//            // 可能使用的是同一个Skill实例来处理，当角色同时有两把武器在切换时，就不能同
//            // 一时刻用同一个SkillData，而应该特别指定用不同实例来同时执行。
//            skillService.playSkill(actor, skill, false);
//
//            // 动画逻辑处理
//            TOAnimProcessLogic processor = new TOAnimProcessLogic(actor, 0);
//            playService.addLogic(processor);
//        }
//        
//    }
//    
//    private void takeOffInner(Actor actor) {
//        // 如果已经detach掉，则不理
//        Spatial skinNode = findSkinNodes(actor.getModel(), data);
//        if (skinNode == null) {
//            skinNode = AssetLoader.loadModel(data.getProto().getFile());
//            skinNode.setUserData(ProtoData.USER_DATA, data);
//        }
//        
//        // 如果指定了骨头，则将skin绑定到目标骨头
//        if (toBindBone != null) {
//            SkeletonControl sc = actor.getModel().getControl(SkeletonControl.class);
//            Node boneNode = sc.getAttachmentsNode(toBindBone);
//            
//            // 如果没有指定本地变换，则直接从bone中获取
//            Bone bone = sc.getSkeleton().getBone(toBindBone);
//            if (toLocalRotation == null) {
//                toLocalRotation = bone.getWorldBindInverseRotation().toAngles(toLocalRotation);
//            }
//            if (toLocalScale == null) {
//                toLocalScale = bone.getWorldBindInverseScale();
//            }
//            // 因为大部分情况下Skin并不是以原点（0,0,0)作为模型的中心点，而是以模型
//            // 的其中某一个位置，通常这个位置刚好是被绑定的骨头的位置，当模型attach到骨头
//            // 位置时由于受到骨头的初始位置，旋转，缩放的影响，这个时候有必要把
//            // 该点重新移到骨头所在的位置处。下面默认以被绑定的骨骼点作为模型原始点
//            // 进行处理。
//            if (toLocalTranslation == null) {
//                // 骨骼点的位置
//                toLocalTranslation = bone.getWorldBindInversePosition().negate();
//                // 被缩放后的位置
//                bone.getWorldBindInverseScale().mult(toLocalTranslation, toLocalTranslation);
//                // 被旋转后的位置
//                bone.getWorldBindInverseRotation().mult(toLocalTranslation, toLocalTranslation);
//                // 移动回骷髅点的位置
//                toLocalTranslation.negateLocal();
//            } 
//            
//            boneNode.attachChild(skinNode);
//        }
//        
//        // 初始坐标变换
//        if (toLocalTranslation != null) {
//            skinNode.setLocalTranslation(toLocalTranslation);
//        }
//        if (toLocalRotation != null) {
//            Quaternion rot = new Quaternion();
//            skinNode.setLocalRotation(rot.fromAngles(toLocalRotation));
//        }
//        if (toLocalScale != null) {
//            skinNode.setLocalScale(toLocalScale);
//        }
//    }
//    
//    private class TOAnimProcessLogic extends AbstractLogic {
//
//        private Actor actor;
//        private int type; // 0:takeOff; 1 : takeOn
//        private boolean isOk;
//        private float timeUsed;
//        
//        public TOAnimProcessLogic(Actor actor, int type) {
//            super(0);
//            this.actor = actor;
//            this.type = type;
//        }
//
//        @Override
//        protected void doLogic(float tpf) {
//            timeUsed += tpf;
//           
//            if (!isOk && timeUsed >= toAnimUseTime * toAnimHangTimePoint) {
//                if (type == 1) {
//                    WeaponSkin.super.attach(actor);
//                } else {
//                    takeOffInner(actor);
//                }
//                isOk = true;
//            }
//            
//            // 执行完要从全局移除动画逻辑
//            if (timeUsed > toAnimUseTime) {
//                playService.removeLogic(this);
//            }
//        }
//        
//    }
//}
