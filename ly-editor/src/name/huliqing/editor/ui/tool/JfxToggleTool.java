/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.tool;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author huliqing
 */
public class JfxToggleTool extends JfxAbstractTool {

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
    public void initialize() {
        view.setText(tool.getName());
        view.selectedProperty().addListener((ObservableValue<? extends Boolean> observable
                    , Boolean oldValue, Boolean newValue) -> {
            setActivated(newValue);
        });
        DoubleBinding size = new DoubleBinding() {
            @Override
            protected double computeValue() {return 14;}
        };
        
        // tooltip
        if (tool.getTips() != null) {
            view.setTooltip(new Tooltip(tool.getTips()));
        }
        
        // icon
        if (tool.getIcon() != null) {
            Image image = new Image(getClass().getResourceAsStream("/" + tool.getIcon()));
            ImageView imageView = new ImageView(image);
            imageView.fitHeightProperty().bind(size);
            imageView.fitWidthProperty().bind(size);
            view.setGraphic(imageView);
        }
        
    }
    
}
