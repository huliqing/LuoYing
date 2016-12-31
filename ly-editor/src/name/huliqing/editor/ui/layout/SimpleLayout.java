/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.layout;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class SimpleLayout extends VBox {
    
    // 根节点面板
    private final Pane root;
    private Region menuBar;
    private Region property;
    private Region edit;
    private Region toolbar;
    
    // 包含除menuBar外的区域
    private final SplitPane contentZone = new SplitPane();
    private final ScrollPane leftZone = new ScrollPane();
    private final VBox rightZone = new VBox();
    
    public SimpleLayout(Pane root) {
        this.root = root;
        // 固定的设置，不能动态改变
        contentZone.setBackground(Background.EMPTY);
        contentZone.setStyle("-fx-padding:0;"); // 把padding设置为0,不然在边缘会有一些空隙
        
        // 其它不需要处理
//        contentZone.setStyle("-fx-padding:0;-fx-border-style: solid inside;"
//                + "-fx-border-width:0;-fx-border-insets:0;-fx-border-radius:0;-fx-border-color:blue;");
    }
    
    /**
     * 设置各个区域的组件
     * @param menuBar 菜单栏 
     * @param property 属性区域 
     * @param edit 主编辑区
     * @param toolbar 工具栏
     */
    public void setZones(Region menuBar, Region property, Region edit, Region toolbar) {
        this.menuBar = menuBar;
        this.property = property;
        this.edit = edit;
        this.toolbar = toolbar;
        resetLayout();
    }
    
    public void resetLayout() {
        if (menuBar == null 
                || property== null
                || edit== null
                || toolbar== null
                ) {
            return;
        }
        
        getChildren().clear();
        getChildren().addAll(menuBar, contentZone);
        contentZone.getItems().clear();
        leftZone.setContent(null);
        rightZone.getChildren().clear();
        
        contentZone.getItems().addAll(leftZone, rightZone);
        
        leftZone.setContent(property);
        rightZone.getChildren().addAll(edit, toolbar);
        
        // -- zone size
        this.minHeightProperty().bind(root.heightProperty());
        this.minWidthProperty().bind(root.widthProperty());
        
        DoubleBinding contentHeight = root.heightProperty().subtract(menuBar.heightProperty());
        contentZone.minHeightProperty().bind(contentHeight);
        contentZone.maxHeightProperty().bind(contentHeight);
        
        leftZone.minHeightProperty().bind(contentZone.heightProperty());
        leftZone.maxHeightProperty().bind(contentZone.heightProperty());
        
        rightZone.minHeightProperty().bind(contentZone.heightProperty());
        rightZone.maxHeightProperty().bind(contentZone.heightProperty());
        
        edit.minHeightProperty().bind(rightZone.heightProperty().subtract(toolbar.heightProperty()));
        
        // 必须延迟一帧进行设置dividerPosition,否则无效
        Jfx.runOnJfx(() -> {contentZone.setDividerPositions(0.2);});
        
    }
}
