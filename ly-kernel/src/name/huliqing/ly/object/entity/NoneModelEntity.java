/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.entity;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import name.huliqing.ly.data.EntityData;

/**
 * 非模型类型的场景实体，这类实体一般不需要有实际存在于场景中的可视模型，一般如各类环境效果：灯光、水体（Filter)、
 * 物理环境、天空、相机、阴影渲染及各类Filter\Scene Processor
 * @author huliqing
 * @param <T>
 */
public abstract class NoneModelEntity <T extends EntityData> extends AbstractEntity<T> {

    /** 
     * 这个物体作为所有不需要实际存在的Entity的Spatial用于
     * {@link #getSpatial() } 方法的调用返回, 避免在调用getSpatial的时候返回null.
     */
    protected final Node NULL_ROOT = new Node(getClass().getName());
    
    @Override
    public Spatial getSpatial() {
        return NULL_ROOT;
    }
    
}
