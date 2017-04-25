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
package name.huliqing.editor.tools.entity;

import name.huliqing.editor.events.Event;
import name.huliqing.luoying.object.entity.impl.InstancedEntity;

/**
 *  InstancedTool用于实体刷支持instanced功能, 这允许实体刷在刷实体的时候选择一个Instanced实体，
 * 这样允许PaintTool把实体刷到指定的InstancedEntity下。
 * @author huliqing
 */
public class InstancedTool extends AbstractEntityBrushTool {
    
    private InstancedEntity instancedEntity;
    
    public InstancedTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }
    
    @Override
    protected void onToolEvent(Event e) {
        // ignore
    }
    
    public void setInstancedEntity(InstancedEntity instancedEntity) {
        this.instancedEntity = instancedEntity;
    }
    
    public InstancedEntity getInstancedEntity() {
        return instancedEntity;
    }
}
