/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.manager;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import name.huliqing.editor.Editor;
import name.huliqing.editor.ui.MainLayout;
import name.huliqing.editor.ui.MenuView;
import name.huliqing.editor.ui.ResourceView;
import name.huliqing.editor.ui.StatusBar;
import name.huliqing.fxswing.Jfx;

/**
 * JFX UI界面管理器
 * @author huliqing
 */
public class UIManager {
    
    /** 菜单区域*/
    public final static MenuView ZONE_MENU = new MenuView();
    
    /** 资源区 */
    public final static ResourceView ZONE_RESOURCE = new ResourceView();
    
    /** 编辑区 */
    public final static Pane ZONE_EDIT = new VBox();
    
    /** 状态区 */
    public final static StatusBar ZONE_STATUS = new StatusBar();

    public final static void initializeLayout(Pane jfxRoot) {
        ZONE_EDIT.setBackground(Background.EMPTY);
        
        // 加载样式文件
        Jfx.getJfxRoot().getStylesheets().add("resources/style/style.css");

        // 创建主编辑器布器
        initMainLayout(jfxRoot);

        // 将JFX中的UndoRedo按键转换到JME场景中
        JfxUndoRedoKeyEventToJme();
    }
    
    private static void initMainLayout(Pane root) {
        MainLayout mainLayout = new MainLayout(root);
        mainLayout.setZones(ZONE_MENU, ZONE_RESOURCE, ZONE_EDIT, ZONE_STATUS);
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
