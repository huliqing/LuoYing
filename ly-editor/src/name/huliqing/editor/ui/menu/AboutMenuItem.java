/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.menu;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.ui.CustomDialog;
import name.huliqing.fxswing.Jfx;

/**
 * 快捷键帮助说明
 * @author huliqing
 */
public class AboutMenuItem extends MenuItem {

    private CustomDialog dialog;
    
    public AboutMenuItem() {
        super(Manager.getRes(ResConstants.MENU_HELP_ABOUT));
        setOnAction(e -> {
            if (dialog == null) {
                Label name = new Label("落梅");
                Label version = new Label("版本：1.0-Alpha");
                Label email = new Label("联系：31703299@qq.com");
                VBox layout = new VBox();
                layout.getChildren().addAll(name, version, email);
                layout.setSpacing(10);
                layout.setPadding(new Insets(10));
                dialog = new CustomDialog(Jfx.getJfxWindow());
                dialog.getChildren().add(layout);
                dialog.setPrefWidth(400);
                dialog.setPrefHeight(200);
            }
            dialog.setTitle(Manager.getRes(ResConstants.MENU_HELP_ABOUT_TITLE));
            dialog.showOnCenter();
        });
    }

}
