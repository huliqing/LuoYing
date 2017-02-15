/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.field;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import name.huliqing.editor.converter.SimpleFieldConverter;
import name.huliqing.luoying.xml.Converter;

/**
 *
 * @author huliqing
 */
public class CheckBoxConverter extends SimpleFieldConverter {

    private final CheckBox checkBox = new CheckBox();
    
    private Boolean lastValue;
    
    public CheckBoxConverter() {
        checkBox.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            updateAndSave(newValue);
        });
        root.setContent(checkBox);
    }
    
    @Override
    protected Node createLayout() {
        return checkBox;
    }
    
    private void updateAndSave(Boolean newValue) {
        if (lastValue == null && newValue == null) 
            return;
        if (lastValue != null && lastValue.equals(newValue)) {
            return;
        }
        updateAttribute(newValue);
        addUndoRedo(lastValue, newValue);
        lastValue = newValue;
    }
    
    @Override
    protected void updateUI() {
        Object propertyValue = data.getAttribute(field);
        lastValue = Converter.getAsBoolean(propertyValue);
        checkBox.setSelected(lastValue != null ? lastValue : false);
    }

    
}
