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

import com.jme3.util.SafeArrayList;
import java.util.List;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.toolbar.EditToolbar;

/**
 *
 * @author huliqing
 */
public class GroupTool extends AbstractTool {
    
    private final SafeArrayList<Tool> children = new SafeArrayList<Tool>(Tool.class);

    public GroupTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    public void initialize(SimpleJmeEdit edit, EditToolbar toolbar) {
        super.initialize(edit, toolbar);
        children.stream().filter(t -> !t.isInitialized()).forEach(t -> {t.initialize(edit, toolbar);});
    }

    @Override
    public void cleanup() {
        children.stream().filter(t -> t.isInitialized()).forEach(t -> {t.cleanup();});
        super.cleanup(); 
    }

    @Override
    protected void onToolEvent(Event e) {}
    
    public void addChild(Tool pt) {
        if (!children.contains(pt)) {
            children.add(pt);
            if (isInitialized() && !pt.isInitialized()) {
                pt.initialize(edit, toolbar);
            }
        }
    }
    
    public List<Tool> getChildren() {
        return children;
    }
    
    public boolean removeChild(Tool pt) {
        if (children.remove(pt)) {
            if (pt.isInitialized()) {
                pt.cleanup();
            }
            return true;
        }
        return false;
    }

    @Override
    public final void update(float tpf) {
        for (Tool t : children.getArray()) {
            t.update(tpf);
        }
    }
    
}
