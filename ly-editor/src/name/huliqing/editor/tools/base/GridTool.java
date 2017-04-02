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
package name.huliqing.editor.tools.base;

import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.tiles.Grid;
import name.huliqing.editor.toolbar.EditToolbar;
import name.huliqing.editor.tools.AbstractTool;
import name.huliqing.editor.tools.ToggleTool;

/**
 * 在场景中产生一个在原点处的网格(xz平面上)
 * @author huliqing
 */
public class GridTool extends AbstractTool implements ToggleTool {
    
    // 网格
    private final Grid grid = new Grid();

    public GridTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    public void initialize(SimpleJmeEdit jmeEdit, EditToolbar toolbar) {
        super.initialize(jmeEdit, toolbar);
        edit.getEditRoot().getParent().attachChild(grid); // 放在editRoot的父节点，这样不会被场景选择到。
    }

    @Override
    public void cleanup() {
        grid.removeFromParent();
        super.cleanup(); 
    }

    @Override
    protected void onToolEvent(Event e) {
        // ignore
    }
 
  
}
