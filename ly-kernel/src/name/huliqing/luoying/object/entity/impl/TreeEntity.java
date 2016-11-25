/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity.impl;

import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import name.huliqing.luoying.shape.TreeCollisionMesh;

/**
 * 环境物体树，这个处理器为树模型定制一个更简单的物理包围盒。来简化物体碰撞
 * 计算。
 * @author huliqing
 */
public class TreeEntity extends PlantEntity {

    @Override
    public void initEntity() {
        super.initEntity();
        // 树体用了一个定制的MeshCollisionShape, 注：MeshCollisionShape类型必须要求mass=0
        RigidBodyControl rbc = spatial.getControl(RigidBodyControl.class);
        if (rbc != null) {
            rbc.setMass(0);
            rbc.setCollisionShape(new MeshCollisionShape(new TreeCollisionMesh()));
        }
    }
    
}
