/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.toolview;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Skin;
import javafx.scene.control.Tooltip;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.tools.CameraTool;
import name.huliqing.editor.tools.Tool;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class CameraToolView extends AbstractToolView {
    
    private final CameraCombo<String> view = new CameraCombo<>();
    private CameraTool cameraTool;

    @Override
    protected void setViewActivated(boolean activated) {
        // ignore
    }

    @Override
    protected void setViewEnabled(boolean enabled) {
        // ignore
    }

    @Override
    public Node getView() {
        return view;
    }

    @Override
    public void initialize(Tool tool, Toolbar toolbar, String displayName, String icon, String tooltip) {
        super.initialize(tool, toolbar, displayName, icon, tooltip);
        cameraTool = (CameraTool) tool;
        if (tooltip != null && !tooltip.isEmpty()) {
            view.setTooltip(new Tooltip(tooltip));
        }
        view.setName(displayName);
        view.getItems().add("当前(.)");
        view.getItems().add("原点(Shift+C)");
        view.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observable, String oldValue, String newValue) {
                Jfx.runOnJme(() -> {
                    switch (newValue) {
                        case "当前(.)":
                            cameraTool.doChaseSelected();
                            break;
                        case "原点(Shift+C)":
                            cameraTool.doChaseOrigin();
                            break;
                        default:
                            break;
                    }
                });
            }
        });
    }
    
    
    private class CameraCombo<T> extends ComboBox<T> {

        private CameraPopup pop;
        private String name;
        
        public CameraCombo() {}
        
        @Override 
        protected Skin<?> createDefaultSkin() {
            if (pop == null) {
                pop = new CameraPopup(this);
                pop.displayNode.setText(name);
            }
            return pop;
        }
        
        public void setName(String name) {
            this.name = name;
            if (pop != null) {
                pop.displayNode.setText(name);
            }
        }
    }
    
    private class CameraPopup extends ComboBoxListViewSkin {

        private final Button displayNode;

        public CameraPopup(ComboBox comboBox) {
            super(comboBox);
            displayNode = new Button("");
        }
        
        @Override
        public Node getDisplayNode() {
            return displayNode;
        }

    }

}
