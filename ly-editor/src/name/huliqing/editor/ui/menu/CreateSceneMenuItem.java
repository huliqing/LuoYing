/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
                File saveFile = makeSaveFile(dir, "scene", ".ying", 0);
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
