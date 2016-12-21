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

import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.object.attribute.BooleanAttribute;

/**
 * 可以改变角色的Boolean属性的状态
 * @author huliqing
 */
public class BooleanAttributeState extends AbstractState {    
    private String bindAttribute;
    private boolean value;
    private boolean restore;
    
    // ---- 保存原始状态以便恢复
    private BooleanAttribute attribute;
    private boolean originValue;
    
    @Override
    public void setData(StateData data) {
        super.setData(data);
        this.bindAttribute = data.getAsString("bindAttribute");
        this.value = data.getAsBoolean("value", false);
        this.restore = data.getAsBoolean("restore", restore);
    }

    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("value", value);
        data.setAttribute("originValue", originValue);
    }

    @Override
    public void initialize() {
        super.initialize();
        attribute = entity.getAttributeManager().getAttribute(bindAttribute, BooleanAttribute.class);
        if (attribute != null) {
            originValue = attribute.getValue();
            attribute.setValue(value);
        }
    }

    @Override
    public void cleanup() {
        if (restore && attribute != null) {
            attribute.setValue(originValue);
        }
        super.cleanup();
    }
    
}
