/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.skin;

import com.jme3.animation.Bone;
import com.jme3.animation.SkeletonControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Config;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.AttributeApply;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.luoying.layer.service.DefineService;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.object.AssetLoader;
import name.huliqing.luoying.object.el.SBooleanEl;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.ModelEntity;
import name.huliqing.luoying.object.sound.SoundManager;
import name.huliqing.luoying.utils.GeometryUtils;

/**
 * @author huliqing
 */
public abstract class AbstractSkin implements Skin {
//    private static final Logger LOG = Logger.getLogger(AbstractSkin.class.getName());
    private final DefineService defineService = Factory.get(DefineService.class);
    private final EntityService entityService = Factory.get(EntityService.class);
    private final ElService elService = Factory.get(ElService.class);
    
    protected SkinData data;
    
    // 空上装备时的声音
    protected String[] sounds;
    // 用于检查角色是否可以使用这件装备
    protected SBooleanEl checkEl;
    
    //注：一件skin可属于多个type,如上下连身的套装，如法袍可属于 "7,8".
    //同时一件skin也可与多个其它skin进行排斥。这里的type和conflictType使用二
    //进制位来表示各个类型，例如一件上下连身的套装（类型属于7,8）在二进制表示为
    private long parts;
    // 定义与其它skin的排斥,当一件skin穿上身时，角色身上受排斥的skin将会脱下来。
    private long conflictParts;
    
    // 当指定了bindBone时，皮肤应该添加到bindBone所在的骨骼上，否则直接添加到角色根节点。
    private String bindBone;
    // skin的本地模型初始变换，注：localRotation需要使用Quaternion.fromAngle转换成Quaternion.
    private Vector3f localLocation;
    private Quaternion localRotation;
    private Vector3f localScale;
    
    // ---- inner
    // 装备的网格节点
    protected Spatial skinNode;
    
    @Override
    public void setData(SkinData data) {
        this.data = data;
        parts = defineService.getSkinPartDefine().convert(data.getAsArray("parts"));
        conflictParts = defineService.getSkinPartDefine().convert(data.getAsArray("conflictParts"));
        
        sounds = data.getAsArray("sounds");
        checkEl = elService.createSBooleanEl(data.getAsString("checkEl", "#{true}"));
        
        bindBone = data.getAsString("bindBone");
        localLocation = data.getAsVector3f("localLocation");
        localRotation = data.getAsQuaternion("localRotation");
        localScale = data.getAsVector3f("localScale");
    }

