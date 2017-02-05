/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
