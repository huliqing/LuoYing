/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.tiles;

import com.jme3.math.Vector3f;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.converter.AbstractPropertyConverter;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.edit.JfxAbstractEdit;

/**
 *
 * @author huliqing
 */
public class Vector3fConverter extends AbstractPropertyConverter{
    
    private final Vector3f vec = new Vector3f();
    
    private final VBox layout = new VBox();
    
    private final HBox xLayout = new HBox();
    private final Label xLabel = new Label("X");
    private final TextField xField = new TextField("");
    
    private final HBox yLayout = new HBox();
    private final Label yLabel = new Label("Y");
    private final TextField yField = new TextField("");
    
    private final HBox zLayout = new HBox();
    private final Label zLabel = new Label("Z");
    private final TextField zField = new TextField("");
    
    private String lastX = "";
    private String lastY = "";
    private String lastZ = "";
    
    private boolean ignoreChangedEvent;
    private final ChangeListener<Boolean> focusedListener = (ObservableValue<? extends Boolean> observable
            , Boolean oldValue, Boolean newValue) -> {
        if (newValue) {
            return;
        }
        updateChanged();
    };
    private final EventHandler<KeyEvent> keyHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER) {
                updateChanged();
            }
        }
    };
    
    public Vector3fConverter() {
        layout.getStyleClass().add(StyleConstants.CLASS_HVBOX);
        xLayout.getStyleClass().add(StyleConstants.CLASS_HVBOX);
        yLayout.getStyleClass().add(StyleConstants.CLASS_HVBOX);
        zLayout.getStyleClass().add(StyleConstants.CLASS_HVBOX);
        
        layout.getChildren().addAll(xLayout, yLayout, zLayout);
        xLayout.getChildren().addAll(xLabel, xField);
        yLayout.getChildren().addAll(yLabel, yField);
        zLayout.getChildren().addAll(zLabel, zField);
        
        xLayout.setAlignment(Pos.CENTER_LEFT);
        yLayout.setAlignment(Pos.CENTER_LEFT);
        zLayout.setAlignment(Pos.CENTER_LEFT);
        
        xField.focusedProperty().addListener(focusedListener);
        yField.focusedProperty().addListener(focusedListener);
        zField.focusedProperty().addListener(focusedListener);
        xField.setOnKeyPressed(keyHandler);
        yField.setOnKeyPressed(keyHandler);
        zField.setOnKeyPressed(keyHandler);
        
        root.setContent(layout);
    }

    @Override
    public void initialize(JfxAbstractEdit editView, DataConverter parent, String property) {
        super.initialize(editView, parent, property);
        updateView(parent.getData().getAsVector3f(property));
    }
    
    private void updateChanged() {
        if (ignoreChangedEvent) {
            return;
        }
        boolean changed = false;
        if (!xField.getText().equals(lastX)) {
            lastX = xField.getText();
            changed = true;
        }
        if (!yField.getText().equals(lastY)) {
            lastY = yField.getText();
            changed = true;
        }
        if (!zField.getText().equals(lastZ)) {
            lastZ = zField.getText();
            changed = true;
        }
        if (changed) {
            try {
                vec.x = Float.parseFloat(lastX);
                vec.y = Float.parseFloat(lastY);
                vec.z = Float.parseFloat(lastZ);
            
                parent.getData().setAttribute(property, vec);
                notifyChangedToParent();
            } catch (NumberFormatException e) {
                // ignore
            }
        }
    }

    @Override
    public void updateView(Object propertyValue) {
        ignoreChangedEvent = true;
        Vector3f value = (Vector3f) propertyValue;
        if (value != null) {
            vec.set(value);
            xField.setText(vec.x + "");
            yField.setText(vec.y + "");
            zField.setText(vec.z + "");
        } else {
            xField.setText("");
            yField.setText("");
            zField.setText("");
        }
        lastX = xField.getText();
        lastY = yField.getText();
        lastZ = zField.getText();
        ignoreChangedEvent = false;
    }
    
    
}
