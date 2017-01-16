/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.tiles;

import com.jme3.math.ColorRGBA;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import name.huliqing.editor.converter.AbstractPropertyConverter;
import name.huliqing.editor.converter.DataConverter;
import name.huliqing.editor.edit.JfxAbstractEdit;

/**
 *
 * @author huliqing
 */
public class ColorFieldConverter extends AbstractPropertyConverter {

    private final ColorPicker cp = new ColorPicker();
    private boolean ignoreChangedEvent;
    
    public ColorFieldConverter() {
        root.setContent(cp);
        cp.prefWidthProperty().bind(root.widthProperty());
        cp.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                updateChanged(newValue);
            }
        });
//        cp.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("ActionEvent, color=" + cp.getValue() + ", et=" + event.getEventType());
//            }
//        });
    }
    
    @Override
    public void initialize(JfxAbstractEdit editView, DataConverter parent, String property) {
        super.initialize(editView, parent, property);
        ColorRGBA jmeColor = parent.getData().getAsColor(property);
        if (jmeColor != null) {
            updateView(jmeColor);
        }
    }
    
    public void updateChanged(Color newValue) {
        if (ignoreChangedEvent) {
            return;
        }
        ColorRGBA jmeColor = toJmeColor(newValue);
        parent.getData().setAttribute(property, jmeColor);
        parent.notifyChangedToParent();
    }
    
    @Override
    public void updateView(Object propertyValue) {
        ignoreChangedEvent = true;
        ColorRGBA jmeColor = (ColorRGBA) propertyValue;
        cp.setValue(toJfxColor(jmeColor));
        ignoreChangedEvent = false;
    }
    
    private Color toJfxColor(ColorRGBA jmeColor) {
        return new Color(jmeColor.r, jmeColor.g, jmeColor.b, jmeColor.a);
    }
    
    private ColorRGBA toJmeColor(Color color) {
        return new ColorRGBA((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getOpacity());
    }
}
