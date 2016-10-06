/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.entity;

import com.jme3.math.Vector3f;
import java.util.List;

/**
 * 实体容器，存取游戏场景中实体对象的容器
 * @author huliqing
 */
public interface EntityContainer {
    
    /**
     * 初始化entity容器
     */
    void initialize();
    
    /**
     * 判断容器是否已经初始化完毕
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理释放资源
     */
    void cleanup();
    
    /**
     * 添加一个实体,在添加实体之前必须确保实体已经初始化。
     * @param entity 
     */
    void addEntity(Entity entity);
    
    /**
     * 从容器中移除指定实体.
     * @param entity 
     * @return  
     */
    boolean removeEntity(Entity entity);
    
    /**
     * 通过唯一id来查找实体,如果不存在这个实体则反回null.
     * @param uniqueId
     * @return 
     */
    Entity getEntity(long uniqueId);
    
    /**
     * 在某一个位置点上，向周围一定的范围内查找实体，在这个范围内的实体都将一起返回。
     * @param location 指定的位置点
     * @param radius 指定的范围
     * @param store 存放结果
     * @return 
     */
    List<Entity> getEntities(Vector3f location, float radius, List<Entity> store);
    
    /**
     * 添加容器侦听器
     * @param listener 
     */
    void addEntityContainerListener(EntityContainerListener listener);
    
    /**
     * 移除容器侦听器
     * @param listener 
     */
    void removeEntityContainerListener(EntityContainerListener listener);
}
