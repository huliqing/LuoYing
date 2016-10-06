/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.entity;

/**
 *
 * @author huliqing
 */
public interface EntityContainerListener {

    /**
     * 当容器初始化完毕后该方法被立即调用
     * @param ec 
     */
    void onEntityContainerInitialized(EntityContainer ec);

    /**
     * 当容器添加了一个实体之后该方法被立即调用。
     * @param ec
     * @param entityAdded 
     */
    void onEntityAdded(EntityContainer ec, Entity entityAdded);

    /**
     * 当容器移除了一个实体之后该方法被立即调用
     * @param ec
     * @param entityRemoved 
     */
    void onEntityRemoved(EntityContainer ec, Entity entityRemoved);

}