    @Override
    public SkinData getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
    }

    @Override
    public long getParts() {
        return parts;
    }

    @Override
    public long getConflictParts() {
        return parts | conflictParts;
    }

    @Override
    public Spatial getSpatial() {
        return skinNode;
    }

    /**
     * 默认情况下装备都是一次性装配，不需要装配过程，所以这个方法始终返回false，只有子类在实现一些特殊的装备过程时
     * 才需要覆盖这个方法。
     * @return 
     */
    @Override
    public boolean isSkinning() {
        return false;
    }

    /**
     * 默认情况下装备都是一次性立即装配，所以这个方法什么也不做。只有子类在实现一些特殊的装备过程时
     * 才需要覆盖这个方法。
     */
    @Override
    public void forceEndSkinning() {
        // donothing
    }
    
    @Override
    public boolean canUse(Entity actor) {
        return checkEl.setSource(actor.getAttributeManager()).getValue();
    }
    
    @Override
    public void attach(Entity actor) {
        data.setUsed(true);
        
        // 执行装备声音
        playSounds(actor, sounds);
        
        //  附加装备属性
        attachSkinAttributes(actor);
        
        // 部分Skin可能无指定模型或者不需要模型(如一些Mock武器，不存在实体模型)。
        if (data.getFile() == null) {
            return;
        }
        
        if (skinNode == null) {
            skinNode = AssetLoader.loadModel(data.getFile());
            // 如果模型希望以unshaded的方式展示，则需要在attach之前转化模型为unshaded
            if (actor instanceof ModelEntity && ((ModelEntity)actor).getData().isPreferUnshaded()) {
                GeometryUtils.makeUnshaded(skinNode);
            }
        }
        
        attach(actor, bindBone, skinNode, localLocation, localRotation, localScale);
    }
    
    /**
     * 给角色穿上装备
     * @param actor 指定的角色
     * @param bindBone 角色身上某块骨头的名称，如果没有指定这个骨头名称，则直接添加到角色节点下面。
     * @param skinNode 装备模型，如果为null则什么也不做。
     * @param localLocation 装备模型的位置偏移。
     * @param localRotation 装备模型的旋转偏移。
     * @param localScale 装备模型的缩放。
     */
    protected void attach(Entity actor, String bindBone, Spatial skinNode, Vector3f localLocation, Quaternion localRotation, Vector3f localScale) {
        if (skinNode == null) {
            return;
        }
        
        // 由于一些武器（如：弓）可能自身包含动画，即包含SkeletonControl,而
        // 这些节点在CustomSkeletonControl中被排除（避免冲突），因而在这里需要
        // 自已打开该功能。
        SkeletonControl skinSC = skinNode.getControl(SkeletonControl.class);
        if (skinSC != null) {
            if (!skinSC.isHardwareSkinningPreferred()) {
                skinSC.setHardwareSkinningPreferred(true);
            }
        }
        
        // 如果指定了bindBone，则将skin绑定到特定的骨头上。
        if (bindBone != null) {
            SkeletonControl actorSC = actor.getSpatial().getControl(SkeletonControl.class);
            Node boneNode = actorSC.getAttachmentsNode(bindBone);
            
            // 如果没有指定本地变换，则直接从bone中获取
            Bone bone = actorSC.getSkeleton().getBone(bindBone);
            if (localRotation == null) {
                localRotation = bone. getModelBindInverseRotation().clone();
            }
            if (localScale == null) {
                localScale = bone.getModelBindInverseScale();
            }
            // 因为大部分情况下Skin并不是以原点（0,0,0)作为模型的中心点，而是以模型
            // 的其中某一个位置，通常这个位置刚好是被绑定的骨头的位置，当模型attach到骨头
            // 位置时由于受到骨头的初始位置，旋转，缩放的影响，这个时候有必要把
            // 该点重新移到骨头所在的位置处。下面默认以被绑定的骨骼点作为模型原始点
            // 进行处理。
            if (localLocation == null) {
                // 骨骼点的位置
                localLocation = bone.getModelBindInversePosition().negate();
                // 被缩放后的位置
                bone.getModelBindInverseScale().mult(localLocation, localLocation);
                // 被旋转后的位置
                bone.getModelBindInverseRotation().mult(localLocation, localLocation);
                // 移动回骷髅点的位置
                localLocation.negateLocal();
            }
            
            if (Config.debug) {
                Logger.getLogger(getClass().getName())
                        .log(Level.INFO, "Skin model attach Transform => localRotation={0}, localScale={1}, localTranslation={2}"
                        , new Object[] {localRotation.toString(), localScale.toString(), localLocation.toString()});
                
            }
            
            boneNode.attachChild(skinNode);
            
        } else {
            
            // 如果角色节点不是Node类型，则不再进行后续处理，因为非Node模型不能添加子节点（装备模型）。
            if (!(actor.getSpatial() instanceof Node)) {
                Logger.getLogger(OutfitSkin.class.getName()).log(Level.WARNING
                        , "actorSpatial is not a Node, could not attach skins, actorId={0}, skinId={1}"
                        , new Object[] {actor.getData().getId(), data.getId()});
                return;
            }
            
            // 检查是否需要为skin打开HardWareSkinning
            GeometryUtils.trySwitchToHardware(actor, skinNode);
            
            // 添加到角色身上
            ((Node) actor.getSpatial()).attachChild(skinNode);
        }
        
        // 初始坐标变换
        if (localLocation != null) {
            skinNode.setLocalTranslation(localLocation);
        }
        if (localRotation != null) {
            skinNode.setLocalRotation(localRotation);
        }
        if (localScale != null) {
            skinNode.setLocalScale(localScale);
        }
        
    }
    
    private void playSounds(Entity actor, String[] sounds) {
        if (sounds != null) {
            for (String sid : sounds) {
                SoundManager.getInstance().playSound(sid, actor.getSpatial().getWorldTranslation());
            }
        }
    }
        
    /**
     * 把装备的属性附加到角色身上
     * @param actor
     * @param skinData 
     */
    private void attachSkinAttributes(Entity actor) {
        if (data.isAttributeApplied()) {
            return;
        }
        List<AttributeApply> aas = data.getApplyAttributes();
        if (aas != null) {
            for (AttributeApply aa : aas) {
                entityService.hitNumberAttribute(actor, aa.getAttribute(), aa.getAmount(), null);
            }
        }
        data.setAttributeApplied(true);
    }

    @Override
    public void detach(Entity actor) {
        // 1.----标记Using=false
        data.setUsed(false);
        
        // 执行装备声音
        playSounds(actor, sounds);
        
        // 2.----移除装备属性
        detachSkinAttributes(actor);
        
        // 3.----移除装备节点,并释放资源
        if (skinNode != null) {
            skinNode.removeFromParent();
            skinNode = null;
        }
    }
    
    /**
     * 把装备的属性从角色身上移除
     * @param actor
     * @param skinData 
     */
    private void detachSkinAttributes(Entity actor) {
        if (!data.isAttributeApplied()) {
            return;
        }
        List<AttributeApply> aas = data.getApplyAttributes();
        if (aas != null) {
            for (AttributeApply aa : aas) {
                entityService.hitNumberAttribute(actor, aa.getAttribute(), -aa.getAmount(), null);
            }
        }
        data.setAttributeApplied(false);
    }
    
    // remove20161130,moveto GeometryUtils.
    // --------------------------------------------------------------------------------------------------------------------------------
