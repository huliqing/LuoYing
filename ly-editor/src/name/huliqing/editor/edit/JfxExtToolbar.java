/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import name.huliqing.editor.constants.AssetConstants;
import name.huliqing.editor.utils.JfxUtils;

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
        arrowL.managedProperty().bind(arrowL.visibleProperty());
        arrowR.managedProperty().bind(arrowR.visibleProperty());
        arrowL.setVisible(false);
        arrowR.setVisible(true);
        
        visibleControl.setStyle("-fx-background-color:lightgray;");
        visibleControl.getChildren().addAll(arrowR, arrowL);
        visibleControl.setMinWidth(10);
        visibleControl.setMaxWidth(10);
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
        setPadding(Insets.EMPTY);
    }
    
    public void addToolbar(String name, Node toolbar) {
        Tab tab = new Tab();
        tab.setText(name);
        tab.setClosable(false);
        
        // 使用一个Layout来统一设置padding,不能直接设置在TabPane或Tab上。
        HBox layout = new HBox();
        layout.setPadding(new Insets(5));
        layout.getChildren().add(toolbar);
        tab.setContent(layout);
        
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
