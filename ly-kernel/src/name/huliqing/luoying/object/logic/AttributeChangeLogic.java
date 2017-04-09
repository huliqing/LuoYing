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
package name.huliqing.luoying.object.logic;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.object.attribute.NumberAttribute;

/**
 * 改变角色属性的逻辑,一般可用来恢复角色的生命，魔法，能量之类的
 * @author huliqing 
 */
public class AttributeChangeLogic extends AbstractLogic {
    
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    private float value = 1f;
    // 影响的目标属性的id
    private String applyAttribute;
    // 作为影响因素的目标属性的id
    private String bindFactorAttribute;
    
    // ---- inner
    private NumberAttribute factorAttribute;

    @Override
    public void setData(LogicData data) {
        super.setData(data); 
        this.value = data.getAsFloat("value");
        this.applyAttribute = data.getAsString("applyAttribute");
        this.bindFactorAttribute = data.getAsString("bindFactorAttribute");
    }

    @Override
    public void initialize() {
        super.initialize();
        factorAttribute = actor.getAttribute(bindFactorAttribute, NumberAttribute.class);
    }
    
    @Override
    protected void doLogic(float tpf) {
        float useFactor = factorAttribute != null ? factorAttribute.floatValue() : 0;
        float applyValue = value * useFactor * interval;
        entityNetwork.hitNumberAttribute(actor, applyAttribute, applyValue, null);
    }
}
