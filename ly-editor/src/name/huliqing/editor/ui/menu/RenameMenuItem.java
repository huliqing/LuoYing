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
package name.huliqing.editor.ui.menu;

import java.io.File;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.ui.FileTree;

/**
 * 菜单“重命名文件、文件夹”
 * @author huliqing
 */
public class RenameMenuItem extends MenuItem {

    private final FileTree fileTree;
    
    public RenameMenuItem(FileTree fileTree) {
        this(fileTree, Manager.getRes(ResConstants.POPUP_RENAME));
    }
    
    public RenameMenuItem(FileTree fileTree, String text) {
        super(text);
        this.fileTree = fileTree;
        setOnAction(e -> doRename());
    }

    private void doRename() {
        ObservableList<TreeItem<File>> selectedItems = fileTree.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty())
            return;
        TreeItem<File> itemSelect = selectedItems.get(0);
        if (itemSelect == null || itemSelect.getValue() == null) {
            return;
        }
        TextInputDialog dialog = new TextInputDialog(itemSelect.getValue().getName());
        dialog.setTitle(Manager.getRes(ResConstants.ALERT_RENAME_TITLE));
        dialog.setHeaderText(Manager.getRes(ResConstants.ALERT_RENAME_HEADER
                , new Object[] {itemSelect.getValue().getName()}));
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (name == null || name.trim().isEmpty()) {
                return;
            }
            File file = itemSelect.getValue();
            File newFile = new File(file.getParent(), name);
            file.renameTo(newFile);
            itemSelect.setValue(newFile);
        });
    }
}
