/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import com.jme3.app.Application;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppStateManager;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.manager.UIManager;
import name.huliqing.editor.ui.utils.JfxUtils;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class FpsAppState extends StatsAppState {
    private ToggleButton statisticsIcon;
    
    public FpsAppState() {
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        setEnabled(false);
        Jfx.runOnJfx(() -> {
            statisticsIcon = new ToggleButton();
            statisticsIcon.setTooltip(new Tooltip(Manager.getRes(ResConstants.COMMON_STATISTICS)));
            statisticsIcon.prefWidth(20);
            statisticsIcon.prefHeight(20);
            statisticsIcon.setPadding(Insets.EMPTY);
            statisticsIcon.setGraphic(JfxUtils.createIcon(AssetConstants.INTERFACE_ICON_STATISTICS));
            statisticsIcon.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                Jfx.runOnJme(() -> {
                    setEnabled(newValue);
                });
            });
            UIManager.ZONE_STATUS.getChildren().addAll(statisticsIcon);
        });
    }

}
