/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.env;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.ly.LuoYing;
import name.huliqing.ly.object.entity.ModelEntity;
import name.huliqing.ly.object.entity.TerrainEntity;

/**
 * 地形环境
 * @author huliqing
 */
public class TerrainEnv extends ModelEntity implements TerrainEntity {
    
    // 射线用于获取地面高度
    private final Ray ray = new Ray();
    private final CollisionResults results = new CollisionResults();
    
    @Override
    public Spatial loadModel() {
        
        // remove20160602,JME3.1之后这个BUG已经修复
//        // TerrainLodControl的性能问题很严重，会在内存中常驻一个守护线程。
//        // 每次载入带有该Control的model时都会产生一个新的线程，该问题在桌面电脑
//        // 上不明显，但是在手机上多试几次就会发生内存溢出漰溃！
//        // 目前两个处理方法：
//        // 1.scene在载入后缓存，这样不会每次都创建lodControl
//        // 2.scene不缓存的情况要则要把TerrainLodControl移除。
//        TerrainLodControl lod = spatial.getControl(TerrainLodControl.class);
//        if (lod != null) {
//            spatial.removeControl(lod);
//        }
        
        return LuoYing.getAssetManager().loadModel(data.getAsString("file"));
    }

    @Override
    public Vector3f getHeight(float x, float z) {
        ray.origin.set(x, Float.MAX_VALUE, z);
        ray.direction.set(0,-1,0);
        results.clear();
        model.collideWith(ray, results);
        if (results.size() <= 0) {
            return null;
        }
        return results.getClosestCollision().getContactPoint();
    }
    
}
