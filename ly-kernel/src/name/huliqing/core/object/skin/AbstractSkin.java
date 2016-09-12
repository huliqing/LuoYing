/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skin;

import com.jme3.animation.Bone;
import com.jme3.animation.Skeleton;
import com.jme3.animation.SkeletonControl;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.UserData;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Config;
import name.huliqing.core.Factory;
import name.huliqing.core.data.AttributeApply;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.object.AssetLoader;
import name.huliqing.core.object.actor.Actor;

/**
 * 
 * @author huliqing
 */
public abstract class AbstractSkin implements Skin {
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    
    protected SkinData data;
    
    // 当指定了bindBone时，皮肤应该添加到bindBone所在的骨骼上，否则直接添加到角色根节点。
    private String bindBone;
    // skin的本地模型初始变换，注：localRotation需要使用Quaternion.fromAngle转换成Quaternion.
    private Vector3f localTranslation;
    private float[] localRotation;
    private Vector3f localScale;

    // ---- inner
    protected Spatial skinNode;
    protected boolean attached;
    
    @Override
    public void setData(SkinData data) {
        this.data = data;
        
        bindBone = data.getAsString("bindBone");
        localTranslation = data.getAsVector3f("localTranslation");
        localRotation = data.getAsFloatArray("localRotation");
        localScale = data.getAsVector3f("localScale");
    }

    @Override
    public SkinData getData() {
        return data;
    }

    @Override
    public int getType() {
        return data.getType();
    }

    @Override
    public int getConflicts() {
        return data.getType() | data.getConflictType();
    }

    @Override
    public Spatial getSpatial() {
        return skinNode;
    }
    
    @Override
    public boolean isBaseSkin() {
        return data.isBaseSkin();
    }

    // remove20160912
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

    @Override
    public boolean isAttached() {
        return attached;
    }
    
    @Override
    public void attach(Actor actor) {
        data.setUsed(true);
        attached = true;
        
        //  附加装备属性
        attachSkinAttributes(actor);
        
        // 如果角色节点不是Node类型，则不再进行后续处理，因为非Node模型不能添加子节点（装备模型）。
        Spatial actorSpatial = actor.getSpatial();
        if (!(actorSpatial instanceof Node)) {
            Logger.getLogger(OutfitSkin.class.getName()).log(Level.WARNING
                    , "actorSpatial is not a Node, could not attach skins, actorId={0}, skinId={1}"
                    , new Object[] {actor.getData().getId(), data.getId()});
            return;
        }
        
        // 部分Skin可能无指定模型或者不需要模型(如一些Mock武器，不存在实体模型)。
        if (data.getFile() == null) {
            return;
        }
        
        if (skinNode == null) {
            skinNode = AssetLoader.loadModel(data.getFile());
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
            SkeletonControl actorSC = actorSpatial.getControl(SkeletonControl.class);
            Node boneNode = actorSC.getAttachmentsNode(bindBone);
            
            // 如果没有指定本地变换，则直接从bone中获取
            Bone bone = actorSC.getSkeleton().getBone(bindBone);
            if (localRotation == null) {
                localRotation = bone. getModelBindInverseRotation().toAngles(localRotation);
            }
            if (localScale == null) {
                localScale = bone.getModelBindInverseScale();
            }
            // 因为大部分情况下Skin并不是以原点（0,0,0)作为模型的中心点，而是以模型
            // 的其中某一个位置，通常这个位置刚好是被绑定的骨头的位置，当模型attach到骨头
            // 位置时由于受到骨头的初始位置，旋转，缩放的影响，这个时候有必要把
            // 该点重新移到骨头所在的位置处。下面默认以被绑定的骨骼点作为模型原始点
            // 进行处理。
            if (localTranslation == null) {
                // 骨骼点的位置
                localTranslation = bone.getModelBindInversePosition().negate();
                // 被缩放后的位置
                bone.getModelBindInverseScale().mult(localTranslation, localTranslation);
                // 被旋转后的位置
                bone.getModelBindInverseRotation().mult(localTranslation, localTranslation);
                // 移动回骷髅点的位置
                localTranslation.negateLocal();
            }
            
            if (Config.debug) {
                Logger.getLogger(getClass().getName())
                        .log(Level.INFO, "Skin model attach Transform => localRotation={0}, localScale={1}, localTranslation={2}"
                        , new Object[] {Arrays.toString(localRotation), localScale.toString(), localTranslation.toString()});
                
            }
            
            boneNode.attachChild(skinNode);
            
        } else {
            
            // 检查是否需要为skin打开HardWareSkinning
            checkSwitchToHardware(actor, skinNode);
            
            // 添加到角色身上
            ((Node) actorSpatial).attachChild(skinNode);
        }
        
        // 初始坐标变换
        if (localTranslation != null) {
            skinNode.setLocalTranslation(localTranslation);
        }
        if (localRotation != null) {
            Quaternion rot = new Quaternion();
            skinNode.setLocalRotation(rot.fromAngles(localRotation));
        }
        if (localScale != null) {
            skinNode.setLocalScale(localScale);
        }
        
    }
        
