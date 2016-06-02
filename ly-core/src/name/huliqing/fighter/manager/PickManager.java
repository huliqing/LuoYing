/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.manager;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import name.huliqing.fighter.data.ProtoData;

/**
 * 鼠标选择工具类
 * @author huliqing
 */
public class PickManager {
    
    public static class PickResult {
        public Spatial spatial;
        public CollisionResult result;
    }
    
    /**
     * 找出最接近的,可被选择的对象.如果最接近的对象不能被选择,则返回null.
     * @param inputManager (not null)
     * @param camera (not null)
     * @param root (not null)
     * @param results 存放结果
     */
    public static PickResult pick(InputManager inputManager, Camera camera, Spatial root) {
        Vector2f v2d = inputManager.getCursorPosition();
        Vector3f click3d = camera.getWorldCoordinates(v2d, 0);
        // 注意DIR方向向量必须归一化，否则可能在collideWith的时候获取不到结果。
        // 实测：没归一化时与ray产生不到碰撞结果。
        Vector3f dir = camera.getWorldCoordinates(v2d, 1).subtract(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);
  
        CollisionResults crs = new CollisionResults();
        root.collideWith(ray, crs);
        
        for (int i = 0; i < crs.size(); i++) {
            CollisionResult cr = crs.getCollision(i);
            Spatial target = findPickable(cr.getGeometry());
            
            if (target != null) {
                PickResult result = new PickResult();
                result.spatial = target;
                result.result = cr;
                return result;
            }
        }
        return null;
    }
    
    private static Spatial findPickable(Spatial s) {
        if (s == null) {
            return null;
        }
            
        // 可被选择的对象.
        ProtoData od = s.getUserData(ProtoData.USER_DATA);
        if (od != null && od.isPickable()) {
            return s;
        } else {
            return findPickable(s.getParent());
        }
    }
   
}
