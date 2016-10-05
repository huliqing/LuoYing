/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object;

import com.jme3.scene.Spatial;
import java.util.logging.Logger;
import name.huliqing.ly.LY;
import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.service.ConfigService;
import name.huliqing.ly.utils.GeometryUtils;

/**
 * 这个类只允许在loader包内调用，在其它包的类不要直接调用该类（除测试用之外）。
 * @author huliqing
 */
public class AssetLoader {
    private static final Logger LOG = Logger.getLogger(AssetLoader.class.getName());
    
    /**
     * 载入模型，但是会根据系统是否使用light进行makeUnshaded
     * @param file
     * @return 
     */
    public static Spatial loadModel(String file) {
        ConfigService configService = Factory.get(ConfigService.class);
        Spatial model = LY.getAssetManager().loadModel(file);
        if (!configService.isUseLight()) {
            GeometryUtils.makeUnshaded(model);
        }
        return model;
    }
    
    /**
     * 载入模型，这个方法载入模型时会偿试把模型的材质替换为unshaded的。
     * @param file
     * @return 
     */
    public static Spatial loadModelUnshaded(String file) {
        Spatial model = LY.getAssetManager().loadModel(file);
        GeometryUtils.makeUnshaded(model);
        return model;
    }
    
    /**
     * 直接载入模型，不修改任何属性
     * @param file
     * @return 
     */
    public static Spatial loadModelDirect(String file) {
        return LY.getAssetManager().loadModel(file);
    }
}
