/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.toolbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ToolBar;
import name.huliqing.editor.tools.Tool;
import name.huliqing.editor.ui.tool.JfxToolFactory;
import name.huliqing.fxswing.Jfx;
import name.huliqing.editor.ui.tool.JfxTool;

/**
 *
 * @author huliqing
 */
public class JfxToolbar extends ToolBar implements ToolbarListener{

    private static final Logger LOG = Logger.getLogger(JfxToolbar.class.getName());
    
    private final Map<Tool, JfxTool> toolViewMap = new HashMap<Tool, JfxTool>();
    
    private EditToolbar  toolbar;
    private boolean initialized;
    
    public JfxToolbar(EditToolbar toolbar) {
        this.toolbar = toolbar;
    }
    
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException();
        }
        initialized = true;

        getItems().clear();
        toolbar.addListener(this);
        List<Tool> enableList =  toolbar.getToolsEnabled();
        List<Tool> activateList = toolbar.getToolsActivated();
        for (Tool tool : enableList) {
            JfxTool toolView = createToolView(tool, activateList != null && activateList.contains(tool));
            if (toolView != null) {
                toolViewMap.put(tool, toolView);
                getItems().add(toolView.getView());
            }
        }
    }
    
    public boolean isInitialized() {
        return initialized;
    }
    
    public void cleanup() {
        if (toolbar != null) {
            toolbar.removeListener(this);
            toolbar = null;
        }
        toolViewMap.clear();
        getItems().clear();
        initialized = false;
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
            JfxTool tv = toolViewMap.get(tool);
            if (tv != null) {
                tv.setActivated(true);
            }
        });
    }

    @Override
    public void onToolDeactivated(Tool tool) {
        Jfx.runOnJfx(() -> {
            JfxTool tv = toolViewMap.get(tool);
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
    
    private JfxTool createToolView(Tool tool, boolean activated) {
        JfxTool tv = JfxToolFactory.createToolView(tool, toolbar);
        if (tv != null) {
            tv.setActivated(activated);
            return tv;
        }  else {
            LOG.log(Level.WARNING, "Unsupported tool, toolName={0}, tool={1}"
                    , new Object[] {tool.getName(), tool.getClass().getName()});
            return null;
        }
    }
    
}
