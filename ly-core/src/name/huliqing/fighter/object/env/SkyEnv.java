/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.scene.Spatial;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.loader.SkyLoader;

/**
 * 天空盒模型
 * @author huliqing
 */
public class SkyEnv extends Env<EnvData> {
    
    @Override
    public Spatial load() {
        Spatial sky = SkyLoader.loadDefaultMin();
        return sky;
    }
    
}
