/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.tool;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import name.huliqing.editor.tools.NumberValueTool;
import name.huliqing.editor.tools.ValueChangedListener;
import name.huliqing.editor.tools.ValueTool;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class JfxNumberValueTool extends JfxAbstractTool<NumberValueTool> 
        implements ValueChangedListener<Number>{

//    private final HBox view = new HBox();
    private final GridPane view = new GridPane();
    private final Label label = new Label();
    private final TextField value = new TextField();
    
    private boolean ignoreUpdateView;
    
    public JfxNumberValueTool() {
        label.setPrefWidth(64);
        value.setPrefWidth(64);
        view.addRow(0, label, value);
    
         // 失去焦点时更新
        value.focusedProperty().addListener((ObservableValue<? extends Boolean> observable
                , Boolean oldValue, Boolean newValue) -> {
            // 如果是获得焦点则不理睬。
            if (newValue) {
                return;
            }
            updateValueToEdit();
        });
        // 按下Enter时更新
        value.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateValueToEdit();
            }
        });
    }

    @Override
    public Node createView() {
        return view;
    }
    
    @Override
    public void initialize() {
        super.initialize();
        label.setText(tool.getName());
        updateValueToView(tool.getValue());
        tool.addValueChangeListener(this);
    }

    // 当3d场景工具值发生变化时更新到JFX组件
    @Override
    public void onValueChanged(ValueTool<Number> vt, Number oldValue, Number newValue) {
        Jfx.runOnJfx(() -> {
            updateValueToView(newValue);
        });
    }
    
    private void updateValueToEdit() {
        try {
            double v = Double.parseDouble(value.getText());
            Jfx.runOnJme(() -> {
                ignoreUpdateView = true;
                tool.setValue(v);
                // 重新获取值，因为NumberValueTool可能会有大小值限制，那么一切以NumberValueTool的值为准
                value.setText(tool.getValue().toString());
                ignoreUpdateView = false;
            });
        } catch (NumberFormatException nfe) {
            // ignore
        }
    }
    
    // 当Jfx组件值发生变化时更新到编辑场景中
    private void updateValueToView(Number newValue) {
        if (ignoreUpdateView) {
            return;
        }
        if (newValue == null) {
            value.setText("");
            return;
        }
        value.setText(newValue.doubleValue() + "");
    }

}
