/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.tool;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import name.huliqing.editor.tools.BooleanValueTool;
import name.huliqing.editor.tools.ValueChangedListener;
import name.huliqing.editor.tools.ValueTool;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class JfxBooleanValueTool extends JfxAbstractTool<BooleanValueTool> implements ValueChangedListener<Boolean>{

    private final HBox layout = new HBox();
    private final Label label = new Label();
    private final CheckBox checkBox = new CheckBox();
    
    private boolean ignoreViewUpdate;
    
    public JfxBooleanValueTool() {
        layout.getChildren().addAll(label, checkBox);
        checkBox.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            Jfx.runOnJme(() -> {
                ignoreViewUpdate = true;
                tool.setValue(newValue);
                ignoreViewUpdate = false;
            });
        });
        
        label.setPrefWidth(100);
        checkBox.setPrefWidth(64);
    }
    
    @Override
    public Region createView() {
        return layout;
    }

    @Override
    public void onValueChanged(ValueTool<Boolean> vt, Boolean oldValue, Boolean newValue) {
        if (ignoreViewUpdate) 
            return;
        Jfx.runOnJfx(() -> {
            checkBox.setSelected(newValue);
        });
    }

    @Override
    public void initialize() {
        super.initialize();
        label.setText(tool.getName());
        checkBox.setSelected(tool.getValue());
    }
    
}
