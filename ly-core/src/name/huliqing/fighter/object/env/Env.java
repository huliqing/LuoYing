/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.app.Application;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.object.DataProcessor;
import name.huliqing.fighter.object.scene.Scene;

/**
 * @author huliqing
 * @param <T>
 */
public abstract class Env<T extends EnvData> implements DataProcessor<T>{
    
    protected T data;

    @Override
    public void initData(T data) {
        this.data = data;
    }

    @Override
    public T getData() {
//        data.setLocation(model.getLocalTranslation());
//        data.setRotation(model.getLocalRotation());
//        data.setScale(model.getLocalScale());
        return data;
    }
    
    public abstract void initialize(Application app, Scene scene);
    
    public void cleanup() {
        // ignore
    }
    
}
