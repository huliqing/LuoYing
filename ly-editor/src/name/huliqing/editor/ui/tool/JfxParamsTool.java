/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.tool;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import name.huliqing.editor.tools.ParamsTool;
import name.huliqing.editor.tools.Tool;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class JfxParamsTool extends JfxAbstractTool<ParamsTool> {

    private final GridPane layout = new GridPane();
    
    public JfxParamsTool() {
        layout.setVgap(5);
    }
    
    @Override
    public Node createView() {
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
                        layout.addRow(i, jt.getView());
                    }
                }
            });
        });
    }
    
}
