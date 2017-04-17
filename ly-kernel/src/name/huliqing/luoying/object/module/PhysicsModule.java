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
package name.huliqing.luoying.object.module;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.data.PhysicsShapeData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.physicsshape.PhysicsShape;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 物理模块，给实体添加物理控制器，这样就可以让物体具有物理碰撞功能。
 * @author huliqing
 */
public class PhysicsModule extends AbstractModule {
    private static final Logger LOG = Logger.getLogger(PhysicsModule.class.getName());
    private RigidBodyControl control;
    
    private float mass;
    private float friction = 0.5f;
    private float restitution;
    private boolean kinematic;
    
    private float angularDamping;
    private float angularFactor = 1;
    private float angularSleepingThreshold = 1.0f;
    private Vector3f angularVelocity;
    private boolean applyPhysicsLocal;
    private float ccdMotionThreshold;
    private float ccdSweptSphereRadius;
    private int collideWithGroups = 1;
    private int collisionGroup = 1;
    private boolean kinematicSpatial = true;
    private float linearDamping;
    private Vector3f linearFactor;
    private float linearSleepingThreshold = 0.8f;
    private Vector3f linearVelocity;
    
    private PhysicsShapeData physicsShapeData;
    
    @Override
    public void setData(ModuleData data) {
        super.setData(data); 
        mass = data.getAsFloat("mass", mass);
        friction = data.getAsFloat("friction", friction);
        restitution = data.getAsFloat("restitution", restitution);
        kinematic = data.getAsBoolean("kinematic", kinematic);
        
        angularDamping = data.getAsFloat("angularDamping", angularDamping);
        angularFactor = data.getAsFloat("angularFactor", angularFactor);
        angularSleepingThreshold = data.getAsFloat("angularSleepingThreshold", angularSleepingThreshold);
        angularVelocity = data.getAsVector3f("angularVelocity");
        applyPhysicsLocal = data.getAsBoolean("applyPhysicsLocal", applyPhysicsLocal);
        ccdMotionThreshold = data.getAsFloat("ccdMotionThreshold", ccdMotionThreshold);
        ccdSweptSphereRadius = data.getAsFloat("ccdSweptSphereRadius", ccdSweptSphereRadius);
        collideWithGroups = data.getAsInteger("collideWithGroups", collideWithGroups);
        collisionGroup = data.getAsInteger("collisionGroup", collisionGroup);
        kinematicSpatial = data.getAsBoolean("kinematicSpatial", kinematicSpatial);
        linearDamping = data.getAsFloat("linearDamping", linearDamping);
        linearSleepingThreshold = data.getAsFloat("linearSleepingThreshold", linearSleepingThreshold);
        linearFactor = data.getAsVector3f("linearFactor");
        linearVelocity = data.getAsVector3f("linearVelocity");
        
        physicsShapeData = data.getAsObjectData("physicsShape");
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        if (control != null) {
            // 注：control可能会被外部引用并更改，所以这里要更新一下
            data.setAttribute("mass", control.getMass());
            data.setAttribute("friction", control.getFriction());
            data.setAttribute("restitution", control.getRestitution());
            data.setAttribute("kinematic", control.isKinematic());
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        Spatial spatial = entity.getSpatial();
        if (spatial == null) {
            return;
        }
        if (spatial.getControl(RigidBodyControl.class) != null) {
            LOG.log(Level.WARNING, "Entity already exists an RigidBodyControl, could not add PhysicsModule, entityId={0}, physicsModuleId={1}"
                    , new Object[] {entity.getData().getId(), data.getId()});
            return;
        }
        
        if (physicsShapeData != null) {
            PhysicsShape ps = Loader.load(physicsShapeData);
            CollisionShape csMesh = ps.getCollisionShape(spatial);
            // MeshCollisionShape类型的碰撞网格只能用于静态物体
            if ((csMesh instanceof MeshCollisionShape) && mass != 0) {
                LOG.log(Level.WARNING, "Dynamic rigidbody can not have mesh collision shape! Now use default CollisionShape instead!");
                control = new RigidBodyControl(mass);
            } else {
                control = new RigidBodyControl(ps.getCollisionShape(spatial), mass);
            }
        } else {
            control = new RigidBodyControl(mass);
        }
        spatial.addControl(control);
        control.setEnabled(isEnabled());
        control.setFriction(friction);
        control.setRestitution(restitution);
        control.setKinematic(kinematic);
        
        control.setAngularDamping(angularDamping);
        control.setAngularFactor(angularFactor);
        control.setAngularSleepingThreshold(angularSleepingThreshold);
        if (angularVelocity != null) {
            control.setAngularVelocity(angularVelocity);
        }
        control.setApplyPhysicsLocal(applyPhysicsLocal);
        control.setCcdMotionThreshold(ccdMotionThreshold);
        control.setCcdSweptSphereRadius(ccdSweptSphereRadius);
        control.setCollideWithGroups(collideWithGroups);
        control.setCollisionGroup(collisionGroup);
        control.setKinematicSpatial(kinematicSpatial);
        control.setLinearDamping(linearDamping);
        if (linearFactor != null) {
            control.setLinearFactor(linearFactor);
        }
        control.setLinearSleepingThreshold(linearSleepingThreshold);
        if (linearVelocity != null) {
            control.setLinearVelocity(linearVelocity);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        control.setEnabled(enabled);
    }

    @Override
    public void cleanup() {
        if (control != null) {
            PhysicsSpace ps = control.getPhysicsSpace();
            if (ps != null) {
                ps.remove(entity.getSpatial());
            }
            entity.getSpatial().removeControl(control);
        }
        super.cleanup(); 
    }

    @Override
    public boolean handleDataAdd(ObjectData hData, int amount) {
        return false;
    }

    @Override
    public boolean handleDataRemove(ObjectData hData, int amount) {
        return false;
    }

    @Override
    public boolean handleDataUse(ObjectData hData) {
        return false;
    }
}
