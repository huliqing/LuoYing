/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.item;

import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.message.StateCode;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * 定义角色杂物品
 * @author huliqing
 */
public interface Item extends DataProcessor<ItemData> {
    
    /**
     * 获取物品类型id， 注：这个是物品的类型ID，并非唯一ID。
     * @return 
     */
    String getId();
    
    /**
     * 这个方法返回一个状态码，用于判断指定角色是否可以使用这个物品。只有返回值为 {@link StateCode#DATA_USE}
     * 时才可以使用物品。
     * @param entity
     * @return 
     * @see #canUse(Entity) 
     * @see StateCode
     */
    int checkStateCode(Entity entity);
    
    /**
     * 让角色使用指定的物品。注：这个方法会强制使用物品，如果要判断当前状态下角色是否可以使用这件物品，
     * 可以使用{@link #canUse(Entity) } 或 {@link #checkStateCode(Entity) }
     * @param entity 
     * @return  
     * @see #canUse(Entity) 
     * @see #checkStateCode(Entity) 
     */
    void use(Entity entity);
    
}
