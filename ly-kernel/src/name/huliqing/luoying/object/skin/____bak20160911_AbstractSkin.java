package name.huliqing.luoying.object.skin;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.object.skin;
//
//import com.jme3.animation.Bone;
//import com.jme3.animation.Skeleton;
//import com.jme3.animation.SkeletonControl;
//import com.jme3.material.Material;
//import com.jme3.math.Quaternion;
//import com.jme3.math.Vector3f;
//import com.jme3.scene.Geometry;
//import com.jme3.scene.Mesh;
//import com.jme3.scene.Node;
//import com.jme3.scene.SceneGraphVisitor;
//import com.jme3.scene.Spatial;
//import com.jme3.scene.UserData;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import name.huliqing.core.Config;
//import name.huliqing.core.Factory;
//import name.huliqing.core.data.AttributeApply;
//import name.huliqing.core.data.ObjectData;
//import name.huliqing.core.data.SkinData;
//import name.huliqing.core.mvc.service.AttributeService;
//import name.huliqing.core.mvc.service.ConfigService;
//import name.huliqing.core.object.AssetLoader;
//import name.huliqing.core.object.Loader;
//import name.huliqing.core.object.actor.Actor;
//import name.huliqing.core.object.attribute.NumberAttribute;
//import name.huliqing.core.object.module.SkinModule;
//
///**
// * 
// * @author huliqing
// * @param <T>
// */
//public abstract class ____bak20160911_AbstractSkin<T extends SkinData> implements Skin<T> {
//    private final ConfigService configService = Factory.get(ConfigService.class);
//    private final AttributeService attributeService = Factory.get(AttributeService.class);
//    private SkinModule skinModule;
//    
//    protected T data;
//    
//    private String bindBone;
//    // skin的本地模型初始变换，注：localRotation需要使用Quaternion.fromAngle转换成Quaternion.
//    private Vector3f localTranslation;
//    private float[] localRotation;
//    private Vector3f localScale;
//
//    @Override
//    public void setData(T data) {
//        this.data = data;
//        bindBone = data.getAsString("bindBone");
//        localTranslation = data.getAsVector3f("localTranslation");
//        localRotation = data.getAsFloatArray("localRotation");
//        localScale = data.getAsVector3f("localScale");
//    }
//
//    @Override
//    public T getData() {
//        return data;
//    }
//
//    protected Spatial loadSkinNode(String file) {
//        Spatial skinNode = AssetLoader.loadModel(file);
//        skinNode.setUserData(ObjectData.USER_DATA, data);
//
//        // 由于一些武器（如：弓）可能自身包含动画，即包含SkeletonControl,而
//        // 这些节点在CustomSkeletonControl中被排除（避免冲突），因而在这里需要
//        // 自已打开该功能。
//        if (configService.isUseHardwareSkinning() && skinNode.getControl(SkeletonControl.class) != null) {
//            skinNode.getControl(SkeletonControl.class).setHardwareSkinningPreferred(true);
//        }
//        return skinNode;
//    }
//
//    
//    @Override
//    public void attach(Actor actor, SkinModule sm, boolean isWeaponTakedOn) {
//        // 如果装备已经存在于身上则不重复处理。
//        if (isSkinAttached(actor, data)) {
//            return;
//        }
//        
//        // 1.----脱下排斥的装备
//        detachConflict(actor, sm);
//        
//        // 2.----换上装备
//        data.setUsed(true);
//        
//        //  附加装备属性
//        attachSkinAttributes(actor, data);
//        
//        // 如果角色节点不是Node类型，则不再进行后续处理，因为非Node模型不能添加子节点（装备模型）。
//        Spatial actorSpatial = actor.getSpatial();
//        if (!(actorSpatial instanceof Node)) {
//            Logger.getLogger(OutfitSkin.class.getName()).log(Level.WARNING
//                    , "actorSpatial is not a Node, could not attach skins, actorId={0}, skinId={1}"
//                    , new Object[] {actor.getData().getId(), data.getId()});
//            return;
//        }
//        
//        // 部分Skin可能无指定模型或者不需要模型(如一些Mock武器，不存在实体模型)。
//        if (data.getFile() == null) {
//            return;
//        }
//        
//        Node actorNode = (Node) actorSpatial;
//        
//        // 直接从角色身上查找是否存在指定Skin节点，如果不存在则重新载入一个。
//        // 重要：当添加模型时一定要把skinData设置上去,这是一个重要引用。
//        Spatial skinNode = findSkinNodes(actor.getSpatial(), data);
//        if (skinNode == null) {
//            skinNode = AssetLoader.loadModel(data.getFile());
//            skinNode.setUserData(ObjectData.USER_DATA, data);
//        }
//        
//        // 由于一些武器（如：弓）可能自身包含动画，即包含SkeletonControl,而
//        // 这些节点在CustomSkeletonControl中被排除（避免冲突），因而在这里需要
//        // 自已打开该功能。
//        SkeletonControl skinSC = skinNode.getControl(SkeletonControl.class);
//        if (skinSC != null) {
//            if (!skinSC.isHardwareSkinningPreferred()) {
//                skinSC.setHardwareSkinningPreferred(true);
//            }
//        }
//        
//        // 如果指定了bindBone，则将skin绑定到特定的骨头上。
//        if (bindBone != null) {
//            SkeletonControl actorSC = actorNode.getControl(SkeletonControl.class);
//            Node boneNode = actorSC.getAttachmentsNode(bindBone);
//            
//            // 如果没有指定本地变换，则直接从bone中获取
//            Bone bone = actorSC.getSkeleton().getBone(bindBone);
//            if (localRotation == null) {
////                localRotation = bone.getWorldBindInverseRotation().toAngles(localRotation);
//                localRotation = bone. getModelBindInverseRotation().toAngles(localRotation);
//            }
//            if (localScale == null) {
////                localScale = bone.getWorldBindInverseScale();
//                localScale = bone.getModelBindInverseScale();
//            }
//            // 因为大部分情况下Skin并不是以原点（0,0,0)作为模型的中心点，而是以模型
//            // 的其中某一个位置，通常这个位置刚好是被绑定的骨头的位置，当模型attach到骨头
//            // 位置时由于受到骨头的初始位置，旋转，缩放的影响，这个时候有必要把
//            // 该点重新移到骨头所在的位置处。下面默认以被绑定的骨骼点作为模型原始点
//            // 进行处理。
//            if (localTranslation == null) {
//                // 骨骼点的位置
////                localTranslation = bone.getWorldBindInversePosition().negate();
//                localTranslation = bone.getModelBindInversePosition().negate();
//                // 被缩放后的位置
////                bone.getWorldBindInverseScale().mult(localTranslation, localTranslation);
//                bone.getModelBindInverseScale().mult(localTranslation, localTranslation);
//                // 被旋转后的位置
////                bone.getWorldBindInverseRotation().mult(localTranslation, localTranslation);
//                bone.getModelBindInverseRotation().mult(localTranslation, localTranslation);
//                // 移动回骷髅点的位置
//                localTranslation.negateLocal();
//            }
//            
//            if (Config.debug) {
//                Logger.getLogger(getClass().getName())
//                        .log(Level.INFO, "Skin model attach Transform => localRotation={0}, localScale={1}, localTranslation={2}"
//                        , new Object[] {Arrays.toString(localRotation), localScale.toString(), localTranslation.toString()});
//                
//            }
//            
//            boneNode.attachChild(skinNode);
//            
//        } else {
//            
//            // 检查是否需要为skin打开HardWareSkinning
//            checkSwitchToHardware(actor, skinNode);
//            
//            // 添加到角色身上
//            actorNode.attachChild(skinNode);
//        }
//        
//        // 初始坐标变换
//        if (localTranslation != null) {
//            skinNode.setLocalTranslation(localTranslation);
//        }
//        if (localRotation != null) {
//            Quaternion rot = new Quaternion();
//            skinNode.setLocalRotation(rot.fromAngles(localRotation));
//        }
//        if (localScale != null) {
//            skinNode.setLocalScale(localScale);
//        }
//        
//        // 3.----补上角色的基本装备
//        fixBaseSkin(actor, sm);
//    }
//        
//    // 在attach skin之前要把一些冲突的装备脱下
//    private void detachConflict(Actor actor, SkinModule sm) {
//        // conflict为排斥的装备类型
//        int conflict = data.getType() | data.getConflictType();
//        List<SkinData> conflictSkins = SkinHelper.findConflictSkins(actor.getSpatial(), conflict);
//        if (!conflictSkins.isEmpty()) {
//            for (SkinData sd : conflictSkins) {
//                sm.detachSkin(sd);
//            }
//        }
//    }
//    
//    /**
//     * 把装备的属性附加到角色身上
//     * @param actor
//     * @param skinData 
//     */
//    private void attachSkinAttributes(Actor actor, SkinData skinData) {
//        if (skinData.isAttributeApplied()) {
//            return;
//        }
//        List<AttributeApply> aas = skinData.getApplyAttributes();
//        if (aas != null) {
//            for (AttributeApply aa : aas) {
//                NumberAttribute attr = attributeService.getAttributeByName(actor, aa.getAttribute());
//                if (attr != null) {
//                    attr.add(aa.getAmount());
//                }
//            }
//        }
//        skinData.setAttributeApplied(true);
//    }
//
//    @Override
//    public void detach(Actor actor, SkinModule sm) {
//        // 如果没有在使用
//        if (!data.isUsed()) {
//            return;
//        }
//        
//        // 1.----标记Using=false
//        data.setUsed(false);
//        
//        // 2.----移除装备属性
//        detachSkinAttributes(actor, data);
//        
//        // 3.----移除装备节点
//        Spatial sn = findSkinNodes(actor.getSpatial(), data);
//        if (sn != null) {
//            sn.removeFromParent();
//        }
//        
//        // 4.----补上角色的基本装备
//        fixBaseSkin(actor, sm);
//    }
//    
//    /**
//     * 把装备的属性从角色身上移除
//     * @param actor
//     * @param skinData 
//     */
//    private void detachSkinAttributes(Actor actor, SkinData skinData) {
//        if (!skinData.isAttributeApplied()) {
//            return;
//        }
//        List<AttributeApply> aas = skinData.getApplyAttributes();
//        if (aas != null) {
//            for (AttributeApply aa : aas) {
//                NumberAttribute attr = attributeService.getAttributeByName(actor, aa.getAttribute());
//                if (attr != null) {
//                    attr.add(-aa.getAmount());
//                }
//            }
//        }
//        skinData.setAttributeApplied(false);
//    }
//    
//    /**
//     * 从角色身上获取指定skinData的模型，如果不存在则返回null.
//     * @param actorModel 指定的角色
//     * @param skinData 指定的skinData
//     * @return 
//     */
//    protected Spatial findSkinNodes(Spatial actorModel, SkinData skinData) {
//        SkinFinder finder = new SkinFinder(skinData);
//        actorModel.breadthFirstTraversal(finder);
//        return finder.getResult();
//    }
//    
//    /**
//     * 检查装备模型是否已经穿在身上。
//     * @param actor
//     * @param skinData
//     * @return 
//     */
//    protected boolean isSkinAttached(Actor actor, SkinData skinData) {
//        SkinFinder finder = new SkinFinder(skinData);
//        actor.getSpatial().breadthFirstTraversal(finder);
//        return finder.getResult() != null;
//    }
//    
//    /**
//     * 部分装备换装后需要补上缺失的基本皮肤,角色必须全部拥有基本皮肤所指定的
//     * skinType，如果缺失则应该从基本皮肤上取出来补上。
//     */
//    private void fixBaseSkin(Actor actor, SkinModule sm) {
//        // 获取当前角色已经装备的所有skinTypes
//        FullSkinTypesTraversal traversal = new FullSkinTypesTraversal(); 
//        actor.getSpatial().breadthFirstTraversal(traversal);
//        int actorSkinTypes = traversal.fullSkinTypes;
//
//        List<SkinData> skinDatas = sm.getBaseSkins();
//        if (skinDatas != null) {
//            for (SkinData baseSkin : skinDatas) {
//                if ((actorSkinTypes & baseSkin.getType()) == 0) {
//                    sm.attachSkin(baseSkin);
//                }
//            }
//        }
//    }
//    
//        /**
//     * 查找当前角色装备的所有skinType
//     */
//    private class FullSkinTypesTraversal implements SceneGraphVisitor {
//        public int fullSkinTypes;
//        @Override
//        public void visit(Spatial actorModel) {
//            ObjectData pd = actorModel.getUserData(ObjectData.USER_DATA);
//            if (pd != null && (pd instanceof SkinData)) {
//                SkinData sd = (SkinData) pd;
//                fullSkinTypes |= sd.getType();
//            }
//        }
//    }
//    
//    // --------------------------------------------------------------------------------------------------------------------------------
//    
//    /**
//     * 用于协助从actorModel中获取指定skinData的模型。
//     */
//    private class SkinFinder implements SceneGraphVisitor {
//
//        private Spatial skinNode;
//        private final SkinData targetSkinData;
//        
//        public SkinFinder(SkinData targetSkinData) {
//            this.targetSkinData = targetSkinData;
//        }
//        
//        @Override
//        public void visit(Spatial spatial) {
//            ObjectData pd = spatial.getUserData(ObjectData.USER_DATA);
//            if (pd != null && pd == targetSkinData) {
//                skinNode = spatial;
//            }
//        }
//        
//        public Spatial getResult() {
//            return skinNode;
//        }
//    }
//    
//    // --------------------------------------------------------------------------------------------------------------------------------
//    // 打开skin的hws,如果角色actor主SkeletonControl已经打开该功能,则skinNode必须自已打开.
//    private void checkSwitchToHardware(Actor actor, Spatial skinNode) {
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
//}
