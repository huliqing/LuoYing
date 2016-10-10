/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.entity;

import name.huliqing.ly.object.module.AttributeModule;
import name.huliqing.ly.object.module.Module;

/**
 *
 * @author huliqing
 */
public class EntityFactory {
    
    /**
     * 从entity中获取指定类型的模块,如果存在多个相同类型的模块，则返回第一个找到的。
     * @param <T>
     * @param entity
     * @param type
     * @return 
     */
    public static <T extends Module> T getModule(Entity entity, Class<T> type) {
        return (T) entity.getModule(type);
    }
    
    /**
     * 转换entity为type类型，如果entity为null或者不是type类型，则返回null。
     * @param <T>
     * @param entity
     * @param type
     * @return 
     */
    public static <T extends Entity> T cast(Entity entity, Class<T> type) {
        if (entity != null && type.isAssignableFrom(entity.getClass())) {
            return (T) entity;
        }
        return null;
    }
}
