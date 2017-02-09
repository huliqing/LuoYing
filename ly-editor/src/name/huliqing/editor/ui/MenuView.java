/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import java.io.File;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import name.huliqing.editor.Editor;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.ConfigManager;
import name.huliqing.editor.utils.JfxUtils;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class MenuView extends MenuBar implements ConfigManager.ConfigChangedListener {
    
    private final Menu file;
    private final MenuItem assets;
    private final Menu assetsRecent; // 最近的资源文件夹
    private final MenuItem save;
    private final MenuItem quick;
    
    private final Menu help;
    
    public MenuView() {
        file = new Menu(Manager.getRes(ResConstants.MENU_FILE));
        assets = new MenuItem(Manager.getRes(ResConstants.MENU_FILE_ASSETS), JfxUtils.createImage(AssetConstants.INTERFACE_MENU_OPEN_DIR, 16, 16));
        assetsRecent = new Menu(Manager.getRes(ResConstants.MENU_FILE_ASSETS_RECENT), JfxUtils.createImage(AssetConstants.INTERFACE_MENU_OPEN_DIR_RECENT, 16, 16));
        save = new MenuItem(Manager.getRes(ResConstants.MENU_FILE_SAVE), JfxUtils.createImage(AssetConstants.INTERFACE_MENU_SAVE, 16, 16));
        quick = new MenuItem(Manager.getRes(ResConstants.MENU_FILE_QUICK), JfxUtils.createImage(AssetConstants.INTERFACE_MENU_QUIT, 16, 16));
        
        help = new Menu(Manager.getRes(ResConstants.MENU_HELP));
        
        file.getItems().addAll(assets, assetsRecent, save, quick);
        getMenus().addAll(file, help);
        
        rebuildAssetRecent();
        Manager.getConfigManager().addListener(this);
        
        assets.setOnAction(e -> openAssetsChooser());
        save.setOnAction(e -> save());
        quick.setOnAction(e -> Jfx.getMainFrame().dispose());

        file.showingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Editor editor = (Editor) Jfx.getJmeApp();
                    save.setDisable(!editor.isModified());
                }
            }
        });
    }
    
    private void save() {
        Editor editor = (Editor) Jfx.getJmeApp();
        editor.save();
        save.setDisable(true);
    }

    private void openAssetsChooser() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle(Manager.getRes(ResConstants.MENU_FILE_ASSETS));
        dirChooser.setInitialDirectory(new File("."));
        File chooseFile = dirChooser.showDialog(null);
        if (chooseFile != null && chooseFile.isDirectory()) {
            Manager.getConfigManager().setMainAssetDir(chooseFile.getAbsolutePath());
        }
    }
    
    private void openAssets(String assetsDir) {
        File assetsDirFile = new File(assetsDir);
        if (!assetsDirFile.exists() || !assetsDirFile.isDirectory()) {
            return;
        }
        Jfx.runOnJme(() -> {Manager.getConfigManager().setMainAssetDir(assetsDir);});
        
    }
    
    private void rebuildAssetRecent() {
        assetsRecent.getItems().clear();
        List<String> assetsDirs = Manager.getConfigManager().getAssetsDirs();
        if (assetsDirs != null && !assetsDirs.isEmpty()) {
            assetsDirs.forEach(t -> {
                if (t == null || t.trim().equals("")) {return;}
                MenuItem item = new MenuItem(t);
                item.setOnAction(e -> {
                    openAssets(item.getText());
                });
                assetsRecent.getItems().add(item);
            });
            MenuItem cleanItem = new MenuItem(Manager.getRes(ResConstants.MENU_FILE_ASSETS_RECENT_CLEAN));
            cleanItem.setOnAction(e -> {
                assetsRecent.getItems().clear();
                Jfx.runOnJme(() -> {
                    Manager.getConfigManager().setAssetsDirs(null);}
                );
            });
            assetsRecent.getItems().add(cleanItem);
        }
    }

    @Override
    public void onConfigChanged(String key) {
        if (ConfigManager.KEY_ASSETS.equals(key)) {
            Jfx.runOnJfx(() -> {rebuildAssetRecent();});
        }
    }
    
}
