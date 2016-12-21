/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.object.actor;

import com.jme3.animation.SkeletonControl;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Config;
import name.huliqing.luoying.data.ActorData;
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
            
            // remove20161207
//            // 当actor附带有effect时，必须把角色原始model设置为不透明的
//            // 否则添加的效果可能会被角色model挡住在后面。(JME3.0存在该问题)
//            temp.setQueueBucket(RenderQueue.Bucket.Opaque);
        }
        
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
    
    /**
     * 检测并判断是否打开或关闭该模型的硬件skining加速
     * @param actor 角色模型
     */
    private static void checkEnableHardwareSkining(Entity actor, Spatial actorModel) {
        SkeletonControl sc = actorModel.getControl(SkeletonControl.class);
        if (sc == null) {
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
