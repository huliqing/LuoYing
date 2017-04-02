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
package name.huliqing.editor.edit;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.constants.StyleConstants;
import name.huliqing.editor.ui.utils.JfxUtils;

/**
 *
 * @author huliqing
 */
public class JfxExtToolbar extends HBox {
    
    private final HBox visibleControl = new HBox();
    private final ImageView arrowR = JfxUtils.createImage(AssetConstants.INTERFACE_COMMON_ARROW_RIGHT, 10, 10);
    private final ImageView arrowL = JfxUtils.createImage(AssetConstants.INTERFACE_COMMON_ARROW_LEFT, 10, 10);
    private final TabPane tabPane = new TabPane();
    
    public JfxExtToolbar() {
        setPadding(Insets.EMPTY);
        arrowL.managedProperty().bind(arrowL.visibleProperty());
        arrowR.managedProperty().bind(arrowR.visibleProperty());
        arrowL.setVisible(false);
        arrowR.setVisible(true);
        
        visibleControl.setStyle("-fx-background-color:rgb(140, 140, 140);");
        visibleControl.getChildren().addAll(arrowR, arrowL);
        visibleControl.setMinWidth(12);
        visibleControl.setMaxWidth(12);
        visibleControl.setAlignment(Pos.CENTER);
        visibleControl.setPadding(Insets.EMPTY);
        visibleControl.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                setToolbarVisible(!isToolbarVisible());
            }
        });

        tabPane.setSide(Side.RIGHT);
        tabPane.setPadding(Insets.EMPTY);
        tabPane.managedProperty().bind(tabPane.visibleProperty());
        getChildren().addAll(visibleControl, tabPane);
        
        getStyleClass().add(StyleConstants.CLASS_HVBOX);
        
        visibleControl.prefHeightProperty().bind(heightProperty());
        tabPane.prefHeightProperty().bind(heightProperty());
        tabPane.setPrefWidth(250);
    }
    
    public void addToolbar(String name, Region toolbar) {
        Tab tab = new Tab();
        tab.setText(name);
        tab.setClosable(false);
        tab.setContent(toolbar);
        tabPane.getTabs().add(tab);
    }
    
    public boolean removeToolbar(String name) {
        Tab found = null;
        for (Tab tab : tabPane.getTabs()) {
            if (name.equals(tab.getText())) {
                found = tab;
                break;
            }
        }
        if (found != null) {
            tabPane.getTabs().remove(found);
            return true;
        }
        return false;
    }
    
    public boolean isToolbarVisible() {
        return tabPane.isVisible();
    }
    
    /**
     * 设置扩展工具栏是否可见
     * @param visible 
     */
    public void setToolbarVisible(boolean visible) {
        tabPane.setVisible(visible);
        arrowR.setVisible(tabPane.isVisible());
        arrowL.setVisible(!tabPane.isVisible());
    }
    
}
