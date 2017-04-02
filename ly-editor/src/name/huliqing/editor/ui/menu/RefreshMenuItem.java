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
