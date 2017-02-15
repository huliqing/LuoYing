/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
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
    // 文本区
    private Region textZone;
    // 状态栏
    private Region statusBar;
    
    // 根节点面板
    private final Pane root;
    // 主体区
    private final SplitPane contentMainSp = new SplitPane();
    private final SplitPane sp2 = new SplitPane();
    private final double[] lastDividerPositions = new double[]{0.8};
    
    public MainLayout(Pane root) {
        setBackground(Background.EMPTY);
        this.root = root;
        // 固定的设置，不能动态改变
        contentMainSp.setBackground(Background.EMPTY);
        // 把padding设置为0,不然在边缘会有一些细微空隙
        contentMainSp.setPadding(Insets.EMPTY);
        
        sp2.setBackground(Background.EMPTY);
        sp2.setPadding(Insets.EMPTY);
        sp2.setDividerPositions(lastDividerPositions);
    }
    
    /**
     * 设置各个区域的组件
     * @param menuBar 菜单栏 
     * @param resourceZone 属性区域 
     * @param editZone 主编辑区
     * @param statusBar
     * @param textZone
     */
    public void setZones(Region menuBar, Region resourceZone, Region editZone, Region statusBar, Region textZone) {
        this.menuZone = menuBar;
        this.resourceZone = resourceZone; 
        this.editZone = editZone;
        this.statusBar = statusBar;
        this.textZone = textZone;
        buildLayout();
    }
    
    public void setTextZoneVisible(boolean visible) {
        if (visible) {
            if (!sp2.getItems().contains(textZone)) {
                sp2.getItems().add(textZone);
                sp2.setDividerPositions(lastDividerPositions);
            }
        } else {
            sp2.getItems().remove(textZone);
        }
    }
    
    private void buildLayout() {
        getChildren().clear();
        contentMainSp.getItems().clear();
        
        getChildren().addAll(menuZone, contentMainSp, statusBar);
        contentMainSp.getItems().addAll(sp2, resourceZone);
        sp2.getItems().addAll(editZone);
        sp2.setOrientation(Orientation.VERTICAL);
        SplitPane.setResizableWithParent(textZone, Boolean.FALSE);
        
        // -- zone size
        prefHeightProperty().bind(root.heightProperty());
        prefWidthProperty().bind(root.widthProperty());
        contentMainSp.prefHeightProperty().bind(heightProperty().subtract(menuZone.heightProperty()).subtract(statusBar.heightProperty()));
        resourceZone.prefHeightProperty().bind(contentMainSp.heightProperty());
        editZone.prefHeightProperty().bind(contentMainSp.heightProperty());
        statusBar.prefWidthProperty().bind(widthProperty());
        
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
