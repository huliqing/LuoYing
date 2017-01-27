/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.tool;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import name.huliqing.editor.edit.Mode;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.tools.ModeTool;
import name.huliqing.editor.tools.Tool;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class JfxModeTool extends JfxAbstractTool implements ModeTool.ModeChangedListener {
    
    private final ChoiceBox<Mode> view = new ChoiceBox<Mode>();
    private ModeTool modeTool;
    
    public JfxModeTool() {
    }

    @Override
    protected void setViewActivated(boolean activated) {
        // ignore
    }

    @Override
    protected void setViewEnabled(boolean enabled) {
        view.setDisable(!enabled);
    }

    @Override
    public Node getView() {
        return view;
    }

    @Override
    public void onModeChanged(Mode newMode) {
        if (newMode == view.getValue())
            return;
        Jfx.runOnJfx(() -> {
            view.setValue(newMode);
        });
    }

    @Override
    public void initialize(Tool tool, Toolbar toolbar, String name, String tooltip, String icon) {
        super.initialize(tool, toolbar, name, tooltip, icon);
        modeTool = (ModeTool) tool;
        modeTool.addListener(this);
        view.getItems().clear();
        view.getItems().addAll(Mode.values());
        view.setValue(modeTool.getMode());
        view.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Jfx.runOnJme(() -> {
                    modeTool.setMode(view.getValue());
                });
            }
        });
        
        // tooltip
        if (tooltip != null && !tooltip.isEmpty()) {
            view.setTooltip(new Tooltip(tooltip));
        }
    }
    
    
}