//    // 打开skin的hws,如果角色actor主SkeletonControl已经打开该功能,则skinNode必须自已打开.
//    private void checkSwitchToHardware(Entity actor, Spatial skinNode) {
//        // 如果hsw未打开,则不需要管,交由sc内部去一起处理就可以.
//        SkeletonControl sc = actor.getSpatial().getControl(SkeletonControl.class);
//        if (!sc.isHardwareSkinningUsed()) {
//            return;
//        }
//        
//        // 如果hsw已经开启过了,则需要把skinNode处理后再添加到actor中,否则skinNode将无法启用hsw
//        final Set<Mesh> targets = new HashSet<Mesh>();
//        final Set<Material> materials = new HashSet<Material>();
//        Node tempNode = new Node();
//        tempNode.attachChild(skinNode);
//        findTargets(tempNode, targets, materials);
//        
//        // Next full 10 bones (e.g. 30 on 24 bones)
//        Skeleton skeleton = sc.getSkeleton();
//        int numBones = ((skeleton.getBoneCount() / 10) + 1) * 10;
//        for (Material m : materials) {
//            m.setInt("NumberOfBones", numBones);
//        }
//        for (Mesh mesh : targets) {
//            if (mesh.isAnimated()) {
//                mesh.prepareForAnim(false);
//            }
//        }
//        sc.setSpatial(actor.getSpatial());
//    }
//    
//    private void findTargets(Node node, Set<Mesh> targets, Set<Material> materials) {
//        Mesh sharedMesh = null;        
//
//        for (Spatial child : node.getChildren()) {
//            if (child instanceof Geometry) {
//                Geometry geom = (Geometry) child;
//
//                // is this geometry using a shared mesh?
//                Mesh childSharedMesh = geom.getUserData(UserData.JME_SHAREDMESH);
//
//                if (childSharedMesh != null) {
//                    // Don���t bother with non-animated shared meshes
//                    if (childSharedMesh.isAnimated()) {
//                        // child is using shared mesh,
//                        // so animate the shared mesh but ignore child
//                        if (sharedMesh == null) {
//                            sharedMesh = childSharedMesh;
//                        } else if (sharedMesh != childSharedMesh) {
//                            throw new IllegalStateException("Two conflicting shared meshes for " + node);
//                        }
//                        materials.add(geom.getMaterial());
//                    }
//                } else {
//                    Mesh mesh = geom.getMesh();
//                    if (mesh.isAnimated()) {
//                        targets.add(mesh);
//                        materials.add(geom.getMaterial());
//                    }
//                }
//            } else if (child instanceof Node) {
//                findTargets((Node) child, targets, materials);
//            }
//        }
//
//        if (sharedMesh != null) {
//            targets.add(sharedMesh);
//        }
//    }
}
