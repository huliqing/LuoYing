/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.field;

import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import name.huliqing.editor.converter.FieldConverter;

/**
 * @author huliqing
 */
public class ChoiceConverter extends FieldConverter {
    
    /** 指定可选的项目列表，格式: "item1,item2,item3"*/
    public final static String FEATURE_ITEMS = "items";

    private final ChoiceBox<String> choice = new ChoiceBox();
    
    private String lastValueSaved;
    
    public ChoiceConverter() {
        // BUG，会让界面越来越宽
//        choice.prefWidthProperty().bind(root.widthProperty()); 

        choice.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            updateChangedAndSave();
        });
    }
    
    @Override
    protected Node createLayout() {
        return choice;
    }

    @Override
    public void initialize() {
        super.initialize();
        choice.getItems().clear();
        List<String> items = featureHelper.getAsList(FEATURE_ITEMS);
        if (items != null) {
            choice.getItems().addAll(items);
        }
    }
    
    @Override
    public void updateUI(Object propertyValue) {
        lastValueSaved = propertyValue != null ? propertyValue.toString() : null;
        choice.setValue(lastValueSaved);
    }
    
    // 保存和记录历史
    private void updateChangedAndSave() {
        String newValue = choice.getValue();
        if (lastValueSaved == null && newValue == null)
            return;
        // 这里要避免回调循环
        if (lastValueSaved != null && lastValueSaved.equals(newValue)) 
            return;
        updateAttribute(newValue);
        addUndoRedo(lastValueSaved, newValue);
        lastValueSaved = newValue;
    }
}
