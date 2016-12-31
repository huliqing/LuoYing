/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.toolview;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.tools.Tool;

/**
 *
 * @author huliqing
 */
public class ToggleToolView extends AbstractToolView {

    private final ToggleButton view = new ToggleButton();
    
    @Override
    public Node getView() {
        return view;
    }
    
    @Override
    protected void setViewActivated(boolean activated) {
        view.setSelected(activated);
    }

    @Override
    protected void setViewEnabled(boolean enabled) {
        view.setDisable(!enabled);
    }

    @Override
    public void initialize(Tool tool, Toolbar toolbar, String displayName, String icon, String tooltip) {
        super.initialize(tool, toolbar, displayName, icon, tooltip);
        view.setText(displayName);
        view.selectedProperty().addListener((ObservableValue<? extends Boolean> observable
                    , Boolean oldValue, Boolean newValue) -> {
            setActivated(newValue);
        });
        DoubleBinding size = new DoubleBinding() {
            @Override
            protected double computeValue() {return 14;}
        };
        
        // icon
        if (icon != null && !icon.equals("")) {
            Image image = new Image(getClass().getResourceAsStream(icon));
            ImageView imageView = new ImageView(image);
            imageView.fitHeightProperty().bind(size);
            imageView.fitWidthProperty().bind(size);
            view.setGraphic(imageView);
        }
        
        // tooltip
        if (tooltip != null && !tooltip.isEmpty()) {
            view.setTooltip(new Tooltip(tooltip));
        }
    }
    
}
