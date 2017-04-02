/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
