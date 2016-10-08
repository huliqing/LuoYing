/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.env;

import name.huliqing.ly.data.env.ModelEnvData;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;
import name.huliqing.ly.shape.TreeCollisionMesh;

/**
 * 环境物体树，这个处理器为树模型定制一个更简单的物理包围盒。来简化物体碰撞
 * 计算。
 * @author huliqing
 */
public class TreeEnv extends PlantEnv<ModelEnvData> {
    
    @Override
    protected void addPhysicsControl(Spatial spatial, RigidBodyControl control) {
        RigidBodyControl rbc = new RigidBodyControl(new MeshCollisionShape(new TreeCollisionMesh()), data.getMass());
        super.addPhysicsControl(spatial, rbc);
    }
    
}
