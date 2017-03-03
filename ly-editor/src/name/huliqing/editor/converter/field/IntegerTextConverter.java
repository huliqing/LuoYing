/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.field;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import name.huliqing.editor.converter.SimpleFieldConverter;
import name.huliqing.editor.utils.Validator;
import name.huliqing.luoying.xml.Converter;

/**
 * 整数值类型字段转换器，只允许Null值及整数类型的值，其它值不行。
 * @author huliqing
 */
public class IntegerTextConverter extends SimpleFieldConverter{
    
    private final TextField content = new TextField("");
    private Integer lastValueSaved;
    
    public IntegerTextConverter() {
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
    protected void updateUI() {
        lastValueSaved = data.getAsInteger(field);
        if (lastValueSaved != null) {
            content.setText(lastValueSaved + "");
        } else {
            content.setText("");
        }
    }

    @Override
    protected Node createLayout() {
        return content;
    }
    
    private void updateChangedAndSave() {
        String newStrValue = content.getText();
        if (newStrValue == null || newStrValue.trim().isEmpty()) {
            if (lastValueSaved == null) {
                return;
            } else {
                updateAttribute(null);
                addUndoRedo(lastValueSaved, null);
                lastValueSaved = null;
                return;
            }
        }
        
        if (!Validator.isInteger(newStrValue)) {
            return;
        }
        
        Integer newValue = Converter.getAsInteger(newStrValue);
        if (lastValueSaved != null && lastValueSaved.intValue() == newValue.intValue()) {
            return;
        }
        updateAttribute(newValue);
        addUndoRedo(lastValueSaved, newValue);
        lastValueSaved = newValue;
    }
}
