/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.layout;

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
public class SceneEditLayout extends VBox {
    
    private final SplitPane topZone = new SplitPane();
    private final StackPane zoneMain = new StackPane();
    
    private Region zoneEdit;
    private Region zoneProperty;
    private Region zoneComponents;
    private Region zoneToolbar;
    
    public SceneEditLayout() {
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
        zoneToolbar.setPrefHeight(30);
        topZone.prefHeightProperty().bind(heightProperty().subtract(zoneToolbar.heightProperty()));
        zoneMain.prefWidthProperty().bind(topZone.widthProperty().subtract(zoneComponents.widthProperty()));
        zoneComponents.setMinWidth(200);
        zoneComponents.setMaxWidth(200);
        Jfx.runOnJfx(() -> {
            zoneComponents.setMinWidth(0);
            zoneComponents.setMaxWidth(99999);
            SplitPane.setResizableWithParent(zoneComponents, Boolean.FALSE);
        });
    }
}
