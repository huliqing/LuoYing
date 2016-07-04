/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.app.Application;
import com.jme3.scene.Spatial;
import name.huliqing.fighter.object.scene.Scene;
import name.huliqing.fighter.utils.GeometryUtils;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class PlantEnv<T extends ModelEnvData> extends ModelEnv<T>{

    private Scene scene;
    
    @Override
    public void initialize(Application app, Scene scene) {
        this.scene = scene;
        super.initialize(app, scene); 
    }

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
