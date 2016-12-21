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
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 物理模块，给物体添加物理控制器
 * @author huliqing
 */
public class PhysicsModule extends AbstractModule {
    private static final Logger LOG = Logger.getLogger(PhysicsModule.class.getName());
    private RigidBodyControl control;
    
    @Override
    public void setData(ModuleData data) {
        super.setData(data); 
    }
    
    @Override
    public void updateDatas() {
        if (control != null) {
            // 注：control可能会被外部引用并更改，所以这里要更新一下。see TreeEnv or PlantEnv
            data.setAttribute("mass", control.getMass());
            data.setAttribute("friction", control.getFriction());
            data.setAttribute("restitution", control.getRestitution());
            data.setAttribute("kinematic", control.isKinematic());
        }
    }

    @Override
    public void initialize(Entity entity) {
        super.initialize(entity);
        Spatial spatial = entity.getSpatial();
        if (spatial == null) {
            return;
        }
        if (spatial.getControl(RigidBodyControl.class) != null) {
            LOG.log(Level.WARNING, "Entity already exists an RigidBodyControl, could not add PhysicsModule, entityId={0}, physicsModuleId={1}"
                    , new Object[] {entity.getData().getId(), data.getId()});
            return;
        }
        
        control = new RigidBodyControl(data.getAsFloat("mass", 0));
        spatial.addControl(control);
        control.setFriction(data.getAsFloat("friction", 0));
        control.setRestitution(data.getAsFloat("restitution", 0));
        control.setKinematic(data.getAsBoolean("kinematic", false));
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
    
}
