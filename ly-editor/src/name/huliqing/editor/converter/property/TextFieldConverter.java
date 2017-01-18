/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.property;

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
    private String lastValueSaved;

    public TextFieldConverter() {
        // 失去焦点时更新
        content.focusedProperty().addListener((ObservableValue<? extends Boolean> observable
                , Boolean oldValue, Boolean newValue) -> {
            // 如果是获得焦点则不理睬。
            if (newValue) {
                return;
            }
            updateChangedAndSave();
        });
        // 按下Enter时更新
        content.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateChangedAndSave();
            }
        });
    }
    
    @Override
    protected Node createLayout() {
        return content;
    }
    
    private void updateChangedAndSave() {
        String newValue = content.getText();
        if (newValue != null && newValue.equals(lastValueSaved)) {
            return;
        }
        updateAttribute(newValue);
        addUndoRedo(lastValueSaved, newValue);
        lastValueSaved = newValue;
    }
    
    @Override
    public void updateUI(Object propertyValue) {
        lastValueSaved = Converter.getAsString(propertyValue);
        if (lastValueSaved != null) {
            content.setText(lastValueSaved);
        } else {
            content.setText("");
        }
    }
    
}
