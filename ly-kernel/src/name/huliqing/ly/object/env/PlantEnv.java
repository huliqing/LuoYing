/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.env;

import name.huliqing.ly.data.env.ModelEnvData;
import com.jme3.scene.Spatial;
import name.huliqing.ly.utils.GeometryUtils;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class PlantEnv<T extends ModelEnvData> extends ModelEnv<T>{

    @Override
    protected Spatial loadModel() {
        Spatial terrain = scene.getTerrain();
        if (terrain != null) {
            GeometryUtils.getTerrainHeight(terrain, data.getLocation());
            data.getLocation().subtractLocal(0, 0.5f, 0);
        }
        return super.loadModel(); 
    }
    
}
