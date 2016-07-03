/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.utils;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * 射线工具类
 * @author huliqing
 */
public class RayUtils {
    
    /**
     * 检测碰撞物
     * @param origin 射线原点
     * @param direction 射线方向
     * @param root 用于检测碰撞的根节点
     * @param store 存放结果
     * @deprecated 不再使用，可能存在BUG。
     * @return 
     */
    public static CollisionResults collideWith(Vector3f origin, Vector3f direction
            , Spatial root, CollisionResults store) {
        Temp temp = Temp.get();
        temp.vec1.set(direction);
        temp.ray.setOrigin(origin);
        temp.ray.setDirection(temp.vec1.normalizeLocal());
        root.collideWith(temp.ray, store);
        
        temp.release();
        return store;
    }
}
