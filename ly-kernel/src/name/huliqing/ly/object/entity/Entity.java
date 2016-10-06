/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.entity;

import com.jme3.scene.Spatial;
import name.huliqing.ly.data.ObjectData;
import name.huliqing.ly.xml.DataProcessor;

/**
 * 实体类物体，一般来说，是指可以存放于场景中、有形或者无形的对象都可以作为实体物体继承自这个类。
 * 如：动物，人物，特效，子弹，花草树木，声音，。。
 * @author huliqing
 * @param <T>
 */
public interface Entity<T extends ObjectData> extends DataProcessor<T>{
    
    /**
     * 初始化实体
     */
    void initialize();
    
    /**
     * 判断实体是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理实体并释放资源
     */
    void cleanup();
    
    /**
     * 获取实体的id
     * @return 
     */
    long getEntityId();
    
    /**
     * 获取实体节点, 如果没有可以返回null.
     * @return 
     */
    Spatial getSpatial();
    
}
