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
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.UserData;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Config;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.mvc.service.ConfigService;
import name.huliqing.core.loader.AssetLoader;
import name.huliqing.core.object.actor.Actor;

/**
 * 
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractSkin<T extends SkinData> implements Skin<T> {
    private final ConfigService configService = Factory.get(ConfigService.class);
    
    protected T data;
    
    private String bindBone;
    // skin的本地模型初始变换，注：localRotation需要使用Quaternion.fromAngle转换成Quaternion.
    private Vector3f localTranslation;
    private float[] localRotation;
    private Vector3f localScale;

    @Override
    public void setData(T data) {
        this.data = data;
        bindBone = data.getAttribute("bindBone");
        localTranslation = data.getAsVector3f("localTranslation");
        localRotation = data.getAsFloatArray("localRotation");
        localScale = data.getAsVector3f("localScale");
    }

    @Override
    public T getData() {
        return data;
    }

    protected Spatial loadSkinNode(String file) {
        Spatial skinNode = AssetLoader.loadModel(file);
        skinNode.setUserData(ObjectData.USER_DATA, data);

        // 由于一些武器（如：弓）可能自身包含动画，即包含SkeletonControl,而
        // 这些节点在CustomSkeletonControl中被排除（避免冲突），因而在这里需要
        // 自已打开该功能。
        if (configService.isUseHardwareSkinning() && skinNode.getControl(SkeletonControl.class) != null) {
            skinNode.getControl(SkeletonControl.class).setHardwareSkinningPreferred(true);
        }
        return skinNode;
    }
    
    @Override
    public void attach(Actor actor) {
        Spatial actorSpatial = (Spatial) actor.getModel();
        if (!(actorSpatial instanceof Node)) {
            Logger.getLogger(OutfitSkin.class.getName()).log(Level.WARNING
                    , "actorSpatial is not a Node, could not attach skins, actorId={0}, skinId={1}"
                    , new Object[] {actor.getData().getId(), data.getId()});
            return;
        }
        // 部分Skin可能无指定模型或者不需要模型，则只设置Using标记就可以。不需要加
        // 载模型文件，如部分模拟的武器类型。
        if (data.getFile() == null) {
            return;
        }
        
        // 重要：当添加模型时一定要把skinData设置上去,这是一个重要引用。
        Node actorNode = (Node) actorSpatial;
        
        // 首先把挂靠着的武器移除
        Spatial skinNode = findSkinNodes(actor.getModel(), data);
        if (skinNode == null) {
            skinNode = AssetLoader.loadModel(data.getFile());
            skinNode.setUserData(ObjectData.USER_DATA, data);
            
            // remove20160226,移到了下面
//            // 由于一些武器（如：弓）可能自身包含动画，即包含SkeletonControl,而
//            // 这些节点在CustomSkeletonControl中被排除（避免冲突），因而在这里需要
//            // 自已打开该功能。
//            if (data.isWeapon() && skinNode.getControl(SkeletonControl.class) != null) {
//                skinNode.getControl(SkeletonControl.class).setHardwareSkinningPreferred(true);
//            }
        }
        
        // 由于一些武器（如：弓）可能自身包含动画，即包含SkeletonControl,而
        // 这些节点在CustomSkeletonControl中被排除（避免冲突），因而在这里需要
        // 自已打开该功能。
        SkeletonControl skinSC = skinNode.getControl(SkeletonControl.class);
        if (data.isWeapon() && skinSC != null) {
            if (!skinSC.isHardwareSkinningPreferred()) {
                skinSC.setHardwareSkinningPreferred(true);
            }
        }
        
        // 如果指定了骨头，则将skin绑定到目标骨头
        if (bindBone != null) {
            SkeletonControl actorSC = actorNode.getControl(SkeletonControl.class);
            Node boneNode = actorSC.getAttachmentsNode(bindBone);
            
            // 如果没有指定本地变换，则直接从bone中获取
            Bone bone = actorSC.getSkeleton().getBone(bindBone);
            if (localRotation == null) {
                localRotation = bone.getWorldBindInverseRotation().toAngles(localRotation);
            }
            if (localScale == null) {
                localScale = bone.getWorldBindInverseScale();
            }
            // 因为大部分情况下Skin并不是以原点（0,0,0)作为模型的中心点，而是以模型
            // 的其中某一个位置，通常这个位置刚好是被绑定的骨头的位置，当模型attach到骨头
            // 位置时由于受到骨头的初始位置，旋转，缩放的影响，这个时候有必要把
            // 该点重新移到骨头所在的位置处。下面默认以被绑定的骨骼点作为模型原始点
            // 进行处理。
            if (localTranslation == null) {
                // 骨骼点的位置
                localTranslation = bone.getWorldBindInversePosition().negate();
                // 被缩放后的位置
                bone.getWorldBindInverseScale().mult(localTranslation, localTranslation);
                // 被旋转后的位置
                bone.getWorldBindInverseRotation().mult(localTranslation, localTranslation);
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
            actorNode.attachChild(skinNode);
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

    @Override
    public void detach(Actor actor) {
        Spatial sn = findSkinNodes(actor.getModel(), data);
        if (sn != null) {
            sn.removeFromParent();
            SkinData sd = sn.getUserData(ObjectData.USER_DATA);
            sd.setUsing(false);
        }
        
        // 部分模型Skin可能无实际模型所以这里仍需要确保把data设为false
        data.setUsing(false);
    }
    
    /**
     * 从角色身上获取指定skinData的模型.
     * @param actorModel 指定的角色
     * @param skinData 指定的skinData
     * @return 
     */
    protected Spatial findSkinNodes(Spatial actorModel, SkinData skinData) {
        SkinFinder finder = new SkinFinder(skinData);
        actorModel.breadthFirstTraversal(finder);
        return finder.getResult();
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
            ObjectData pd = spatial.getUserData(ObjectData.USER_DATA);
            if (pd != null && pd == targetSkinData) {
                skinNode = spatial;
            }
        }
        
        public Spatial getResult() {
            return skinNode;
        }
    }
    
    
    // 打开skin的hws,如果角色actor主SkeletonControl已经打开该功能,则skinNode必须自已打开.
    private void checkSwitchToHardware(Actor actor, Spatial skinNode) {
        // 如果hsw未打开,则不需要管,交由sc内部去一起处理就可以.
        SkeletonControl sc = actor.getModel().getControl(SkeletonControl.class);
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
        sc.setSpatial(actor.getModel());
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
