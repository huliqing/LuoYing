/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.item;

import name.huliqing.core.data.ItemData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.xml.DataProcessor;

/**
 *
 * @author huliqing
 */
public interface Item extends DataProcessor<ItemData> {
    
    /**
     * 判断角色是否可以使用当前物品
     * @param actor
     * @return 
     */
    boolean canUse(Actor actor);
    
    /**
     * 让角色使用指定的物品
     * @param actor 
     */
    void use(Actor actor);
    
}
