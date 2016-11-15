/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.item;

import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 可用于删除角色身上某些状态的物品
 * @author huliqing
 */
public class StateRemoveItem extends AbstractItem {
    
    private String[] states;
    
    @Override
    public void setData(ItemData data) {
        super.setData(data);
        states = data.getAsArray("states");
    }

    @Override
    public void use(Entity actor) {
        super.use(actor);
        
        if (states == null) 
            return;
        
        for (String sid : states) {
            ObjectData od = actor.getData().getObjectData(sid);
            if (od != null) {
                actor.removeObjectData(od, 1);
            }
        }
        
        // 物品减少
        actor.removeObjectData(data, 1);    
    }
    
}
