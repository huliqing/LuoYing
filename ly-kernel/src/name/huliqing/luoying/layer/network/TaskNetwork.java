/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public interface TaskNetwork extends Inject {
    
    /**
     * 完成指定的任务
     * @param actor
     * @param taskId
     */
    void completeTask(Entity actor, String taskId);
    
    // remove20161123
//    /**
//     * 增加或减少任务物品的数量,任务物品并不作为普通物品一样存放在角色包裹上
//     * ,因为任务物品不能使用、删除
//     * @param actor 角色
//     * @param taskId 任务
//     * @param itemId 任务物品ID
//     * @param amount 要增加或减少的任务物品数量，可正可负 
//     */
//    void applyItem(Entity actor, String taskId, String itemId, int amount);
    
    
}
