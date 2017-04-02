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

import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.ui.CustomDialog;
import name.huliqing.fxswing.Jfx;
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
