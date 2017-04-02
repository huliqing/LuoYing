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

import com.jme3.export.binary.BinaryExporter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.ui.FileTree;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.data.SceneData;
import name.huliqing.luoying.object.Loader;

/**
 * 菜单“创建场景”
 * @author huliqing
 */
public class CreateSceneMenuItem extends MenuItem {

    private final FileTree fileTree;
    
    public CreateSceneMenuItem(FileTree fileTree) {
        this(fileTree, Manager.getRes(ResConstants.POPUP_CREATE_SCENE));
    }
    
    public CreateSceneMenuItem(FileTree fileTree, String text) {
        super(text);
        this.fileTree = fileTree;
        setOnAction(e -> doCreateScene());
    }
    
    public void doCreateScene() {
        SceneData sceneData = Loader.loadData(IdConstants.SYS_SCENE);
        TreeItem<File> item = fileTree.getSelectionModel().getSelectedItem();
        if (item == null || item.getValue() == null)
            return;
        File dir = item.getValue();
        if (!dir.exists() || dir.isFile()) 
            return;
        
        Jfx.runOnJme(() -> {
            try {
                File saveFile = makeSaveFile(dir, "scene", ".lyo", 0);
                BinaryExporter.getInstance().save(sceneData, saveFile);
                Jfx.runOnJfx(() -> {
                    fileTree.refreshItem(item);
                    fileTree.refresh();
                });
            } catch (IOException ex) {
                Logger.getLogger(CreateSceneMenuItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    private File makeSaveFile(File dir, String name, String suffix, int index) {
        File file = new File(dir, name + (index == 0 ? "" : index) + suffix);
        if (!file.exists()) {
            return file;
        } else {
            index++;
            return makeSaveFile(dir, name, suffix, index);
        }
    }
    
   
}
