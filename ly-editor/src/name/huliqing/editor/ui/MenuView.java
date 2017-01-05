/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import java.io.File;
import java.util.List;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.ConfigManager;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class MenuView extends MenuBar implements ConfigManager.ConfigChangedListener {
    
    private final Menu file;
    private final MenuItem assets;
    private final Menu assetsRecent; // 最近的资源文件夹
    private final MenuItem quick;
    
    private final Menu help;
    
    public MenuView() {
        file = new Menu(Manager.getRes(ResConstants.MENU_FILE));
        assets = new MenuItem(Manager.getRes(ResConstants.MENU_FILE_ASSETS));
        assetsRecent = new Menu(Manager.getRes(ResConstants.MENU_FILE_ASSETS_RECENT));
        quick = new MenuItem(Manager.getRes(ResConstants.MENU_FILE_QUICK));
        
        help = new Menu(Manager.getRes(ResConstants.MENU_HELP));
        
        getMenus().addAll(file, help);
        file.getItems().addAll(assets, assetsRecent, quick);
        
        quick.setOnAction(e -> Jfx.getMainFrame().dispose());
        assets.setOnAction(e -> openAssetsChooser());
        
        rebuildAssetRecent();
        
        Manager.getConfigManager().addListener(this);
        
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
