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
public interface Env<T extends EnvData> extends DataProcessor<T>{
    
    /**
     * 获取Data
     * @return 
     */
    @Override
    T getData();
    
    /**
     * 初始化Data.该方法只允许DataFactory调用,而且一般应该只设置一次，运行
     * 时不应该再调用。
     * @param data 
     */
    @Override
    void initData(T data);
    
    /**
     * 初始化Env
     * @param app
     * @param scene 
     */
    void initialize(Application app, Scene scene);
    
    /**
     * 判断Env是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理并释放Env资源
     */
    void cleanup();
    
}