    /**
     * 把装备的属性附加到角色身上
     * @param actor
     * @param skinData 
     */
    private void attachSkinAttributes(Actor actor) {
        if (data.isAttributeApplied()) {
            return;
        }
        List<AttributeApply> aas = data.getApplyAttributes();
        if (aas != null) {
            for (AttributeApply aa : aas) {
                attributeService.addNumberAttributeValue(actor, aa.getAttribute(), aa.getAmount());
            }
        }
        data.setAttributeApplied(true);
    }

    @Override
    public void detach(Actor actor) {
        // 1.----标记Using=false
        data.setUsed(false);
        attached = false;
        
        // 2.----移除装备属性
        detachSkinAttributes(actor);
        
        // 3.----移除装备节点
        if (skinNode != null) {
            skinNode.removeFromParent();
        }
    }
    
    /**
     * 把装备的属性从角色身上移除
     * @param actor
     * @param skinData 
     */
    private void detachSkinAttributes(Actor actor) {
        if (!data.isAttributeApplied()) {
            return;
        }
        List<AttributeApply> aas = data.getApplyAttributes();
        if (aas != null) {
            for (AttributeApply aa : aas) {
                attributeService.addNumberAttributeValue(actor, aa.getAttribute(), -aa.getAmount());
            }
        }
        data.setAttributeApplied(false);
    }
    
    // --------------------------------------------------------------------------------------------------------------------------------
    // 打开skin的hws,如果角色actor主SkeletonControl已经打开该功能,则skinNode必须自已打开.
    private void checkSwitchToHardware(Actor actor, Spatial skinNode) {
        // 如果hsw未打开,则不需要管,交由sc内部去一起处理就可以.
        SkeletonControl sc = actor.getSpatial().getControl(SkeletonControl.class);
        if (!sc.isHardwareSkinningUsed()) {
            return;
        }
        
        // 如果hsw已经开启过了,则需要把skinNode处理后再添加到actor中,否则skinNode将无法启用hsw
        final Set<Mesh> targets = new HashSet<Mesh>();
        final Set<Material> materials = new HashSet<Material>();
        Node tempNode = new Node();
        tempNode.attachChild(skinNode);
        findTargets(tempNode, targets, materials);
        
        // Next full 10 bones (e.g. 30 on 24 bones)
        Skeleton skeleton = sc.getSkeleton();
        int numBones = ((skeleton.getBoneCount() / 10) + 1) * 10;
        for (Material m : materials) {
            m.setInt("NumberOfBones", numBones);
        }
        for (Mesh mesh : targets) {
            if (mesh.isAnimated()) {
                mesh.prepareForAnim(false);
            }
        }
        sc.setSpatial(actor.getSpatial());
    }
    
    private void findTargets(Node node, Set<Mesh> targets, Set<Material> materials) {
        Mesh sharedMesh = null;        

        for (Spatial child : node.getChildren()) {
            if (child instanceof Geometry) {
                Geometry geom = (Geometry) child;

                // is this geometry using a shared mesh?
                Mesh childSharedMesh = geom.getUserData(UserData.JME_SHAREDMESH);

                if (childSharedMesh != null) {
                    // Don���t bother with non-animated shared meshes
                    if (childSharedMesh.isAnimated()) {
                        // child is using shared mesh,
                        // so animate the shared mesh but ignore child
                        if (sharedMesh == null) {
                            sharedMesh = childSharedMesh;
                        } else if (sharedMesh != childSharedMesh) {
                            throw new IllegalStateException("Two conflicting shared meshes for " + node);
                        }
                        materials.add(geom.getMaterial());
                    }
                } else {
                    Mesh mesh = geom.getMesh();
                    if (mesh.isAnimated()) {
                        targets.add(mesh);
                        materials.add(geom.getMaterial());
                    }
                }
            } else if (child instanceof Node) {
                findTargets((Node) child, targets, materials);
            }
        }

        if (sharedMesh != null) {
            targets.add(sharedMesh);
        }
    }
}
