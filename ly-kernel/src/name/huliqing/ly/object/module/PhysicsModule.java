/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.module;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.ly.data.ModuleData;
import name.huliqing.ly.object.entity.Entity;

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
            entity.getSpatial().removeControl(control);
        }
        super.cleanup(); 
    }
    
}
