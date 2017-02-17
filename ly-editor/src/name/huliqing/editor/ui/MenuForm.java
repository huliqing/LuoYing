/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import java.io.File;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import name.huliqing.editor.Editor;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.constants.ConfigConstants;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.ConfigManager;
import name.huliqing.editor.manager.UIManager;
import name.huliqing.editor.ui.menu.AboutMenuItem;
import name.huliqing.editor.ui.menu.HelpShortcutMenuItem;
import name.huliqing.editor.ui.utils.JfxUtils;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class MenuForm extends MenuBar implements ConfigManager.ConfigChangedListener {
    
    private final Menu file;
    private final MenuItem assets;
    private final Menu assetsRecent; // 最近的资源文件夹
    private final MenuItem save;
    private final MenuItem quick;
    
    private final Menu help;
    private final MenuItem helpShortcut;
    private final MenuItem helpAbout;
    
    private final Menu form;
    private final MenuItem formOutput;
    
    public MenuForm() {
        file = new Menu(Manager.getRes(ResConstants.MENU_FILE));
        assets = new MenuItem(Manager.getRes(ResConstants.MENU_FILE_ASSETS), JfxUtils.createIcon(AssetConstants.INTERFACE_MENU_OPEN_DIR));
        assetsRecent = new Menu(Manager.getRes(ResConstants.MENU_FILE_ASSETS_RECENT), JfxUtils.createIcon(AssetConstants.INTERFACE_MENU_OPEN_DIR_RECENT));
        save = new MenuItem(Manager.getRes(ResConstants.MENU_FILE_SAVE), JfxUtils.createIcon(AssetConstants.INTERFACE_MENU_SAVE));
        quick = new MenuItem(Manager.getRes(ResConstants.MENU_FILE_QUICK), JfxUtils.createIcon(AssetConstants.INTERFACE_MENU_QUIT));
        assets.setOnAction(e -> openAssetsChooser());
        save.setOnAction(e -> save());
        quick.setOnAction(e -> Quit.doQuit());
        file.getItems().addAll(assets, assetsRecent, save, quick);
        file.showingProperty().addListener((ObservableValue<? extends Boolean> observable
                , Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                Editor editor = (Editor) Jfx.getJmeApp();
                save.setDisable(!editor.isModified());
            }
        });

        help = new Menu(Manager.getRes(ResConstants.MENU_HELP));
        helpShortcut = new HelpShortcutMenuItem();
        helpAbout = new AboutMenuItem();
        help.getItems().addAll(helpShortcut, helpAbout);
        
        form = new Menu(Manager.getRes(ResConstants.MENU_FORM));
        formOutput = new MenuItem(Manager.getRes(ResConstants.FORM_OUTPUT_TITLE));
        form.getItems().add(formOutput);
        formOutput.setOnAction(e -> UIManager.displayOutputForm());
        
        getMenus().addAll(file, form, help);
        rebuildAssetRecent();
        Manager.getConfigManager().addListener(this);
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
        if (ConfigConstants.KEY_ASSETS.equals(key)) {
            Jfx.runOnJfx(() -> {rebuildAssetRecent();});
        }
    }
    
}
