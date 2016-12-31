/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import name.huliqing.editor.Editor;
import name.huliqing.editor.EditorListener;
import name.huliqing.editor.forms.Form;
import name.huliqing.editor.forms.FormListener;
import name.huliqing.editor.toolbar.EditToolbar;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.toolbar.ToolbarListener;
import name.huliqing.editor.tools.GridTool;
import name.huliqing.editor.tools.ModeTool;
import name.huliqing.editor.tools.MoveTool;
import name.huliqing.editor.tools.PickTool;
import name.huliqing.editor.tools.RotationTool;
import name.huliqing.editor.tools.ScaleTool;
import name.huliqing.editor.tools.Tool;
import name.huliqing.editor.ui.toolview.ModeToolView;
import name.huliqing.editor.ui.toolview.ToggleToolView;
import name.huliqing.editor.ui.toolview.ToolView;
import name.huliqing.editor.ui.toolview.ToolViewFactory;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class ToolBarView extends ToolBar implements EditorListener, FormListener, ToolbarListener{

    private static final Logger LOG = Logger.getLogger(ToolBarView.class.getName());
    
    private Form form;
    private EditToolbar  toolbar;
    
    // 暂位按钮，避免正式的工具按钮在未载入之前工具栏因为没有任何东西而导致被自动缩小而看不到。
    private final Button empty = new Button("");
    
    private final Map<Tool, ToolView> toolViewMap = new HashMap<Tool, ToolView>();
    
    public ToolBarView() {
        Editor editor = (Editor) Jfx.getJmeApp();
        editor.addListener(this);
        form = editor.getForm();
        if (form != null) {
            form.addListener(this);
            resetToolbar();
        }
        // 暂位,避免
        empty.setVisible(false);
        getItems().add(empty);
    }

    private void resetToolbar() {
        getItems().clear();
        toolViewMap.clear();
        if (toolbar != null) {
           toolbar.removeListener(this);
        }
        if (form == null) {
            return;
        }
        toolbar = (EditToolbar) form.getToolbar();
        if (toolbar == null) {
            return;
        }
        toolbar.addListener(this);
        List<Tool> enableList =  toolbar.getToolsEnabled();
        List<Tool> activateList = toolbar.getToolsActivated();
        for (Tool tool : enableList) {
            ToolView toolView = createToolView(tool, activateList != null && activateList.contains(tool));
            if (toolView != null) {
                toolViewMap.put(tool, toolView);
                getItems().add(toolView.getView());
            }
        }
    }

    @Override
    public void onToolAdded(Tool toolAdded) {
    }

    @Override
    public void onToolRemoved(Tool toolRemoved) {
    }

    @Override
    public void onToolActivated(Tool tool) {
        Jfx.runOnJfx(() -> {
            ToolView tv = toolViewMap.get(tool);
            if (tv != null) {
                tv.setActivated(true);
            }
        });
    }

    @Override
    public void onToolDeactivated(Tool tool) {
        Jfx.runOnJfx(() -> {
            ToolView tv = toolViewMap.get(tool);
            if (tv != null) {
                tv.setActivated(false);
            }
        });
    }

    @Override
    public void onToolEnabled(Tool tool) {
    }
    
    @Override
    public void onToolDisabled(Tool tool) {
    }
    
    private ToolView createToolView(Tool tool, boolean activated) {
        ToolView tv = ToolViewFactory.createToolView(tool, toolbar);
        if (tv != null) {
            tv.setActivated(activated); 
            return tv; 
        }  else {
            LOG.log(Level.WARNING, "Unsupported tool, toolName={0}, tool={1}"
                    , new Object[] {tool.getName(), tool.getClass().getName()});
            return null;
        }
    }

    @Override
    public void onFormChanged(Editor editor, Form newForm) {
        if (form != null) {
            form.removeListener(this);
        }
        form = newForm;
        form.addListener(this);
        Jfx.runOnJfx(() -> {resetToolbar();});
    }
    
    @Override
    public void onToolbarChanged(Form form, Toolbar newToolbar) {
        Jfx.runOnJfx(() -> {resetToolbar();});
    }
    
}
