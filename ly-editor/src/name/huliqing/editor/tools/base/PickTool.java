/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.base;

import com.jme3.math.Ray;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.editor.tools.EditTool;
import name.huliqing.luoying.manager.PickManager;

/**
 * 选择物体的工具
 * @author huliqing
 */
public class PickTool extends EditTool {
    
    private SelectObj lastSelectObj;

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
        Ray pickRay = PickManager.getPickRay(editor.getCamera(), editor.getInputManager().getCursorPosition(), null);
        SelectObj newSelectObj = edit.doPick(pickRay);
        
        // 不需要增加历史记录
        if (newSelectObj != null && newSelectObj == lastSelectObj) {
            edit.setSelected(newSelectObj);
            lastSelectObj = newSelectObj;
            return;
        }
        
        // 需要增加历史记录
        if (newSelectObj != null) {
            SelectObj before = edit.getSelected();
            edit.setSelected(newSelectObj);
            lastSelectObj = newSelectObj;
            edit.addUndoRedo(new PickUndoRedo(before, newSelectObj));
        }
    }
    
    private class PickUndoRedo implements UndoRedo {
        private final SelectObj before;
        private final SelectObj after;
        
        public PickUndoRedo(SelectObj before, SelectObj after) {
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
