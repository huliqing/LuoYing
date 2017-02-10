/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.menu;

import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.ui.CustomDialog;
import name.huliqing.fxswing.Jfx;
import name.huliqing.luoying.manager.ResManager;
import name.huliqing.luoying.utils.FileUtils;

/**
 * 快捷键帮助说明
 * @author huliqing
 */
public class HelpShortcutMenuItem extends MenuItem {

    private CustomDialog dialog;
    private final TextArea textInput = new TextArea();
    
    public HelpShortcutMenuItem() {
        super(Manager.getRes(ResConstants.MENU_HELP_SHORTCUT));
        setOnAction(e -> {
            if (dialog == null) {
                dialog = new CustomDialog(Jfx.getJfxWindow());
                textInput.setPrefRowCount(30);
                textInput.setEditable(false);
                dialog.getChildren().add(textInput);
                dialog.prefWidth(500);
                dialog.prefHeight(550); 
            }
            textInput.setText(FileUtils.readFile(FileUtils.readFile("/resources/shortcut.txt"), "utf-8"));
            dialog.setTitle(Manager.getRes(ResConstants.MENU_HELP_SHORTCUT_TITLE));
            dialog.showOnCenter();
        });
    }

}
