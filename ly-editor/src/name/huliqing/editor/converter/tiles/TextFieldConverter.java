/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.tiles;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import name.huliqing.editor.converter.AbstractPropertyConverter;
import name.huliqing.luoying.xml.Converter;

/**
 * @author huliqing
 */
public class TextFieldConverter extends AbstractPropertyConverter {

    private final TextField content = new TextField("");
    private String lastText = "";

    public TextFieldConverter() {
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
    protected Node createLayout() {
        return content;
    }
    
    private void updateChanged() {
        if (!content.getText().equals(lastText)) {
            lastText = content.getText();
            updateAttribute(lastText);
        }
    }
    
    @Override
    public void updateUI(Object propertyValue) {
        String value = Converter.getAsString(propertyValue);
        if (propertyValue != null) {
            content.setText(value);
        } else {
            content.setText("");
        }
        lastText = content.getText();
    }
    
}
