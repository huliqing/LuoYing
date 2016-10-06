/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.env;

import com.jme3.app.Application;
import name.huliqing.ly.data.env.EnvData;
import name.huliqing.ly.object.scene.Scene;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class AbstractEnv<T extends EnvData> implements Env<T> {
    
    protected T data;
    protected Scene scene;
    protected boolean initialized;

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
    }
    
    /**
     * 初始化
     * @param app
     * @param scene 
     */
    @Override
    public void initialize(Application app, Scene scene) {
        this.scene = scene;
        this.initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * 当Env退出时清理资源。
     */
    @Override
    public void cleanup() {
        initialized = false;
    }
}
