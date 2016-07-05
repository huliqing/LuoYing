/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.app.Application;
import com.jme3.scene.Spatial;
import name.huliqing.fighter.loader.SkyLoader;
import name.huliqing.fighter.object.scene.Scene;

/**
 * 天空盒模型
 * @author huliqing
 * @param <T>
 */
public class SkyEnv <T extends ModelEnvData> extends ModelEnv<T> {

    @Override
    public void initialize(Application app, Scene scene) {
        if (!loaded) {
            model = loadModel();
            loaded = true;
        }
        scene.setSky(model);
    }
    
    @Override
    public Spatial loadModel() {
        Spatial sky = SkyLoader.loadDefaultMin();
        return sky;
    }
    
}
