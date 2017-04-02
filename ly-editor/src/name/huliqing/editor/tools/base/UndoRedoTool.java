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

import com.jme3.input.KeyInput;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.tools.AbstractTool;

/**
 * Undo Redo工具
 * @author huliqing
 */
public class UndoRedoTool extends AbstractTool {
    
    private final String EVENT_UNDO_REDO = "undoRedoEvent";
    
    private final JmeEvent LCtrEvent;
    private final JmeEvent RCtrEvent;
    private boolean ctrPressed;
    
    private final JmeEvent LShiftEvent;
    private final JmeEvent RShiftEvent;
    private boolean shiftPressed;

    public UndoRedoTool(String name, String tips, String icon) {
        super(name, tips, icon);
        LCtrEvent = bindEvent("LCtrEvent").bindKey(KeyInput.KEY_LCONTROL, true);
        RCtrEvent = bindEvent("RCtrEvent").bindKey(KeyInput.KEY_RCONTROL, true);
        LShiftEvent = bindEvent("LShiftEvent").bindKey(KeyInput.KEY_LSHIFT, true);
        RShiftEvent = bindEvent("RShiftEvent").bindKey(KeyInput.KEY_RSHIFT, true);
    }
    
    /**
     * 绑定一个UndoRedo按键如(z).
     * 配合ctr和shift进行undo\redo
     * @return 
     */
    public JmeEvent bindUndoRedoEvent() {
        return bindEvent(EVENT_UNDO_REDO);
    }
    
    @Override
    protected void onToolEvent(Event e) {
        if (e == LCtrEvent || e == RCtrEvent) {
            ctrPressed = e.isMatch();
        } else if (e == LShiftEvent || e == RShiftEvent) {
            shiftPressed = e.isMatch();
        }
        
        // 优先redo,后再undo,因为按键存在冲突
        if (e.isMatch() && EVENT_UNDO_REDO.equals(e.getName())) {
            if (ctrPressed && shiftPressed) {
                edit.redo();
            } else if (ctrPressed) {
                edit.undo();
            }
        }
    }
    
}
