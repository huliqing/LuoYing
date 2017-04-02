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
