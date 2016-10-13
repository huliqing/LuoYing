/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.env;

import com.jme3.scene.Spatial;
import name.huliqing.ly.data.ModelEntityData;
import name.huliqing.ly.object.entity.ModelEntity;

/**
 * 环境类物体
 * @author huliqing
 * @param <T>
 */
public class ModelEnv<T extends ModelEntityData> extends ModelEntity<T> {

    @Override
    public void setData(T data) {
        super.setData(data);
        
        
    }

    @Override
    public T getData() {
        return super.getData();
    }
    
    @Override
    protected Spatial loadModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
