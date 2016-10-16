/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object;

import com.jme3.scene.Spatial;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.utils.GeometryUtils;

/**
 * 这个类只允许在loader包内调用，在其它包的类不要直接调用该类（除测试用之外）。
 * @author huliqing
 */
public class AssetLoader {
//    private static final Logger LOG = Logger.getLogger(AssetLoader.class.getName());
    
    /**
     * 载入模型，但是会根据系统是否使用light进行makeUnshaded
     * @param file
     * @return 
     */
    public static Spatial loadModel(String file) {
        return LuoYing.getAssetManager().loadModel(file);
    }
    
    /**
     * 载入模型，这个方法载入模型时会偿试把模型的材质替换为unshaded的。
     * @param file
     * @return 
     */
    public static Spatial loadModelUnshaded(String file) {
        Spatial model = LuoYing.getAssetManager().loadModel(file);
        GeometryUtils.makeUnshaded(model);
        return model;
    }
    
}
