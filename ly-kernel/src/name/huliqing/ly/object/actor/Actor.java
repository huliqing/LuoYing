/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.actor;

import com.jme3.scene.Spatial;
import name.huliqing.ly.object.entity.ModelEntity;

/**
 * 角色，角色由数据(ObjectData)和模块处理器(Module)组成。
 * @author huliqing
 */
public class Actor extends ModelEntity {
    
    /**
     * 载入基本模型
     * @return 
     */
    @Override
    protected Spatial loadModel() {
        return ActorModelLoader.loadActorModel(this);
    }
    
}
