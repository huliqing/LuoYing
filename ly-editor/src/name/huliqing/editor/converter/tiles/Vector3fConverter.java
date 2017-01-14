/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.tiles;

import com.jme3.math.Vector3f;
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
public class Vector3fConverter extends AbstractPropertyConverter{
    
    private final Vector3f vec = new Vector3f();
    
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
    
    public Vector3fConverter() {
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
        
        updateView(parent.getData().getAsVector3f(property));
    }

    private void change(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (ignoreChangedEvent) {
            return;
        }
        try {
            vec.x = Float.parseFloat(x.getText());
            vec.y = Float.parseFloat(y.getText());
            vec.z = Float.parseFloat(z.getText());
            
            this.parent.getData().setAttribute(property, vec);
            
            notifyChangedToParent();
        } catch (NumberFormatException e) {
            // ignore
        }
    }

    @Override
    public void updateView(Object propertyValue) {
        ignoreChangedEvent = true;
        Vector3f value = (Vector3f) propertyValue;
        if (value != null) {
            vec.set(value);
            x.setText(vec.x + "");
            y.setText(vec.y + "");
            z.setText(vec.z + "");
        } else {
            x.setText("");
            y.setText("");
            z.setText("");
        }
        root.setContent(layout);
        ignoreChangedEvent = false;
    }
    
    
}
