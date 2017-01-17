/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.tiles;

import com.jme3.math.ColorRGBA;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import name.huliqing.editor.converter.AbstractPropertyConverter;
import name.huliqing.luoying.xml.Converter;

/**
 *
 * @author huliqing
 */
public class ColorFieldConverter extends AbstractPropertyConverter {

    private final ColorPicker layout = new ColorPicker();
    
    public ColorFieldConverter() {
        layout.prefWidthProperty().bind(root.widthProperty());
        layout.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                updateAttribute(toJmeColor(newValue));
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
    protected Node createLayout() {
        return layout;
    }
    
    @Override
    protected void updateUI(Object propertyValue) {
        ColorRGBA jmeColor = Converter.getAsColor(propertyValue);
        layout.setValue(toJfxColor(jmeColor));
    }
    
    private Color toJfxColor(ColorRGBA jmeColor) {
        if (jmeColor == null) {
            return new Color(1,1,1,1);
        }
        return new Color(jmeColor.r, jmeColor.g, jmeColor.b, jmeColor.a);
    }
    
    private ColorRGBA toJmeColor(Color color) {
        return new ColorRGBA((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getOpacity());
    }
}
