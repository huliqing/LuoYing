/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.tiles;

import com.jme3.math.Quaternion;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import name.huliqing.editor.converter.AbstractPropertyConverter;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.edit.JfxAbstractEdit;

/**
 *
 * @author huliqing
 */
public class QuaternionConverter extends AbstractPropertyConverter{
    
    private final float[] temp = new float[3];
    private final Quaternion qua = new Quaternion();
    
    private final VBox layout = new VBox();
    
    private final HBox xLayout = new HBox();
    private final Label xl = new Label("X");
    private final TextField x = new TextField("");
    
    private final HBox yLayout = new HBox();
    private final Label yl = new Label("Y");
    private final TextField y = new TextField("");
    
    private final HBox zLayout = new HBox();
    private final Label zl = new Label("Z");
    private final TextField z = new TextField("");
    
    private boolean ignoreChangedEvent;
    
    public QuaternionConverter() {
        layout.getChildren().addAll(xLayout, yLayout, zLayout);
        xLayout.getChildren().addAll(xl, x);
        yLayout.getChildren().addAll(yl, y);
        zLayout.getChildren().addAll(zl, z);
        xLayout.setAlignment(Pos.CENTER_LEFT);
        yLayout.setAlignment(Pos.CENTER_LEFT);
        zLayout.setAlignment(Pos.CENTER_LEFT);
        
        x.textProperty().addListener(this::change);
        y.textProperty().addListener(this::change);
        z.textProperty().addListener(this::change);
        
        root.setContent(layout);
    }

    @Override
    public void initialize(JfxAbstractEdit editView, DataConverter parent, String property) {
        super.initialize(editView, parent, property);
        updateView(parent.getData().getAsQuaternion(property));
    }

    private void change(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (ignoreChangedEvent) {
            return;
        }
        try {
            temp[0] = Float.parseFloat(x.getText());
            temp[1] = Float.parseFloat(y.getText());
            temp[2] = Float.parseFloat(z.getText());
            qua.fromAngles(temp);
            this.parent.getData().setAttribute(property, qua);
            notifyChangedToParent();
        } catch (NumberFormatException e) {
            // ignore
        }
    }

    @Override
    public void updateView(Object propertyValue) {
        ignoreChangedEvent = true;
        Quaternion value = (Quaternion) propertyValue;
        if (value != null) {
            qua.set(value);
            qua.toAngles(temp);
            x.setText(temp[0] + "");
            y.setText(temp[1] + "");
            z.setText(temp[2] + "");
        } else {
            x.setText("");
            y.setText("");
            z.setText("");
        }
        ignoreChangedEvent = false;
    }
    
}
