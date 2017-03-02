/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.field;

import com.jme3.math.Quaternion;
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
public class QuaternionConverter extends SimpleFieldConverter {
    
    private final float[] temp = new float[3];
    
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
    private Quaternion lastValueSaved;
    
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
    
    public QuaternionConverter() {
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
        
        xLabel.setMinWidth(15);
        yLabel.setMinWidth(15);
        zLabel.setMinWidth(15);
        layout.setSpacing(3);
    }

    @Override
    protected Node createLayout() {
        return layout;
    }
    
    private void updateChanged() {
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
                temp[0] = Float.parseFloat(lastX);
                temp[1] = Float.parseFloat(lastY);
                temp[2] = Float.parseFloat(lastZ);
                Quaternion qua = new Quaternion();
                qua.fromAngles(temp);
                updateAttribute(qua);
                addUndoRedo(lastValueSaved, new Quaternion(qua));
                lastValueSaved = new Quaternion(qua);
            } catch (NumberFormatException e) {
                // ignore
            }
        }
    }

    @Override
    protected void updateUI() {
        Object propertyValue = data.getAttribute(field);
        lastValueSaved = Converter.getAsQuaternion(propertyValue);
        if (lastValueSaved != null) {
            lastValueSaved.toAngles(temp);
            xField.setText(temp[0] + "");
            yField.setText(temp[1] + "");
            zField.setText(temp[2] + "");
        } else {
            xField.setText("");
            yField.setText("");
            zField.setText("");
        }
        lastX = xField.getText();
        lastY = yField.getText();
        lastZ = zField.getText();
    }
    
}
