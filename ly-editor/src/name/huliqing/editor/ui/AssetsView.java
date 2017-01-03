/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import java.io.File;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseButton;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.manager.ConfigManager;
import name.huliqing.editor.manager.ConfigManager.ConfigChangedListener;
import name.huliqing.editor.manager.EditManager;
import name.huliqing.editor.ui.tiles.FileTree;
import name.huliqing.fxswing.Jfx;

/**
 * 
 * @author huliqing
 */
public class AssetsView extends ScrollPane implements ConfigChangedListener {
    
    private final FileTree assetTree = new FileTree();
    private String currentAssetDir = "";
    
    public AssetsView() {
        assetTree.minHeightProperty().bind(heightProperty());
        assetTree.minWidthProperty().bind(widthProperty());
        setContent(assetTree);

        updateAassetDir();
        Manager.getConfigManager().addListener(this);
        
        assetTree.setOnMouseClicked((e) -> {
            if (e.getButton() != MouseButton.PRIMARY) 
                return;
            if (e.getClickCount() != 2) 
                return;
            ObservableList<TreeItem<File>> selectedItems = assetTree.getSelectionModel().getSelectedItems();
            if (selectedItems.isEmpty())
                return;
            TreeItem<File> itemSelect = selectedItems.get(0);
            if (itemSelect == null || itemSelect.getValue() == null || itemSelect.getValue().isDirectory())
                return;
            
            EditManager.openSpatialEditor(itemSelect.getValue().getAbsolutePath());
        });
    }

    @Override
    public void onConfigChanged(String key) {
        String newMainAssets = Manager.getConfigManager().getMainAssetDir();
        if (key.equals(ConfigManager.KEY_MAIN_ASSETS) && !currentAssetDir.equals(newMainAssets)) {
            Jfx.runOnJfx(() -> {
                updateAassetDir();
            });
        }
    }
    
    public void updateAassetDir() {
        String mainAssetsDir = Manager.getConfigManager().getMainAssetDir();
        if (mainAssetsDir != null && !mainAssetsDir.isEmpty()) {
            File assetsDirFile = new File(mainAssetsDir);
            if (assetsDirFile.isDirectory()) {
                currentAssetDir = mainAssetsDir;
                ((FileTree)assetTree).setRootDir(assetsDirFile);
                assetTree.getRoot().setExpanded(true);
            }            
        }
    }
}
