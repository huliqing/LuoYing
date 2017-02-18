/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.tool;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.tools.ParamsTool;
import name.huliqing.editor.tools.Tool;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class JfxParamsTool extends JfxAbstractTool<ParamsTool> {

    private final VBox layout = new VBox();
    
    public JfxParamsTool() {
        layout.getStyleClass().add(StyleConstants.CLASS_HVBOX);
        layout.setSpacing(5);
    }
    
    @Override
    public Region createView() {
        return layout;
    }

    @Override
    protected void setViewEnabled(boolean enabled) {
        root.setVisible(enabled);
    }

    @Override
    public void initialize() {
        super.initialize();
        Jfx.runOnJme(() -> {
            List<Tool> children = new ArrayList(tool.getChildren());
            Jfx.runOnJfx(() -> {
                for (int i = 0; i < children.size(); i++) {
                    Tool child = children.get(i);
                    JfxTool jt = JfxToolFactory.createJfxTool(child, toolbar);
                    if (jt != null) {
                        jt.initialize();
                        Region toolView = jt.getView();
                        toolView.prefWidthProperty().bind(layout.widthProperty());
                        layout.getChildren().add(toolView);
                    }
                }
            });
        });
    }
    
}
