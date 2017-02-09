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
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.ui.FileTree;

/**
 * 菜单“刷新文件夹”
 * @author huliqing
 */
public class RefreshMenuItem extends MenuItem {

    private final FileTree fileTree;
    
    public RefreshMenuItem(FileTree fileTree) {
        this(fileTree, Manager.getRes(ResConstants.POPUP_REFRESH));
    }
    
    public RefreshMenuItem(FileTree fileTree, String text) {
        super(text);
        this.fileTree = fileTree;
        setOnAction(e -> doRefresh());
    }
    
    private void doRefresh() {
        ObservableList<TreeItem<File>> items = fileTree.getSelectionModel().getSelectedItems();
        items.filtered(t -> t != null).forEach(t -> {
            fileTree.refreshItem(t);
        });
    }
}
