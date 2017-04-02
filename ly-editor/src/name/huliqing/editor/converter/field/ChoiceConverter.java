/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.editor.converter.field;

import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import name.huliqing.editor.converter.SimpleFieldConverter;

/**
 * @author huliqing
 */
public class ChoiceConverter extends SimpleFieldConverter {
    
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
    protected void updateUI() {
        Object propertyValue = data.getAttribute(field);
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
