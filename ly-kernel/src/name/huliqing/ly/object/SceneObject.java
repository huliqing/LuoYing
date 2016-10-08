/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import name.huliqing.ly.data.ObjectData;
import name.huliqing.ly.object.scene.Scene;
import name.huliqing.ly.xml.DataProcessor;

/**
 * 场景类物体，一般来说，是指可以存放于场景中、有形或者无形的对象都可以作为物体继承自这个类。
 * 如：动物，人物，特效，子弹，花草树木，声音，。。
 * @author huliqing
 * @param <T>
 */
public interface SceneObject<T extends ObjectData> extends DataProcessor<T>{

    @Override
    public void setData(T data);

    @Override
    public T getData();
    
    @Override
    public void updateDatas();

    /**
     * 初始化
     * @param scene 场景容器
     */
    void initialize(Scene scene);
    
    /**
     * 判断是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理并释放资源
     */
    void cleanup();
    
    /**
     * 获取物体的唯一id, 通过这个id可以从scene中直接找到这个物体
     * @return 
     */
    long getObjectId();
    
    /**
     * 获取物体在场景中的位置
     * @return 
     */
    Vector3f getLocation();
    
    /**
     * 设置物体在场景中的位置
     * @param location 
     */
    void setLocation(Vector3f location);
    
    /**
     * 获取物体在场景中的旋转变换
     * @return 
     */
    Quaternion getRotation();
    
    /**
     * 设置物体在场景中的旋转变换
     * @param rotation 
     */
    void setRotation(Quaternion rotation);
    
    /**
     * 获取物体在场景中的缩放
     * @return 
     */
    Vector3f getScale();
    
    /**
     * 设置物体在场景中的缩放
     * @param scale 
     */
    void setScale(Vector3f scale);
    
}
