/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.undoredo.UndoRedo;
import name.huliqing.luoying.manager.PickManager;

/**
 * 选择物体的工具
 * @author huliqing
 */
public class PickTool extends EditTool {
    
    private final CollisionResults pickResults = new CollisionResults();
    
    public PickTool(String name) {
        super(name);
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
        // remove20170111
//        Node editRoot = form.getEditRoot();
//        pickResults.clear();
//        PickManager.pick(editor.getCamera(), editor.getInputManager().getCursorPosition(), editRoot, pickResults);
//        if (pickResults.size() > 0) {
//            Spatial picked = pickResults.getClosestCollision().getGeometry();
//            SelectObj before = form.getSelected();
//            SelectObj after = new SpatialSelectObj(picked);
//            form.setSelected(after);
//            form.addUndoRedo(new PickUndoRedo(before, after));
//        }

        Ray pickRay = PickManager.getPickRay(editor.getCamera(), editor.getInputManager().getCursorPosition(), null);
        SelectObj newSelectObj = form.doPick(pickRay);
        if (newSelectObj != null) {
            SelectObj before = form.getSelected();
            form.setSelected(newSelectObj);
            form.addUndoRedo(new PickUndoRedo(before, newSelectObj));
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
                form.setSelected(before);
            }
        }

        @Override
        public void redo() {
            if (after != null) {
                form.setSelected(after);
            }
        }
    
    }
}
