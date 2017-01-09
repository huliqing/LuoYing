/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * @author huliqing
 */
public class TextFieldConverter extends AbstractPropertyConverter {

    private final TextField content = new TextField("");

    @Override
    public void initialize(DataConverter parent, String property) {
        super.initialize(parent, property);
        String fieldValue = parent.getData().getAsString(property);
        content.setText(fieldValue != null ? fieldValue : "");
        root.setContent(content);
        
        content.textProperty().addListener(this::change);
    }

    private void change(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        notifyChangedToParent();
    }
}
