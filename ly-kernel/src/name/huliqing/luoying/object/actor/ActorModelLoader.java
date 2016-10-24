/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.actor;

import com.jme3.animation.SkeletonControl;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Config;
import name.huliqing.luoying.data.ActorData;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.AssetLoader;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.effect.Effect;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public class ActorModelLoader {
    private final static Logger LOG = Logger.getLogger(ActorModelLoader.class.getName());
    
    /**
     * character
     *      |- AnimControl
     *      |- SkeletonControl
     *      |- foot
     *      |- lowerBody
     *      |- upperBody
     *      |- hand
     *      |- face
     *      |- eye
     *      |- ear
     *      |- hair
     *      |- weaponLeft
     *      |- weaponRight
     * @param actor
     * @return 
     */
    public static Spatial loadActorModel(Actor actor) {
        // 0.==== Load base model : character
        ActorData data = actor.getData();
        // xxx 要移动到ActorModule中去
        String actorFile = data.getFile();
        
        if (Config.debug) {
            LOG.log(Level.INFO, "Load actor file={0}", actorFile);
        }
        
        // 需要确保最外层是Node类
        Spatial actorModel = AssetLoader.loadModel(actorFile);
        if (actorModel instanceof Geometry) {
            Spatial temp = actorModel;
            actorModel = new Node();
            ((Node) actorModel).attachChild(temp);
            // 当actor附带有effect时，必须把角色原始model设置为不透明的
            // 否则添加的效果可能会被角色model挡住在后面。(JME3.0存在该问题)
            temp.setQueueBucket(RenderQueue.Bucket.Opaque);
        }
        actorModel.setName(data.getName());
//        actorModel.setUserData(ObjectData.USER_DATA, data); // remove
        actorModel.setShadowMode(RenderQueue.ShadowMode.Cast);
        
        
        // remove20161009，xxx 重构分离
        // 4.2 碰撞盒
//        String collisionShape = data.getAttribute("collisionShape", "capsule");
//        float collisionRadius = data.getAsFloat("collisionRadius", 0.4f);
//        float collisionHeight = data.getAsFloat("collisionHeight", 2.8f);
//        float mass = data.getAsFloat("mass", 0);
//        
//        TempVars tv = TempVars.get();
//        if (collisionShape.equals("capsule")) {
//            
//            actor.setModel(actorModel, collisionRadius, collisionHeight, mass);
//            
//        } else if (collisionShape.equals("box")) {
//            
//            Vector3f boxScale = data.getAsVector3f("collisionBoxScale", new Vector3f(1, 1, 1)); // box碰撞盒的缩放
//            BoundingBox bb = (BoundingBox)actorModel.getWorldBound();
//            bb.getExtent(tv.vect1);
//            tv.vect1.multLocal(boxScale);
//            CompoundCollisionShape ccs = new CompoundCollisionShape();
//            ccs.addChildShape(new BoxCollisionShape(tv.vect1), new Vector3f(0, tv.vect1.y, 0));
//            actor.setModel(actorModel, collisionRadius, collisionHeight, mass, ccs);
//            
//        } else if (collisionShape.equals("mesh")) {
//            
//            CollisionShape cShape = CollisionShapeFactory.createMeshShape(actorModel);
//            actor.setModel(actorModel, collisionRadius, collisionHeight, mass, cShape);
//            
//        } else if (collisionShape.equals("dynamicMesh")) {
//            
//            CollisionShape cShape = CollisionShapeFactory.createDynamicMeshShape(actorModel);
//            actor.setModel(actorModel, collisionRadius, collisionHeight, mass, cShape);
//            
//        } else {
//            throw new UnsupportedOperationException("Unsupported collisionShape=" + collisionShape);
//        }
//        tv.release();
        
        // 6.==== 绑定特效
        String[] effects = data.getAsArray("effects");
        if (effects != null) {
            for (String eid : effects) {
                Effect ae = Loader.load(eid);
                ((Node) actorModel).attachChild(ae.getSpatial());
            }
        }

        // 14.偿试激活HardwareSkining
        checkEnableHardwareSkining(actor, actorModel);
        return actorModel;
    }
    
//    /**
//     * 载入扩展的动画,该方法从角色所配置的extAnim目录中查找动画文件并进行加
//     * 载。
//     * @param actor
//     * @param animName
//     * @return 
//     */
//    public static boolean loadExtAnim(Actor actor, String animName) {
//        // xxx 要移动到ActorModule中去
//        String animDir = actor.getData().getExtAnim();
//        
//        if (animDir == null) {
//            LOG.log(Level.WARNING, "Entity {0} no have a extAnim defined"
//                    + ", could not load anim {1}", new Object[] {actor.getData().getId(), animName});
//            return false;
//        }
//        String animFile = animDir + "/" + animName + ".mesh.j3o";
//        try {
//            Spatial animExtModel = AssetLoader.loadModelDirect(animFile);
//            GeometryUtils.addSkeletonAnim(animExtModel, actor.getSpatial());
//            return true;
//        } catch (Exception e) {
//            LOG.log(Level.WARNING, "Could not load extAnim, actor={0}, animName={1}, exception={2}"
//                    , new Object[] {actor.getData().getId(), animName, e.getMessage()});
//        }
//        return false;
//    }
    
    /**
     * 检测并判断是否打开或关闭该模型的硬件skining加速
     * @param actor 角色模型
     */
    private static void checkEnableHardwareSkining(Entity actor, Spatial actorModel) {
        EntityData data = actor.getData();
        SkeletonControl sc = actorModel.getControl(SkeletonControl.class);
        
        if (data == null || sc == null) {
            return;
        }
        
//        // 全局没有打开的情况下则不处理。
//        if (!Factory.get(ConfigService.class).isUseHardwareSkinning()) {
//            return;
//        }

        // remove20161010,以后默认开启
//        // 默认情冲下打开hardwareSkinning,除非在actor.xml中设置不打开。
//        if (!data.isHardwareSkinning()) {
//            return;
//        }
        
        // 代换自定义的SkeletonControl,因为默认的SkeletonControl会把带
        // SkeletonControl的子节点也进行处理。比如弓武器，当弓武器带有动画时可能
        // 导致角色的SkeletonControl和弓的SkeletonControl存在冲突导致弓模型变形
        CustomSkeletonControl csc = new CustomSkeletonControl(sc.getSkeleton());
        actorModel.removeControl(sc);
        actorModel.addControl(csc);
        csc.setHardwareSkinningPreferred(true);
    }
    
}
