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
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.toolbar.ToolbarListener;
import name.huliqing.editor.tools.Tool;
import name.huliqing.editor.ui.tool.JfxTool;
import name.huliqing.editor.ui.tool.JfxToolFactory;
import name.huliqing.fxswing.Jfx;

/**
 * JFX 普通工具栏渲染器，将JME工具栏渲染到VBox样式
 * @author huliqing
 */
public class JfxEditToolbar implements JfxToolbar, ToolbarListener{

    private static final Logger LOG = Logger.getLogger(JfxEditToolbar.class.getName());
    
    private final ScrollPane root = new ScrollPane();
    private final VBox layout = new VBox();
    private final Map<Tool, JfxTool> toolViewMap = new HashMap<Tool, JfxTool>();
    
    private Toolbar  toolbar;
    private boolean initialized;
    
    public JfxEditToolbar() {
        root.setContent(layout);
        root.setPadding(new Insets(7));
        
        layout.prefHeightProperty().bind(root.heightProperty());
        layout.prefWidthProperty().bind(root.widthProperty());
        layout.getStyleClass().add(StyleConstants.CLASS_HVBOX);
        layout.setPadding(Insets.EMPTY);
        layout.setSpacing(5);
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
                toolViewMap.put(tool, jfxTool);
                Region view = jfxTool.getView();
                layout.getChildren().add(view);
                view.prefWidthProperty().bind(layout.widthProperty());
            }
        }
        root.setFitToWidth(true);
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public void cleanup() {
        toolbar.removeListener(this);
        layout.getChildren().clear();
        toolViewMap.clear();
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
        Jfx.runOnJfx(() -> {
            layout.setDisable(!enabled);
        });
    }

    @Override
    public Region getView() {
        return root;
    }
    
}
