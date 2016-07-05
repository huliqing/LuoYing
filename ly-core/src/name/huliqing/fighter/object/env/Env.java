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
        return data;
    }
    
    /**
     * 初始化
     * @param app
     * @param scene 
     */
    public abstract void initialize(Application app, Scene scene);
    
    /**
     * 当Env退出时清理资源。
     */
    public void cleanup() {}
    
}
