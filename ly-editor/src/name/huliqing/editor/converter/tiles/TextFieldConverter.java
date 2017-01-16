/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.tiles;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import name.huliqing.editor.converter.AbstractPropertyConverter;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.edit.JfxAbstractEdit;

/**
 * @author huliqing
 */
public class TextFieldConverter extends AbstractPropertyConverter {

    private final TextField content = new TextField("");
    private boolean ignoreChangedEvent;
    private String lastText = "";

    public TextFieldConverter() {
        root.setContent(content);
        // 失去焦点时更新
        content.focusedProperty().addListener((ObservableValue<? extends Boolean> observable
                , Boolean oldValue, Boolean newValue) -> {
            // 如果是获得焦点则不理睬。
            if (newValue) {
                return;
            }
            updateChanged();
        });
        // 按下Enter时更新
        content.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateChanged();
            }
        });
    }
    
    @Override
    public void initialize(JfxAbstractEdit editView, DataConverter parent, String property) {
        super.initialize(editView, parent, property);
        updateView(parent.getData().getAsString(property));
    }
    
    private void updateChanged() {
        if (ignoreChangedEvent) {
            return;
        }
        // 失去焦点时才检测变化
        if (!content.getText().equals(lastText)) {
            lastText = content.getText();
            this.parent.getData().setAttribute(property, lastText);
            notifyChangedToParent();
        }
    }
    
    @Override
    public void updateView(Object propertyValue) {
        ignoreChangedEvent = true;
        if (propertyValue != null) {
            content.setText(propertyValue.toString());
        } else {
            content.setText("");
        }
        lastText = content.getText();
        ignoreChangedEvent = false;
    }
    
}
