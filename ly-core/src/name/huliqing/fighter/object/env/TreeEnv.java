/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;
import name.huliqing.fighter.shape.TreeCollisionMesh;
import name.huliqing.fighter.utils.GeometryUtils;

/**
 * 环境物体树，这个处理器为树模型定制一个更简单的物理包围盒。来简化物体碰撞
 * 计算。
 * @author huliqing
 * @param <T>
 */
public class TreeEnv<T extends ModelEnvData> extends ModelEnv<T> {

    @Override
    protected Spatial loadModel() {
        Spatial terrain = scene.getTerrain();
        if (terrain != null) {
            GeometryUtils.getTerrainHeight(terrain, data.getLocation());
            data.getLocation().subtractLocal(0, 0.5f, 0);
        }
        return super.loadModel(); 
    }
    
    @Override
    protected void addPhysicsControl(Spatial spatial, RigidBodyControl control, T data) {
        RigidBodyControl rbc = new RigidBodyControl(new MeshCollisionShape(new TreeCollisionMesh()), data.getMass());
        super.addPhysicsControl(spatial, rbc, data);
    }
    
}
