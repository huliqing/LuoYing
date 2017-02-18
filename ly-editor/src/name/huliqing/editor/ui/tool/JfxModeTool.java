/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.tool;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import name.huliqing.editor.edit.Mode;
import name.huliqing.editor.tools.base.ModeTool;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class JfxModeTool extends JfxAbstractTool implements ModeTool.ModeChangedListener {
    
    private final ChoiceBox<Mode> view = new ChoiceBox<Mode>();
    private ModeTool modeTool;
    private boolean ignoreEvent;
    
    public JfxModeTool() {
        view.setPrefWidth(80);
        view.setPrefHeight(25);
        view.setMaxHeight(25);
    }
    
    @Override
    public Region createView() {
        return view;
    }

    @Override
    public void onModeChanged(Mode newMode) {
        if (newMode == view.getValue())
            return;
        Jfx.runOnJfx(() -> {
            ignoreEvent = true;
            view.setValue(newMode);
            ignoreEvent = false;
        });
    }

    @Override
    public void initialize() {
        super.initialize();
        modeTool = (ModeTool) tool;
        modeTool.addModeChangedListener(this);
        view.getItems().clear();
        view.getItems().addAll(Mode.values());
        view.setValue(modeTool.getMode());
        view.valueProperty().addListener((ObservableValue<? extends Mode> observable, Mode oldValue, Mode newValue) -> {
            if (ignoreEvent)
                return;
            Jfx.runOnJme(() -> {
                modeTool.setMode(view.getValue());
            });
        });
        
        // tooltip
        if (tool.getTips() != null) {
            view.setTooltip(new Tooltip(tool.getTips()));
        }
    }
    
}
