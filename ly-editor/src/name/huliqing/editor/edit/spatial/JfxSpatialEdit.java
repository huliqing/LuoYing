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
package name.huliqing.editor.edit.spatial;

import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import name.huliqing.editor.edit.JfxSimpleEdit;
import name.huliqing.fxswing.Jfx;

/**
 * Spatial编辑器
 * @author huliqing
 */
public class JfxSpatialEdit extends JfxSimpleEdit<SpatialEdit> {
//    private static final Logger LOG = Logger.getLogger(JfxSpatialEdit.class.getName());
    
    public JfxSpatialEdit() {
        this.jmeEdit = new SpatialEdit();
    }

    @Override
    protected void onDragOver(DragEvent e) {
        Dragboard db = e.getDragboard();
        if (db.hasFiles()) {
            e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        e.consume();
    }

    @Override
    protected void onDragDropped(DragEvent e) {
        Dragboard db = e.getDragboard();
        if (db.hasFiles()) {
            
            // remove,以后要改为添加节点到当前场景
//            EditManager.openSpatialEditor(db.getFiles().get(0).getAbsolutePath()); 

            e.setDropCompleted(true);
        }
        e.consume();
    }

    public void setFilePath(String abstractFilePath) {
        Jfx.runOnJme(() -> {
            jmeEdit.setFilePath(abstractFilePath);
        });
    }
 
    
}
