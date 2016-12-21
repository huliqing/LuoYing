/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
