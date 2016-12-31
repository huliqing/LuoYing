/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.toolview;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import name.huliqing.editor.forms.Mode;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.tools.ModeTool;
import name.huliqing.editor.tools.Tool;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class ModeToolView extends AbstractToolView implements ModeTool.ModeChangedListener {
    
    private final ChoiceBox<Mode> view = new ChoiceBox<Mode>();
    private ModeTool modeTool;
    
    public ModeToolView() {
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
    public void initialize(Tool tool, Toolbar toolbar, String displayName, String icon, String tooltip) {
        super.initialize(tool, toolbar, displayName, icon, tooltip);
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
