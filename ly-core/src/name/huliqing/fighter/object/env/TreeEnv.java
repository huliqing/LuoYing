/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.shape.TreeCollisionMesh;

/**
 * 环境物体树，这个处理器为树模型定制一个更简单的物理包围盒。来简化物体碰撞
 * 计算。
 * @author huliqing
 */
public class TreeEnv extends Env<EnvData> {

    @Override
    protected void addPhysicsControl(Spatial spatial, RigidBodyControl control, EnvData data) {
        RigidBodyControl rbc = new RigidBodyControl(new MeshCollisionShape(new TreeCollisionMesh()), data.getMass());
        super.addPhysicsControl(spatial, rbc, data);
    }
    
}
