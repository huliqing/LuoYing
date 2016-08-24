/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.attribute;

import name.huliqing.core.data.AttributeData;

/**
 * 拥有“等级”功能的属性接口，这种属性可以设置等级。
 * @author huliqing
 * @param <V>
 * @param <T>
 */
public interface LevelAttribute<V, T extends AttributeData> extends Attribute<V, T>{
    
    /**
     * 设置属性等级
     * @param level 
     */
    void setLevel(int level);
    
    /**
     * 获取等级
     * @return
     */
    int getLevel();
    
}
