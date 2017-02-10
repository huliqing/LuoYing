/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import name.huliqing.editor.constants.StyleConstants;

/**
 *
 * @author huliqing
 */
public class StatusBar extends HBox {
    
    public StatusBar() {
        setPadding(new Insets(0, 3, 0, 3));
        setMinHeight(25);
        setMaxHeight(25);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add(StyleConstants.CLASS_STATUS_BAR);
    }
    
}
