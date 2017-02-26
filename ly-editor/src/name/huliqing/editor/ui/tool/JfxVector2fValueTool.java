/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.tool;

import com.jme3.math.Vector2f;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.tools.ValueChangedListener;
import name.huliqing.editor.tools.ValueTool;
import name.huliqing.editor.tools.Vector2fValueTool;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class JfxVector2fValueTool extends JfxAbstractTool<Vector2fValueTool> implements ValueChangedListener<Vector2f>{

    private final VBox view = new VBox();
    private final Label label = new Label();
    
    private final HBox content = new HBox();
    private final Label xLabel = new Label("X");
    private final Label yLabel = new Label("Y");
    private final TextField xValue = new TextField();
    private final TextField yValue = new TextField();
    
    private boolean ignoreUpdateView;
    
    public JfxVector2fValueTool() {
        view.getChildren().addAll(label, content);
        content.getChildren().addAll(xLabel, xValue, yLabel, yValue);
        
        xValue.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) return;
            updateValueToEdit();
        });
        xValue.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) updateValueToEdit();
        });
        yValue.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) return;
            updateValueToEdit();
        });
        yValue.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) updateValueToEdit();
        });
        
        view.getStyleClass().add(StyleConstants.CLASS_HVBOX);
        content.getStyleClass().add(StyleConstants.CLASS_HVBOX);
        xLabel.setPrefWidth(15);
        yLabel.setPrefWidth(15);
        xValue.setPrefWidth(64);
        yValue.setPrefWidth(64);
        
        content.prefWidthProperty().bind(view.widthProperty());
        content.setAlignment(Pos.CENTER_LEFT);
        view.setAlignment(Pos.CENTER_LEFT);
        view.setSpacing(5);
    }
    
    @Override
    protected Region createView() {
        return view;
    }
    
    @Override
    public void initialize() {
        super.initialize();
        label.setText(tool.getName());
        updateValueToView(tool.getValue());
        tool.addValueChangeListener(this);
        
        // tooltip
        if (tool.getTips() != null) {
            label.setTooltip(new Tooltip(tool.getTips()));
        }
    }
    
    @Override
    public void onValueChanged(ValueTool<Vector2f> vt, Vector2f oldValue, Vector2f newValue) {
        Jfx.runOnJfx(() -> {
            updateValueToView(newValue);
        });
    }
    
    private void updateValueToEdit() {
        try {
            float x = Float.parseFloat(xValue.getText());
            float y = Float.parseFloat(yValue.getText());
            Jfx.runOnJme(() -> {
                ignoreUpdateView = true;
                tool.setValue(new Vector2f(x, y));
                ignoreUpdateView = false;
            });
        } catch (NumberFormatException nfe) {
            // ignore
        }
    }
    
    // 当Jfx组件值发生变化时更新到编辑场景中
    private void updateValueToView(Vector2f newValue) {
        if (ignoreUpdateView) {
            return;
        }
        if (newValue == null) {
            xValue.setText("");
            yValue.setText("");
            return;
        }
        xValue.setText(newValue.x + "");
        yValue.setText(newValue.y + "");
    }
}
