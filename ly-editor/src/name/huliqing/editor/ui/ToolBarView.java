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
import name.huliqing.editor.editforms.EditForm;
import name.huliqing.editor.toolbar.EditToolbar;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.toolbar.ToolbarListener;
import name.huliqing.editor.tools.Tool;
import name.huliqing.editor.ui.toolview.ToolView;
import name.huliqing.editor.ui.toolview.ToolViewFactory;
import name.huliqing.fxswing.Jfx;
import name.huliqing.editor.editforms.EditFormListener;
import name.huliqing.editor.formview.FormView;

/**
 *
 * @author huliqing
 */
public class ToolBarView extends ToolBar implements EditFormListener, ToolbarListener{

    private static final Logger LOG = Logger.getLogger(ToolBarView.class.getName());
    
    private final FormView formView;
    private EditToolbar  toolbar;
    
    private final Map<Tool, ToolView> toolViewMap = new HashMap<Tool, ToolView>();
    
    public ToolBarView(FormView formView) {
        this.formView = formView;
        
        resetToolbar();
        
    }

    private void resetToolbar() {
        getItems().clear();
        toolViewMap.clear();
        if (toolbar != null) {
           toolbar.removeListener(this);
        }

        toolbar = (EditToolbar) formView.getEditForm().getToolbar();
        if (toolbar == null) {
            LOG.log(Level.WARNING, "Toolbar not found from EditForm! editForm={0}", formView.getEditForm());
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
    public void onToolbarChanged(EditForm form, Toolbar newToolbar) {
//        Jfx.runOnJfx(() -> {resetToolbar();});
    }
    
}
