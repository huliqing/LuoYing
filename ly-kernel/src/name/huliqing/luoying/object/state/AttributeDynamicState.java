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
package name.huliqing.luoying.object.state;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 可以"持续"改变角色属性值的状态.
 * @author huliqing
 */
public class AttributeDynamicState extends AbstractState {
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);

    // 影响的属性名称
    private String bindNumberAttribute;
    // 每一次间隔要添加的数值
    private float addValue;
    // 时间间隔,单位秒。
    private float interval;
    
    // ---- inner
    private float intervalUsed;
    private Entity sourceActor;
    
    @Override
    public void setData(StateData data) {
        super.setData(data);
        bindNumberAttribute = data.getAsString("bindNumberAttribute");
        addValue = data.getAsFloat("addValue");
        interval = data.getAsFloat("interval", 1.0f);
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        intervalUsed += tpf;
        if (intervalUsed >= interval && bindNumberAttribute != null) {
            intervalUsed = 0;
            if (sourceActor == null) {
                sourceActor = entity.getScene().getEntity(data.getSourceActor());
            }
            entityNetwork.hitNumberAttribute(entity, bindNumberAttribute, addValue, sourceActor);
        }
    }

}
