/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.tool;

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
    public Node createView() {
        return view;
    }

    @Override
    protected void setViewEnabled(boolean enabled) {
        view.setSelected(enabled);
    }

    @Override
    public void initialize() {
        super.initialize();
        view.setText(tool.getName());
        view.selectedProperty().addListener((ObservableValue<? extends Boolean> observable
                    , Boolean oldValue, Boolean newValue) -> {
            setEnabled(newValue);
        });
        
        // tooltip
        if (tool.getTips() != null) {
            view.setTooltip(new Tooltip(tool.getTips()));
        }
        
        // icon
        if (tool.getIcon() != null) {
            Image image = new Image(getClass().getResourceAsStream("/" + tool.getIcon()));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(14);
            imageView.setFitHeight(14);
            view.setGraphic(imageView);
        }
        
        if (tool.isInitialized()) {
            setViewEnabled(true);
        }
    }
    
}
