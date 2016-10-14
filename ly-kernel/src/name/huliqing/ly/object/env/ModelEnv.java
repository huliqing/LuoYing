/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.env;

import com.jme3.scene.Spatial;
import name.huliqing.ly.object.AssetLoader;
import name.huliqing.ly.object.entity.ModelEntity;

/**
 * 普通模型类环境物体
 * @author huliqing
 */
public class ModelEnv extends ModelEntity {

    @Override
    protected Spatial loadModel() {
        return AssetLoader.loadModel(data.getAsString("file"));
    }
    
}
