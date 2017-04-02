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

import com.jme3.math.Ray;
import com.jme3.util.SafeArrayList;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.editor.edit.controls.ControlTile;
import name.huliqing.editor.tools.AbstractTool;
import name.huliqing.luoying.manager.PickManager;

/**
 * 选择物体的工具
 * @author huliqing
 */
public class PickTool extends AbstractTool {
    
    private ControlTile lastSelectedControlTile;

    public PickTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }
    
    public JmeEvent bindPickEvent() {
        return bindEvent("pickEvent");
    }

    @Override
    public void onToolEvent(Event e) {
        if (e.isMatch()) {
            doPick();
        }
    }

    private void doPick() {
        SafeArrayList<ControlTile> cts = edit.getControlTiles();
        if (cts == null || cts.isEmpty())
            return;
        
        Ray pickRay = PickManager.getPickRay(editor.getCamera(), editor.getInputManager().getCursorPosition(), null);
        Float minDist = null;
        Float temp;
        ControlTile ctPicked = null;
        for (ControlTile ct : cts.getArray()) {
            temp = ct.pickCheck(pickRay);
            if (temp == null)
                continue;
            if (minDist == null || temp.floatValue() < minDist) {
                minDist = temp;
                ctPicked = ct;
            }
        }
        
        // 不需要增加历史记录
        if (ctPicked != null && ctPicked == lastSelectedControlTile) {
            edit.setSelected(ctPicked);
            lastSelectedControlTile = ctPicked;
            return;
        }
        
        // 需要增加历史记录
        if (ctPicked != null) {
            ControlTile before = edit.getSelected();
            edit.setSelected(ctPicked);
            lastSelectedControlTile = ctPicked;
            edit.addUndoRedo(new PickUndoRedo(before, ctPicked));
        }
    }
    
    private class PickUndoRedo implements UndoRedo {
        private final ControlTile before;
        private final ControlTile after;
        
        public PickUndoRedo(ControlTile before, ControlTile after) {
            this.before = before;
            this.after = after;
        }
        
        @Override
        public void undo() {
            if (before != null) {
                edit.setSelected(before);
            }
        }

        @Override
        public void redo() {
            if (after != null) {
                edit.setSelected(after);
            }
        }
    
    }
}
