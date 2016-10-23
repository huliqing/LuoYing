/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.xml.DataProcessor;
import name.huliqing.luoying.xml.ProtoData;

public class Loader {
    
    public static <T extends ProtoData> T loadData(String id) {
        return DataFactory.createData(id);
    }
    
    public static <T extends DataProcessor> T load(String id) {
        return load(DataFactory.createData(id));
    }
    
    public static <T extends DataProcessor> T load(ProtoData data) {
        T dp = DataFactory.createProcessor(data);
        if (dp instanceof Entity) {
            ((Entity) dp).initialize();
        }
        return dp;
    }
    
    public static Spatial loadModel(String file) {
        return AssetLoader.loadModel(file);
    }
    
    public static Material loadMaterial(String j3mFile) {
        AssetManager am = LuoYing.getAssetManager();
        return am.loadMaterial(j3mFile);
    }
}
