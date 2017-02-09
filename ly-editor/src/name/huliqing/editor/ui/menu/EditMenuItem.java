/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.menu;

import java.io.File;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.EditManager;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.ui.FileTree;

/**
 * * 菜单“编辑文件”,根据不同的文件类型打开不同的编辑器
 * @author huliqing
 */
public class EditMenuItem extends MenuItem {

    private final FileTree fileTree;
    
    public EditMenuItem(FileTree fileTree) {
        this(fileTree, Manager.getRes(ResConstants.POPUP_EDIT));
    }
    
    public EditMenuItem(FileTree fileTree, String text) {
        super(text);
        this.fileTree = fileTree;
        setOnAction(e -> doEdit());
    }
    
    public void doEdit() {
        File fileSelected = getSelectMainFile();
        if (fileSelected == null || fileSelected.isDirectory()) 
            return;
        EditManager.openEdit(fileSelected.getAbsolutePath());
    }
    
    private File getSelectMainFile() {
        ObservableList<TreeItem<File>> selectedItems = fileTree.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty())
            return null;
        TreeItem<File> itemSelect = selectedItems.get(0);
        if (itemSelect != null) {
            return itemSelect.getValue();
        }
        return null;
    }
}
