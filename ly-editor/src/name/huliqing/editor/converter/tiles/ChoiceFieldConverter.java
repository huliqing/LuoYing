/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.tiles;

import java.util.Arrays;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ChoiceBox;
import name.huliqing.editor.converter.AbstractPropertyConverter;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.edit.JfxAbstractEdit;

/**
 *
 * @author huliqing
 */
public class ChoiceFieldConverter extends AbstractPropertyConverter {

    private final ChoiceBox<String> choice = new ChoiceBox();
    private boolean ignoreChangedEvent;
    
    public ChoiceFieldConverter(String... items) {
        this(Arrays.asList(items));
    }
    public ChoiceFieldConverter(List<String> items) {
        choice.getItems().addAll(items);
//        choice.prefWidthProperty().bind(root.widthProperty()); // BUG，会让界面越来越宽
        root.setContent(choice);
    }

    @Override
    public void initialize(JfxAbstractEdit editView, DataConverter parent, String property) {
        super.initialize(editView, parent, property);
        String value = parent.getData().getAsString(property);
        choice.setValue(value);
        
        choice.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateChanged();
            }
        });
    }
    
    private void updateChanged() {
        if (ignoreChangedEvent)
            return;
        parent.getData().setAttribute(property, choice.getValue());
        notifyChangedToParent();
    }
    
    @Override
    public void updateView(Object propertyValue) {
        ignoreChangedEvent = true;
        choice.setValue(propertyValue.toString());
        ignoreChangedEvent = false;
    }
    
}
