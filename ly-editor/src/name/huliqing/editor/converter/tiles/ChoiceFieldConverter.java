/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.tiles;

import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import name.huliqing.editor.converter.AbstractPropertyConverter;
import name.huliqing.editor.converter.DataConverter;

/**
 *
 * @author huliqing
 */
public class ChoiceFieldConverter extends AbstractPropertyConverter {
    
    /** 指定可选的项目列表，格式: "item1,item2,item3"*/
    public final static String FEATURE_ITEMS = ChoiceFieldConverter.class.getName() + ":items";

    private final ChoiceBox<String> choice = new ChoiceBox();
    
    public ChoiceFieldConverter() {
//        choice.prefWidthProperty().bind(root.widthProperty()); // BUG，会让界面越来越宽
        choice.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateAttribute(choice.getValue());
            }
        });
    }

    @Override
    public void initialize(DataConverter parent) {
        super.initialize(parent);
        choice.getItems().clear();
        List<String> items = featureHelper.getAsList(FEATURE_ITEMS);
        if (items != null) {
            choice.getItems().addAll(items);
        }
    }
    
    @Override
    protected Node createLayout() {
        return choice;
    }
    
    @Override
    public void updateUI(Object propertyValue) {
        choice.setValue(propertyValue != null ? propertyValue.toString() : null);
    }
    
}
