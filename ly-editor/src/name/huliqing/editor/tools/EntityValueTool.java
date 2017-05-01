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
package name.huliqing.editor.tools;

import name.huliqing.editor.events.Event;
import name.huliqing.luoying.object.entity.Entity;

/**
 * EntityValueTool, 用于选择一个Entity
 * @author huliqing
 */
public class EntityValueTool extends AbstractValueTool<Entity> {
    
    private Class<? extends Entity> entityType;
    
    public EntityValueTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    protected void onToolEvent(Event e) {
        // ignore
    }
    
    /**
     * 获取要限定的实体类型
     * @return 
     */
    public Class<? extends Entity> getEntityType() {
        return entityType;
    }
    
    /**
     * 设置要限定的实体类型
     * @param entityType 
     */
    public void setEntityType(Class<? extends Entity> entityType) {
        this.entityType = entityType;
    }
}
