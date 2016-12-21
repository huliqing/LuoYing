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
package name.huliqing.luoying.object.drop;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.DropData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.object.el.STNumberEl;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 掉落属性值设置
 * @author huliqing
 */
public class AttributeDrop extends AbstractDrop {
    private final ElService elService = Factory.get(ElService.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    
    // 属性名称
    private String attribute;
    // 这个公式定义角色source可以掉落多少属性值给target。
    private STNumberEl valueHitEl;
    
    @Override
    public void setData(DropData data) {
        super.setData(data);
        attribute = data.getAsString("attribute");
        valueHitEl = elService.createSTNumberEl(data.getAsString("valueHitEl", "#{0}"));
    }

    @Override
    public void doDrop(Entity source, Entity target) {
        // 属性掉是掉给target角色的，如果target不存在，则就没有意义。
        if (target == null) {
            return;
        }
        valueHitEl.setSource(source.getAttributeManager());
        valueHitEl.setTarget(target.getAttributeManager());
        entityNetwork.hitNumberAttribute(target, attribute, valueHitEl.getValue().floatValue(), source);
        playDropSounds(source);
    }

}
