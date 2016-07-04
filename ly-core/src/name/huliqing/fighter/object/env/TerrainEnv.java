/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.app.Application;
import com.jme3.scene.Spatial;
import name.huliqing.fighter.object.scene.Scene;

/**
 * 地面模型。
 * @author huliqing
 * @param <T>
 */
public class TerrainEnv<T extends ModelEnvData> extends ModelEnv<T> {

    @Override
    public void initialize(Application app, Scene scene) {
        super.initialize(app, scene);
    }
    
    @Override
    public Spatial loadModel() {
        Spatial spatial = super.loadModel();
        
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
        
        return spatial;
    }
    
}
