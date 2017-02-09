/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
