/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.entity;

import com.jme3.scene.Spatial;
import name.huliqing.ly.data.ModelEntityData;
import name.huliqing.ly.object.scene.Scene;

/**
 * 模型类的场景物体.
 * @author huliqing
 * @param <T>
 */
public abstract class ModelEntity<T extends ModelEntityData> extends AbstractEntity<T> {
    
    protected Spatial model;

    @Override
    public void setData(T data) {
        super.setData(data); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T getData() {
        return super.getData(); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void updateDatas() {
        if (model != null) {
            data.setLocation(model.getLocalTranslation());
            data.setRotation(model.getLocalRotation());
            data.setScale(model.getLocalScale());
        }
    }
    
    @Override
    public void initialize(Scene scene) {
        super.initialize(scene);
        model = loadModel();
        model.setLocalTranslation(data.getLocation());
        model.setLocalRotation(data.getRotation());
        model.setLocalScale(data.getScale());
    }
    
    @Override
    public Spatial getSpatial() {
        return model;
    }
    
    /**
     * 载入模型
     * @return 
     */
    protected abstract Spatial loadModel();
}
