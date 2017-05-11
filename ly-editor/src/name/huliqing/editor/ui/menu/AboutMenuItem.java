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
                Label name = new Label("落樱RPG编辑器");
                Label version = new Label("版本：3.0-Alpha");
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
