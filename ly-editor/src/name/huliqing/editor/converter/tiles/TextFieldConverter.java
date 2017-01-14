/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.tiles;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import name.huliqing.editor.converter.AbstractPropertyConverter;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.edit.JfxAbstractEdit;

/**
 * @author huliqing
 */
public class TextFieldConverter extends AbstractPropertyConverter {

    private final TextField content = new TextField("");
    private boolean ignoreChangedEvent;

    public TextFieldConverter() {
        root.setContent(content);
        content.textProperty().addListener(this::change);
    }
    
    @Override
    public void initialize(JfxAbstractEdit editView, DataConverter parent, String property) {
        super.initialize(editView, parent, property);
        updateView(parent.getData().getAsString(property));
    }
    
    private void change(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (ignoreChangedEvent) {
            return;
        }
        this.parent.getData().setAttribute(property, newValue);
        notifyChangedToParent();
    }
    
    @Override
    public void updateView(Object propertyValue) {
        ignoreChangedEvent = true;
        if (propertyValue != null) {
            content.setText(propertyValue.toString());
        } else {
            content.setText("");
        }
        ignoreChangedEvent = false;
    }
    
}
