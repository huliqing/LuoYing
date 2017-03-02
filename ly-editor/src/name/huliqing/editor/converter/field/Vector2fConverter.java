/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.field;

import com.jme3.math.Vector2f;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.converter.SimpleFieldConverter;
import name.huliqing.luoying.xml.Converter;

/**
 *
 * @author huliqing
 */
public class Vector2fConverter extends SimpleFieldConverter{
    
    private final VBox layout = new VBox();
    
    private final HBox xLayout = new HBox();
    private final Label xLabel = new Label("X");
    private final TextField xField = new TextField("");
    
    private final HBox yLayout = new HBox();
    private final Label yLabel = new Label("Y");
    private final TextField yField = new TextField("");
    
    private String lastX = "";
    private String lastY = "";
    private Vector2f lastValue;
    
    private final ChangeListener<Boolean> focusedListener = (ObservableValue<? extends Boolean> observable
            , Boolean oldValue, Boolean newValue) -> {
        if (newValue) {
            return;
        }
        updateAndSave();
    };
    private final EventHandler<KeyEvent> keyHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER) {
                updateAndSave();
            }
        }
    };
    
    public Vector2fConverter() {
        layout.getStyleClass().add(StyleConstants.CLASS_HVBOX);
        xLayout.getStyleClass().add(StyleConstants.CLASS_HVBOX);
        yLayout.getStyleClass().add(StyleConstants.CLASS_HVBOX);
        
        layout.getChildren().addAll(xLayout, yLayout);
        xLayout.getChildren().addAll(xLabel, xField);
        yLayout.getChildren().addAll(yLabel, yField);
        
        xLayout.setAlignment(Pos.CENTER_LEFT);
        yLayout.setAlignment(Pos.CENTER_LEFT);
        
        xField.focusedProperty().addListener(focusedListener);
        yField.focusedProperty().addListener(focusedListener);
        xField.setOnKeyPressed(keyHandler);
        yField.setOnKeyPressed(keyHandler);
        
        xLabel.setMinWidth(15);
        yLabel.setMinWidth(15);
        layout.setSpacing(3);
    }

    @Override
    protected Node createLayout() {
        return layout;
    }
    
    private void updateAndSave() {
        boolean changed = false;
        if (!xField.getText().equals(lastX)) {
            lastX = xField.getText();
            changed = true;
        }
        if (!yField.getText().equals(lastY)) {
            lastY = yField.getText();
            changed = true;
        }
        if (changed) {
            try {
                Vector2f newVec = new Vector2f(Float.parseFloat(lastX), Float.parseFloat(lastY));
                updateAttribute(newVec);
                addUndoRedo(lastValue, new Vector2f(newVec));
                lastValue = new Vector2f(newVec);
            } catch (NumberFormatException e) {
                // ignore
            }
        }
    }

    @Override
    protected void updateUI() {
        Object propertyValue = data.getAttribute(field);
        lastValue = Converter.getAsVector2f(propertyValue);
        if (lastValue != null) {
            xField.setText(lastValue.x + "");
            yField.setText(lastValue.y + "");
        } else {
            xField.setText("");
            yField.setText("");
        }
        lastX = xField.getText();
        lastY = yField.getText();
    }
    
    
}
