/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.scene.Spatial;
import name.huliqing.fighter.loader.SkyLoader;

/**
 * 天空盒模型
 * @author huliqing
 * @param <T>
 */
public class SkyEnv <T extends ModelEnvData> extends ModelEnv<T> {
    
    @Override
    public Spatial loadModel() {
        Spatial sky = SkyLoader.loadDefaultMin();
        return sky;
    }
    
}
