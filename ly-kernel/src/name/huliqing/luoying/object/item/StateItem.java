/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.item;

import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 可让角色获得某些状态的物品
 * @author huliqing
 */
public class StateItem extends AbstractItem {
    private String[] states;

    @Override
    public void setData(ItemData data) {
        super.setData(data);
        states = data.getAsArray("states");
    }

    @Override
    protected void doUse(Entity actor) {
        if (states == null)
            return;
        
        // 因为添加状态涉及到概率，所以需要使用network方式
        for (String sid : states) {
            StateData sd = Loader.loadData(sid);
            actor.addObjectData(sd, 1);
        }
        actor.removeObjectData(data, 1);        
    }
    
}
