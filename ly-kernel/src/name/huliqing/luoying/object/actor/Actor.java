/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.actor;

import com.jme3.scene.Spatial;
import name.huliqing.luoying.data.ActorData;
import name.huliqing.luoying.object.entity.ModelEntity;

/**
 * 角色，角色由数据(ObjectData)和模块处理器(Module)组成。
 * @author huliqing
 * @param <T>
 */
public class Actor<T extends ActorData> extends ModelEntity<T> {

    @Override
    public void setData(T data) {
        super.setData(data); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T getData() {
        return super.getData(); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * 载入基本模型
     * @return 
     */
    @Override
    protected Spatial loadModel() {
        return ActorModelLoader.loadActorModel(this);
    }
    
}
