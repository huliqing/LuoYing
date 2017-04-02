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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import name.huliqing.editor.converter.SimpleFieldConverter;
import name.huliqing.editor.utils.Validator;
import name.huliqing.luoying.xml.Converter;

/**
 * 整数(长)值类型字段转换器，只允许Null值及长整数类型的值，其它值不行。
 * @author huliqing
 */
public class LongTextConverter extends SimpleFieldConverter{
    
    private final TextField content = new TextField("");
    private Long lastValueSaved;
    
    public LongTextConverter() {
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
        lastValueSaved = data.getAsLong(field);
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
        
        if (!Validator.isLong(newStrValue)) {
            return;
        }
        
        Long newValue = Converter.getAsLong(newStrValue);
        if (lastValueSaved != null && lastValueSaved.longValue() == newValue.longValue()) {
            return;
        }
        updateAttribute(newValue);
        addUndoRedo(lastValueSaved, newValue);
        lastValueSaved = newValue;
    }
}
