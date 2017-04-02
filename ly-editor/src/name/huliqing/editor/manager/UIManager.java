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
package name.huliqing.editor.manager;

import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import name.huliqing.editor.Editor;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.ui.MainLayout;
import name.huliqing.editor.ui.MenuForm;
import name.huliqing.editor.ui.OutputForm;
import name.huliqing.editor.ui.ResourceZone;
import name.huliqing.editor.ui.StatusZone;
import name.huliqing.fxswing.Jfx;

/**
 * JFX UI界面管理器
 * @author huliqing
 */
public class UIManager {
    // ----- 整体布局
    private static MainLayout mainLayout;
    
    // ---- 主体区域
    /** 菜单区域*/
    public final static MenuForm ZONE_MENU = new MenuForm();
    
    /** 资源区 */
    public final static ResourceZone ZONE_RESOURCE = new ResourceZone();
    
    /** 编辑区 */
    public final static Pane ZONE_EDIT = new VBox();
    
    /** 文本编辑区 */
    public final static TabPane ZONE_TEXT = new TabPane();
    
    /** 状态区 */
    public final static StatusZone ZONE_STATUS = new StatusZone();
    
    // ---- 各种窗口
    private static OutputForm outputForm;

    public final static void initializeLayout(Pane jfxRoot) {
        ZONE_EDIT.setBackground(Background.EMPTY);
        ZONE_TEXT.getTabs().addListener((ListChangeListener.Change<? extends Tab> c) -> {
            if (mainLayout != null) 
                mainLayout.setTextZoneVisible(!c.getList().isEmpty());
        });
        
        // 加载样式文件
        Jfx.getJfxRoot().getStylesheets().add("resources/style/gray_style.css");
        Jfx.getJfxRoot().getStylesheets().add("resources/style/gray_scroll_bar.css");

        // 创建主编辑器布器
        initMainLayout(jfxRoot);

        // 将JFX中的UndoRedo按键转换到JME场景中
        JfxUndoRedoKeyEventToJme();
    }
    
    public final static void displayOutputForm() {
        if (outputForm == null) {
            outputForm = new OutputForm();
            Tab tab = new Tab();
            tab.setText(Manager.getRes(ResConstants.FORM_OUTPUT_TITLE));
            tab.setContent(outputForm);
            outputForm.setUserData(tab);
        }
        Tab tab = (Tab) outputForm.getUserData();
        if (!ZONE_TEXT.getTabs().contains(tab)) {
            outputForm.setVisible(true);
            ZONE_TEXT.getTabs().add(tab);
        } else {
            ZONE_TEXT.getTabs().remove(tab);
            outputForm.setVisible(false);
        }
    }
    
    public final static void output(String output, Color color) {
        if (outputForm != null && outputForm.isVisible()) {
            Jfx.runOnJfx(() -> {
                outputForm.addText(output, color);
                outputForm.scrollToBottom();
            });
        }
    }
    
    private static void initMainLayout(Pane root) {
        if (mainLayout == null) {
            mainLayout = new MainLayout(root);
        }
        mainLayout.setZones(ZONE_MENU, ZONE_RESOURCE, ZONE_EDIT, ZONE_STATUS, ZONE_TEXT);
        root.getChildren().add(mainLayout);
    }
    
    // 将JFX中的UndoRedo按键转换到JME场景中
    private static void JfxUndoRedoKeyEventToJme() {
        Jfx.getJfxRoot().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination undo = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
            final KeyCombination redo = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
            final KeyCombination save = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
            final KeyCombination saveAll = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
            @Override
            public void handle(KeyEvent ke) {
                if (save.match(ke)) {
                    ((Editor) Jfx.getJmeApp()).save();
                } else if (saveAll.match(ke)) {
                    ((Editor) Jfx.getJmeApp()).saveAll();
                } else if(redo.match(ke)) {
                    ((Editor) Jfx.getJmeApp()).redo();
                } else if (undo.match(ke)) {
                    ((Editor) Jfx.getJmeApp()).undo();
                }
            }
        });
    }
}
