/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit;

import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author huliqing
 */
public class UndoRedoManager {

    private static final Logger LOG = Logger.getLogger(UndoRedoManager.class.getName());
    
    private final int limit = 32;
    private final Stack<UndoRedo> undoList = new Stack<UndoRedo>();
    private final Stack<UndoRedo> redoList = new Stack<UndoRedo>();
    
    public synchronized void add(UndoRedo undoRedo) {
        undoList.push(undoRedo);
        // 确保undo之后再操作的时候清理redo列表
        if (!redoList.isEmpty()) {
            redoList.clear();
        }
        // 历史记录的限制数
        if (undoList.size() > limit) {
            undoList.remove(0);
        }
//        LOG.log(Level.INFO, "====UndoRedo, add={0}, size={1}", new Object[]{undoRedo, undoList.size()});
    }
    
    public synchronized void undo() {
        if (undoList.isEmpty())
            return;
        UndoRedo ur = null;
        try {
            ur = undoList.pop();
            ur.undo();
//            LOG.log(Level.INFO, "<<<<UndoRedo, add={0}, after size={1}", new Object[]{ur, undoList.size()});
            redoList.add(ur);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not undo, ur=" + ur, e);
        }
    }
    
    public synchronized void redo() {
         if (redoList.isEmpty())
            return;
         UndoRedo ur = null;
         try {
            ur = redoList.pop();
            ur.redo();
            undoList.push(ur);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Could not redo, ur=" + ur, e);
        }
    }
}
