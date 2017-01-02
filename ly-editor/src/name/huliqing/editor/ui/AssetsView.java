/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import java.io.File;
import javafx.scene.layout.VBox;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.manager.ConfigManager;
import name.huliqing.editor.manager.ConfigManager.ConfigChangedListener;
import name.huliqing.editor.ui.tiles.FileTree;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class AssetsView extends VBox implements ConfigChangedListener {
    
    private final FileTree assetTree = new FileTree();
    private String currentAssetDir = "";
    
    public AssetsView() {
        assetTree.minHeightProperty().bind(this.heightProperty());
        assetTree.minWidthProperty().bind(this.widthProperty());
        getChildren().add(assetTree);
        updateAassetDir();
        
        Manager.getConfigManager().addListener(this);
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
                assetTree.setRootDir(assetsDirFile);
                assetTree.getRoot().setExpanded(true);
            }            
        }
    }
}
