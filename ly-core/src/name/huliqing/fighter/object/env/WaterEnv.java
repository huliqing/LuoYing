/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.scene.Spatial;
import name.huliqing.fighter.data.EnvData;

/**
 *
 * @author huliqing
 */
public class WaterEnv  extends Env<EnvData> {

    @Override
    protected Spatial load() {
        Spatial spatial = super.load();
        return spatial;
    }
}
