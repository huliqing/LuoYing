/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author huliqing
 */
public class AutoCompleteComboBoxListener<T> implements EventHandler<KeyEvent> {

    private ComboBox comboBox;
    private final ObservableList<T> items = FXCollections.observableArrayList();
    private final ObservableList filterItems = FXCollections.observableArrayList();
    
    private boolean moveCaretToPos = false;
    private int caretPos;
    
    private String lastInputText;

    public AutoCompleteComboBoxListener(final ComboBox comboBox) {
        this.comboBox = comboBox;
        this.comboBox.setEditable(true);
        this.comboBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                comboBox.hide();
            }
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
