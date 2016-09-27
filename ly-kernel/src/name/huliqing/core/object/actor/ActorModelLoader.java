/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actor;

import com.jme3.animation.SkeletonControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Config;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ActorData;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.mvc.service.ConfigService;
import name.huliqing.core.object.AssetLoader;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.effect.Effect;
import name.huliqing.core.utils.GeometryUtils;

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
        actorModel.setUserData(ObjectData.USER_DATA, data);
        actorModel.setShadowMode(RenderQueue.ShadowMode.Cast);
        
        // 4.====create character control
        
        // remove20160927
        // 4.1 注缩放必须放在碰撞盒加入之前，因为碰撞盒不能跟着缩放
//        int scaleLen = data.getProto().checkAttributeLength("scale");
//        if (scaleLen >= 3) {
//            actorModel.setLocalScale(data.getAsVector3f("scale"));
//        } else if (scaleLen == 1) {
//            float s = data.getAsFloat("scale");
//            actorModel.setLocalScale(new Vector3f(s,s,s));
//        }

        // 4.1 注缩放必须放在碰撞盒加入之前，因为碰撞盒不能跟着缩放
        Vector3f scale = data.getScale();
        if (scale != null) {
            actorModel.setLocalScale(scale);
        }
        
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
//        
//        // 4.3 视角和初始正视角方向
//        Vector3f forward = data.getAsVector3f("localForward");
//        if (forward != null) {
//            actor.setLocalForward(forward);
//        }
//        
//        Vector3f viewDirection = data.getAsVector3f("viewDirection");
//        if (viewDirection != null) {
//            actor.setViewDirection(viewDirection);
//        }
        
        // 6.==== 绑定特效
        String[] effects = data.getAsArray("effects");
        if (effects != null) {
            for (String eid : effects) {
                Effect ae = Loader.loadEffect(eid);
                ((Node) actorModel).attachChild(ae);
            }
        }

        // 14.偿试激活HardwareSkining
        checkEnableHardwareSkining(actor, actorModel);
        
        return actorModel;
    }
    
    
    /**
     * 载入扩展的动画,该方法从角色所配置的extAnim目录中查找动画文件并进行加
     * 载。
     * @param actor
     * @param animName
     * @return 
     */
    public static boolean loadExtAnim(Actor actor, String animName) {
//        String animDir = actor.getData().getAsString("extAnim");
        String animDir = actor.getData().getExtAnim();
        if (animDir == null) {
            LOG.log(Level.WARNING, "Actor {0} no have a extAnim defined"
                    + ", could not load anim {1}", new Object[] {actor.getData().getId(), animName});
            return false;
        }
        String animFile = animDir + "/" + animName + ".mesh.j3o";
        try {
            Spatial animExtModel = AssetLoader.loadModelDirect(animFile);
            GeometryUtils.addSkeletonAnim(animExtModel, actor.getSpatial());
            return true;
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Could not load extAnim, actor={0}, animName={1}, exception={2}"
                    , new Object[] {actor.getData().getId(), animName, e.getMessage()});
        }
        return false;
    }
    
    /**
     * 检测并判断是否打开或关闭该模型的硬件skining加速
     * @param actor 角色模型
     */
    private static void checkEnableHardwareSkining(Actor actor, Spatial actorModel) {
        ActorData data = actor.getData();
        SkeletonControl sc = actorModel.getControl(SkeletonControl.class);
        
        if (data == null || sc == null) {
            return;
        }
        
        // 全局没有打开的情况下则不处理。
        if (!Factory.get(ConfigService.class).isUseHardwareSkinning()) {
            return;
        }

        // 默认情冲下打开hardwareSkinning,除非在actor.xml中设置不打开。
        if (!data.isHardwareSkinning()) {
            return;
        }
        
        // 代换自定义的SkeletonControl,因为默认的SkeletonControl会把带
        // SkeletonControl的子节点也进行处理。比如弓武器，当弓武器带有动画时可能
        // 导致角色的SkeletonControl和弓的SkeletonControl存在冲突导致弓模型变形
        CustomSkeletonControl csc = new CustomSkeletonControl(sc.getSkeleton());
        actorModel.removeControl(sc);
        actorModel.addControl(csc);
        csc.setHardwareSkinningPreferred(true);
    }
    
}
