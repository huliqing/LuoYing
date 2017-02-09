/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.menu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.stage.Modality;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.ui.FileTree;

/**
 * 菜单“删除文件、文件夹”
 * @author huliqing
 */
public class DeleteMenuItem extends MenuItem {

    private final FileTree fileTree;
    
    public DeleteMenuItem(FileTree fileTree) {
        this(fileTree, Manager.getRes(ResConstants.POPUP_DELETE));
    }
    
    public DeleteMenuItem(FileTree fileTree, String text) {
        super(text);
        this.fileTree = fileTree;
        setOnAction(e -> doDelete());
    }
    
    private void doDelete() {
        ObservableList<TreeItem<File>> items = fileTree.getSelectionModel().getSelectedItems();
        if (items.isEmpty())
            return;
        List<TreeItem<File>> selectList = new ArrayList<TreeItem<File>>(items);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
//        alert.initOwner(getScene().getWindow()); // 这一句会导致result.get() == null,并报错
        alert.setTitle(Manager.getRes(ResConstants.ALERT_DELETE_TITLE));
        if (selectList.size() == 1 && selectList.get(0).getValue() != null) {
            alert.setHeaderText(Manager.getRes(ResConstants.ALERT_DELETE_HEADER_SINGLE));
            alert.setContentText(selectList.get(0).getValue().getName());
        } else {
            alert.setHeaderText(Manager.getRes(ResConstants.ALERT_DELETE_HEADER_MULT, new Object[]{selectList.size()}));
        }
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            selectList.stream().filter(t -> t.getValue() != null).forEach(t -> {
                deleteFile(t.getValue()); // 递归删除
                if (t.getParent() != null) {
                    t.getParent().getChildren().remove(t);
                }
            });
        }
    }
    
    private void deleteFile(File file) {
        if (file.isDirectory()) {
            for (File sub : file.listFiles()) {
                deleteFile(sub);
            }
        }
        file.delete();
    }
}
