/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.Statistics;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.manager.UIManager;
import name.huliqing.editor.utils.JfxUtils;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class EditorStatsAppState extends AbstractAppState {

    private Statistics stat;
    private String[] labels;
    private int[] datas;
    private final StringBuilder sb = new StringBuilder();
    
    private final Label label = new Label();
    private ToggleButton statisticsIcon;
    
    public EditorStatsAppState() {
        setEnabled(false);
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        stat = app.getRenderer().getStatistics();
        Jfx.runOnJfx(() -> {
            label.setVisible(false);
            statisticsIcon = new ToggleButton();
            statisticsIcon.setTooltip(new Tooltip(Manager.getRes(ResConstants.COMMON_STATISTICS)));
            statisticsIcon.prefWidth(20);
            statisticsIcon.prefHeight(20);
            statisticsIcon.setPadding(Insets.EMPTY);
            statisticsIcon.setGraphic(JfxUtils.createImage(AssetConstants.INTERFACE_ICON_STATISTICS, 20, 20));
            statisticsIcon.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                label.setVisible(newValue);
                setEnabled(newValue);
            });
            UIManager.ZONE_STATUS.getChildren().addAll(statisticsIcon, label);
        });
        labels = stat.getLabels();
        datas = new int[labels.length];
        stat.setEnabled(isEnabled());
    }

    @Override
    public void cleanup() {
        Jfx.runOnJfx(() -> {
            UIManager.ZONE_STATUS.getChildren().removeAll(statisticsIcon, label);
        });
        super.cleanup();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled); 
        if (stat != null) {
            stat.setEnabled(enabled);
        }
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        stat.getData(datas);
        sb.setLength(0);

        for (int i = 0; i < labels.length; i++) {
            sb.append(labels[i]).append("=").append(datas[i]).append(" | ");
        }
        final String str = sb.toString();
        Jfx.runOnJfx(() -> label.setText(str));
        
    }
    
    

}
