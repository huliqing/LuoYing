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
 * Float值类型字段转换器，只允许Null值及float类型的值，其它值不行。
 * @author huliqing
 */
public class FloatTextConverter extends SimpleFieldConverter{
    
    private final TextField content = new TextField("");
    private String lastValueSaved;
    
    public FloatTextConverter() {
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
        Object propertyValue = data.getAttribute(field);
        lastValueSaved = Converter.getAsString(propertyValue);
        if (lastValueSaved != null) {
            content.setText(lastValueSaved);
        } else {
            content.setText("");
        }
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
        if (newValue != null && !Validator.isFloat(newValue)) {
            return;
        }
        // newValue为null或为Float都可以
        updateAttribute(newValue);
        addUndoRedo(lastValueSaved, newValue);
        lastValueSaved = newValue;
    }
}
