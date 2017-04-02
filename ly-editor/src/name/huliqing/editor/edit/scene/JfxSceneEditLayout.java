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
package name.huliqing.editor.edit.scene;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class JfxSceneEditLayout extends VBox {
    
    private final SplitPane topZone = new SplitPane();
    private final StackPane zoneMain = new StackPane();
    
    private Region zoneEdit;
    private Region zoneProperty;
    private Region zoneComponents;
    private Region zoneToolbar;
    
    public JfxSceneEditLayout() {
    }
    
    public void setZoneEdit(Region zoneEdit) {
        this.zoneEdit = zoneEdit;
    }
    
    public void setZoneProperty(Region zoneProperty) {
        this.zoneProperty = zoneProperty;
    }
    
    public void setZoneComponents(Region zoneComponents) {
        this.zoneComponents = zoneComponents;
    }
    
    public void setZoneToolbar(Region zoneToolbar) {
        this.zoneToolbar = zoneToolbar;
    }
    
    public void buildLayout() {
        getChildren().clear();
        getChildren().addAll(topZone, zoneToolbar);
        
        topZone.getItems().addAll(zoneMain, zoneComponents);
        // zoneEdit应该放在zoneProperty上面，因为需要响应一些鼠标事件，如拖放操作
        zoneMain.getChildren().addAll(zoneProperty, zoneEdit); 
        
        topZone.setBackground(Background.EMPTY);
        zoneMain.setBackground(Background.EMPTY);
//        zoneToolbar.setPrefHeight(30);
        topZone.prefHeightProperty().bind(heightProperty().subtract(zoneToolbar.heightProperty()));
        zoneMain.prefWidthProperty().bind(topZone.widthProperty().subtract(zoneComponents.widthProperty()));
        zoneComponents.setMinWidth(200);
        zoneComponents.setMaxWidth(200);
        
        zoneProperty.prefHeightProperty().bind(zoneMain.heightProperty());
        
        Jfx.runOnJfx(() -> {
            zoneComponents.setMinWidth(0);
            zoneComponents.setMaxWidth(99999);
            SplitPane.setResizableWithParent(zoneComponents, Boolean.FALSE);
        });
    }
}
