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
import javafx.util.StringConverter;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.ConfigManager;
import name.huliqing.editor.toolbar.Toolbar;
import name.huliqing.editor.tools.CameraTool;
import name.huliqing.editor.tools.Tool;
import name.huliqing.editor.utils.BestEditCamera;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class CameraToolView extends AbstractToolView {
    
    private final CameraCombo<ActionItem> view = new CameraCombo<>();
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
    public void initialize(Tool tool, Toolbar toolbar, String name, String tooltip, String icon) {
        super.initialize(tool, toolbar, name, tooltip, icon);
        cameraTool = (CameraTool) tool;
        view.setName(name);
        if (tooltip != null && !tooltip.isEmpty()) {
            view.setTooltip(new Tooltip(tooltip));
        }
        
        view.setConverter(new ActionItemConverter());
        view.getItems().add(new ActionItem(ConfigManager.getRes(ResConstants.VIEW_FOCUS_ORIGIN), 0));
        view.getItems().add(new ActionItem(ConfigManager.getRes(ResConstants.VIEW_FOCUS_TARGET), 1));
        
        view.getItems().add(new ActionItem(ConfigManager.getRes(ResConstants.VIEW_BACK), 11));
        view.getItems().add(new ActionItem(ConfigManager.getRes(ResConstants.VIEW_BOTTOM), 12));
        view.getItems().add(new ActionItem(ConfigManager.getRes(ResConstants.VIEW_FRONT), 13));
        view.getItems().add(new ActionItem(ConfigManager.getRes(ResConstants.VIEW_LEFT), 14));
        view.getItems().add(new ActionItem(ConfigManager.getRes(ResConstants.VIEW_RIGHT), 15));
        view.getItems().add(new ActionItem(ConfigManager.getRes(ResConstants.VIEW_TOP), 16));
        
        view.getItems().add(new ActionItem(ConfigManager.getRes(ResConstants.VIEW_ORTHO), 20));
        view.getItems().add(new ActionItem(ConfigManager.getRes(ResConstants.VIEW_PERSP), 21));

        view.valueProperty().addListener(new ChangeListener<ActionItem>() {
            
            @Override
            public void changed(ObservableValue observable, ActionItem oldValue, ActionItem newValue) {
                Jfx.runOnJme(() -> {
                    switch (newValue.value) {
                        case 0:
                            cameraTool.doChaseOrigin();
                            break;
                        case 1:
                            cameraTool.doChaseSelected();
                            break;
                        case 11:
                            cameraTool.doSwitchView(BestEditCamera.View.back);
                            break;
                        case 12:
                            cameraTool.doSwitchView(BestEditCamera.View.bottom);
                            break;
                        case 13:
                            cameraTool.doSwitchView(BestEditCamera.View.front);
                            break;
                        case 14:
                            cameraTool.doSwitchView(BestEditCamera.View.left);
                            break;
                        case 15:
                            cameraTool.doSwitchView(BestEditCamera.View.right);
                            break;
                        case 16:
                            cameraTool.doSwitchView(BestEditCamera.View.top);
                            break;
                        case 20:
                            cameraTool.doOrthoView();
                            break;
                        case 21:
                            cameraTool.doPerspView();
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

    private class ActionItemConverter<T extends ActionItem>  extends StringConverter<T> {

        @Override
        public String toString(T object) {
            return object.name;
        }

        @Override
        public T fromString(String string) {
            ActionItem item = new ActionItem();
            item.name = string;
            return (T)item;
        }
        
    }
    
    private class ActionItem {
        public String name;
        public int value;
        
        public ActionItem() {}
        public ActionItem(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }
}
