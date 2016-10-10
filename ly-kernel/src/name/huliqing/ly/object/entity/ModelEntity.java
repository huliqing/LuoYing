/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.entity;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.ly.object.scene.Scene;

/**
 * 模型类的场景物体.
 * @author huliqing
 */
public abstract class ModelEntity extends AbstractEntity {
    
    private final static String DATA_LOCATION = "location";
    private final static String DATA_ROTATION= "rotation";
    private final static String DATA_SCALE = "scale";

    protected Spatial model;
    
    @Override
    public void updateDatas() {
        if (model != null) {
            data.setAttribute(DATA_LOCATION, model.getLocalTranslation());
            data.setAttribute(DATA_ROTATION, model.getLocalRotation());
            data.setAttribute(DATA_SCALE, model.getLocalScale());
        }
    }
    
    @Override
    public void initialize(Scene scene) {
        super.initialize(scene);
        model = loadModel();
        model.setLocalTranslation(data.getAsVector3f(DATA_LOCATION, Vector3f.ZERO));
        model.setLocalRotation(data.getAsQuaternion(DATA_ROTATION, Quaternion.IDENTITY));
        model.setLocalScale(data.getAsVector3f(DATA_SCALE, Vector3f.UNIT_XYZ));
    }
    
    @Override
    public Spatial getSpatial() {
        return model;
    }
    
    /**
     * 载入基本模型
     * @return 
     */
    protected abstract Spatial loadModel();
}
