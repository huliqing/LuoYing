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
package name.huliqing.editor.ui.utils;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

/**
 * 用于支持自动完成功能，让ComboBox有自动提示录入的功能。
 * @author huliqing
 * @param <T>
 */
public class AutoCompleteComboBoxListener<T> implements EventHandler<KeyEvent> {

    private final ComboBox comboBox;
    private final ObservableList<T> items = FXCollections.observableArrayList();
    private final ObservableList filterItems = FXCollections.observableArrayList();
    
    private boolean moveCaretToPos = false;
    private int caretPos;
    
    private String lastInputText;

    public AutoCompleteComboBoxListener(final ComboBox comboBox) {
        this.comboBox = comboBox;
        this.comboBox.setEditable(true);
        this.comboBox.setOnKeyPressed((KeyEvent t) -> {
            comboBox.hide();
        });
        this.comboBox.setOnKeyReleased(AutoCompleteComboBoxListener.this);
        setItems(comboBox.getItems());
    }
    
    public void setItems(List<T> items) {
        this.items.clear();
        this.items.addAll(items);
        this.filterItems.clear();
        this.filterItems.addAll(items);
        this.comboBox.setItems(filterItems);
    }
    
    @Override
    public void handle(KeyEvent event) {
        if (event.isControlDown() 
                || event.getCode() == KeyCode.CONTROL
                || event.getCode() == KeyCode.TAB
                || event.getCode() == KeyCode.HOME
                || event.getCode() == KeyCode.END 
                || event.getCode() == KeyCode.LEFT
                || event.getCode() == KeyCode.RIGHT 
                ) {
            return;
        }

        if(event.getCode() == KeyCode.UP) {
            caretPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getCode() == KeyCode.DOWN) {
            if(!comboBox.isShowing()) {
                comboBox.show();
            }
            caretPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getCode() == KeyCode.BACK_SPACE) {
            moveCaretToPos = true;
            caretPos = comboBox.getEditor().getCaretPosition();
        } else if(event.getCode() == KeyCode.DELETE) {
            moveCaretToPos = true;
            caretPos = comboBox.getEditor().getCaretPosition();
        }

        lastInputText = comboBox.getEditor().getText();
        
        String inputLowerCase = lastInputText.toLowerCase().trim();
        filterItems.clear();
        items.stream().filter((d) -> (d.toString().toLowerCase().contains(inputLowerCase))).forEach((d) -> {
            filterItems.add(d);
        });

        comboBox.setItems(filterItems);
        comboBox.getEditor().setText(lastInputText);
        
        if(!moveCaretToPos) {
            caretPos = -1;
        }
        moveCaret(lastInputText.length());
        if(!comboBox.getItems().isEmpty()) {
            comboBox.show();
        }
    }

    private void moveCaret(int textLength) {
        if(caretPos == -1) {
            comboBox.getEditor().positionCaret(textLength);
        } else {
            comboBox.getEditor().positionCaret(caretPos);
        }
        moveCaretToPos = false;
    }
    
    public void setStringConverter(StringConverter sc) {
        comboBox.setConverter(sc);
    }
}
