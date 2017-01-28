/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class MainLayout extends VBox {
    
    // 根节点面板
    private final Pane root;
    
    // menu区
    private Region menuBar;
    // 主体区
    private final SplitPane contentZone = new SplitPane();
    
    // 主体区中的“资源区”和“编辑区”
    private Region resourceZone;
    private Region editZone;
    
    public MainLayout(Pane root) {
        this.root = root;
        // 固定的设置，不能动态改变
        contentZone.setBackground(Background.EMPTY);
        // 把padding设置为0,不然在边缘会有一些细微空隙
        contentZone.setStyle("-fx-padding:0;");
        // 这些样式不需要设置，默认即可
//        contentZone.setStyle("-fx-padding:0;-fx-border-style: solid inside;"
//                + "-fx-border-width:0;-fx-border-insets:0;-fx-border-radius:0;-fx-border-color:blue;");
    }
    
    /**
     * 设置各个区域的组件
     * @param menuBar 菜单栏 
     * @param resourceZone 属性区域 
     * @param editZone 主编辑区
     */
    public void setZones(Region menuBar, Region resourceZone, Region editZone) {
        this.menuBar = menuBar;
        this.resourceZone = resourceZone; 
        this.editZone = editZone;
        buildLayout();
    }
    
    private void buildLayout() {
        getChildren().clear();
        getChildren().addAll(menuBar, contentZone);
        contentZone.getItems().clear();
        contentZone.getItems().addAll(editZone, resourceZone);
        
        // -- zone size
        prefHeightProperty().bind(root.heightProperty());
        prefWidthProperty().bind(root.widthProperty());
        contentZone.prefHeightProperty().bind(heightProperty().subtract(menuBar.heightProperty()));
        resourceZone.prefHeightProperty().bind(contentZone.heightProperty());
        editZone.prefHeightProperty().bind(contentZone.heightProperty());
        
        // remove20170107,起到效果，原因不明，可能和JFXPanel与Swing的整合有关。必须使用下面的特殊方式处理:
//        contentZone.setDividerPositions(0.2f);
//        SplitPane.setResizableWithParent(resourceZone, Boolean.FALSE);
        
        // 特殊方式限制resourceZone在初始化的时候为200的宽度,
        // 并在延迟一帧后去除resourceZone限制并取消自动宽度, 这样resourceZone不会随着父窗口的放大而拉大。
        resourceZone.setMinWidth(250);
        resourceZone.setMaxWidth(250);
        Jfx.runOnJfx(() -> {
            resourceZone.setMinWidth(0);
            resourceZone.setMaxWidth(Integer.MAX_VALUE);
            SplitPane.setResizableWithParent(resourceZone, Boolean.FALSE);
        });
    }
    
}
