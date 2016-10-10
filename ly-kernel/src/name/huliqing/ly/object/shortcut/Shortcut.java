/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.shortcut;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.ly.data.ObjectData;
import name.huliqing.ly.data.ShortcutData;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.xml.DataProcessor;

/**
 *
 * @author huliqing
 * @param <T>
 */
public interface Shortcut<T extends ObjectData> extends DataProcessor<ShortcutData>{
    
    /**
     * 初始化快捷方式
     */
    void initialize();
    
    /**
     * 判断快捷方式是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理快捷方式
     */
    void cleanup();
   
    /**
     * 获取快捷方式界面节点。
     * @return 
     */
    Spatial getView();

    /**
     * 获取快捷方式的操作角色
     * @return 
     */
    Entity getActor();
    
    /**
     * 设置设置快捷方式的操作角色
     * @param actor 
     */
    void setActor(Entity actor);
    
    /**
     * @return 
     */
    T getObjectData();
    
    /**
     * @param objectData 
     */
    void setObjectData(T objectData);
   
    /**
     * 获取快捷方式的宽度
     * @return 
     */
    float getWidth();
    
    /**
     * 设置快捷方式宽度
     * @param width 
     */
    void setWidth(float width);
    
    /**
     * 获取快捷方式的高度
     * @return 
     */
    float getHeight();
    
    /**
     * 设置快捷方式高度
     * @param height 
     */
    void setHeight(float height);

    /**
     * 获取快捷方式的位置
     * @return 
     */
    Vector3f getLocation();
    
    /**
     * 设置快捷方式的位置
     * @param location 
     */
    void setLocation(Vector3f location);

    /**
     * 判断快捷方式是否可以拖动
     * @return 
     */
    boolean isDragEnabled();
    
    /**
     * 设置快捷方式是否可以拖动。
     * @param dragEnabled 
     */
    void setDragEnagled(boolean dragEnabled);
    
    /**
     * 删除物品
     */
    void removeObject();
}
