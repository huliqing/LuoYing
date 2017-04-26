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
package name.huliqing.editor.tools.batch;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.toolbar.EntityBatchToolbar;
import name.huliqing.editor.tools.AbstractTool;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 实体源列表，这个列表中存放所有将要进行Batch的实体
 * @author huliqing
 */
public class BatchSourceTool extends AbstractTool<SimpleJmeEdit, EntityBatchToolbar> {
    
    private List<Entity> entities;

    public BatchSourceTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    protected void onToolEvent(Event e) {
        // ignore
    }

    public List<Entity> getEntities() {
        return entities;
    }
    
    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }
    
    public void addEntity(Entity entity) {
        if (entities == null) {
            entities = new ArrayList<>();
        }
        if (!entities.contains(entity)) {
            entities.add(entity);
        }
    }
    
    public boolean removeEntity(Entity entity) {
        return entities != null && entities.remove(entity);
    }
    
    public void clear() {
        if (entities != null) {
            entities.clear();
        }
    }
}
