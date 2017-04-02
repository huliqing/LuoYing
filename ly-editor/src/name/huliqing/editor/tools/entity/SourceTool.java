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

import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.events.Event;
import name.huliqing.luoying.data.EntityData;

/**
 * 实体刷的笔刷源列表,笔刷使用这些源来克隆到场景中
 * @author huliqing
 */
public class SourceTool extends AbstractEntityBrushTool {
    
    private final List<EntityData> sources = new ArrayList();

    public SourceTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    protected void onToolEvent(Event e) {
        // ignore
    }
    
    public void addSource(EntityData ed) {
        if (!sources.contains(ed)) {
            sources.add(ed);
        }
    }
    
    public boolean removeSource(EntityData ed) {
        return sources.remove(ed);
    }
    
    public List<EntityData> getSources() {
        return sources;
    }
}
