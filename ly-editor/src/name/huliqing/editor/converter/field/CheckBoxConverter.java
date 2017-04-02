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
