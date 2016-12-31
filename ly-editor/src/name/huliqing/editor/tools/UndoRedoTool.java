/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;

/**
 * Undo Redo工具
 * @author huliqing
 */
public class UndoRedoTool extends EditTool {
    
    private final String EVENT_UNDO = "undoEvent";
    private final String EVENT_REDO = "redoEvent";

    public UndoRedoTool(String name) {
        super(name);
    }
    
    public JmeEvent bindUndoEvent() {
        return bindEvent(EVENT_UNDO);
    }
    
    public JmeEvent bindRedoEvent() {
        return bindEvent(EVENT_REDO);
    }

    @Override
    protected void onToolEvent(Event e) {
        if (!e.isMatch()) {
            return;
        }
        if (EVENT_UNDO.equals(e.getName())) {
            form.getUndoRedoManager().undo();
        } else if (EVENT_REDO.equals(e.getName())){
            form.getUndoRedoManager().redo();
        }
    }
    
}
