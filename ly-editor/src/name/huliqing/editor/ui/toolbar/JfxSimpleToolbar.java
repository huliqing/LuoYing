/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.toolbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Region;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.toolbar.ToolbarListener;
import name.huliqing.editor.tools.Tool;
import name.huliqing.fxswing.Jfx;
import name.huliqing.editor.ui.tool.JfxTool;
import name.huliqing.editor.ui.tool.JfxToolFactory;

/**
 * JFX 普通的工具栏渲染器，将jme工具栏渲染为JFX的默认ToolBar样式
 * @author huliqing
 */
public class JfxSimpleToolbar extends ToolBar implements JfxToolbar, ToolbarListener {

    private static final Logger LOG = Logger.getLogger(JfxSimpleToolbar.class.getName());
    
    private final Map<Tool, JfxTool> toolViewMap = new HashMap<Tool, JfxTool>();
    
    private Toolbar  toolbar;
    private boolean initialized;
    
    public JfxSimpleToolbar() {
        setStyle("-fx-background-color:transparent;");
    }

    @Override
    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    @Override
    public String getName() {
        return toolbar.getName();
    }
    
    @Override
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException();
        }
        initialized = true;
        toolbar.addListener(this);
        List<Tool> tools =  toolbar.getTools();
        for (Tool tool : tools) {
            JfxTool jfxTool = createToolView(tool);
            if (jfxTool != null) {
                jfxTool.initialize();
                jfxTool.setEnabled(tool.isInitialized());
                jfxTool.getView().getStyleClass().add(StyleConstants.CLASS_TOOLBAR_BUTTON);
                toolViewMap.put(tool, jfxTool);
                getItems().add(jfxTool.getView());
            }
        }
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public void cleanup() {
        getItems().clear();
        toolViewMap.clear();
        toolbar.removeListener(this);
        initialized = false;
    }

    @Override
    public void onToolAdded(Tool toolAdded) {
    }

    @Override
    public void onToolRemoved(Tool toolRemoved) {
    }

    @Override
    public void onToolEnabled(Tool tool) {
        Jfx.runOnJfx(() -> {
            JfxTool tv = toolViewMap.get(tool);
            if (tv != null) {
                tv.setEnabled(true);
            }
        });
    }
    
    @Override
    public void onToolDisabled(Tool tool) {
        Jfx.runOnJfx(() -> {
            JfxTool tv = toolViewMap.get(tool);
            if (tv != null) {
                tv.setEnabled(false);
            }
        });
    }
    
    private JfxTool createToolView(Tool tool) {
        JfxTool tv = JfxToolFactory.createJfxTool(tool, toolbar);
        if (tv != null) {
            return tv;
        }  else {
            LOG.log(Level.WARNING, "Unsupported tool, toolName={0}, tool={1}"
                    , new Object[] {tool.getName(), tool.getClass().getName()});
            return null;
        }
    }

    @Override
    public void onStateChanged(boolean enabled) {
        this.setDisable(!enabled);
    }

    @Override
    public Region getView() {
        return this;
    }
    
}
