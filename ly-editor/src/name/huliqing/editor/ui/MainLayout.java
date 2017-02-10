/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import javafx.geometry.Insets;
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
    
    // 菜单区
    private Region menuZone;
    // 资源区
    private Region resourceZone;
    // 编辑区
    private Region editZone;
    // 状态栏
    private Region statusBar;
    
    // 根节点面板
    private final Pane root;
    // 主体区
    private final SplitPane contentZone = new SplitPane();
    
    public MainLayout(Pane root) {
        this.root = root;
        // 固定的设置，不能动态改变
        contentZone.setBackground(Background.EMPTY);
        // 把padding设置为0,不然在边缘会有一些细微空隙
        contentZone.setPadding(Insets.EMPTY);
    }
    
    /**
     * 设置各个区域的组件
     * @param menuBar 菜单栏 
     * @param resourceZone 属性区域 
     * @param editZone 主编辑区
     * @param statusBar
     */
    public void setZones(Region menuBar, Region resourceZone, Region editZone, Region statusBar) {
        this.menuZone = menuBar;
        this.resourceZone = resourceZone; 
        this.editZone = editZone;
        this.statusBar = statusBar;
        buildLayout();
    }
    
    private void buildLayout() {
        getChildren().clear();
        getChildren().addAll(menuZone, contentZone, statusBar);
        contentZone.getItems().clear();
        contentZone.getItems().addAll(editZone, resourceZone);
        
        // -- zone size
        prefHeightProperty().bind(root.heightProperty());
        prefWidthProperty().bind(root.widthProperty());
        contentZone.prefHeightProperty().bind(heightProperty().subtract(menuZone.heightProperty()).subtract(statusBar.heightProperty()));
        resourceZone.prefHeightProperty().bind(contentZone.heightProperty());
        editZone.prefHeightProperty().bind(contentZone.heightProperty());
        statusBar.prefWidthProperty().bind(widthProperty());
        
        // remove20170107,起到效果，原因不明，可能和JFXPanel与Swing的整合有关。必须使用下面的特殊方式处理:
//        contentZone.setDividerPositions(0.2f);
//        SplitPane.setResizableWithParent(resourceZone, Boolean.FALSE);
        
        // 特殊方式限制resourceZone在初始化的时候为200的宽度,
        // 并在延迟一帧后去除resourceZone限制并取消自动宽度, 这样resourceZone不会随着父窗口的放大而拉大。
        resourceZone.setMinWidth(220);
        resourceZone.setMaxWidth(220);
        Jfx.runOnJfx(() -> {
            resourceZone.setMinWidth(0);
            resourceZone.setMaxWidth(Integer.MAX_VALUE);
            SplitPane.setResizableWithParent(resourceZone, Boolean.FALSE);
        });
    }
    
}
