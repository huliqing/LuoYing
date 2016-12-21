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

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.layer.service.EntityService;
//import name.huliqing.luoying.layer.service.ItemService;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 消耗品，用于为指定角色补充属性值的物品
 * @author huliqing
 */
public class AttributeItem extends AbstractItem {
    private final EntityService entityService = Factory.get(EntityService.class);

    // 指定要补充哪个属性 
    private String attribute;
    
    // 补充的量可正，可负
    private float amount;

    @Override
    public void setData(ItemData data) {
        super.setData(data); 
        this.attribute = data.getAsString("attribute");
        this.amount = data.getAsFloat("amount", 0);
    }
    
    @Override
    protected void doUse(Entity actor) {
        // 补充属性值
        entityService.hitNumberAttribute(actor, attribute, amount, null);
        
        // 物品减少
        actor.removeObjectData(data, 1);
        
    }
    
}
