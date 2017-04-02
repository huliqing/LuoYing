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
