/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.ui.layout;

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
    private Region assetsZone;
    private Region editZone;
    
    public MainLayout(Pane root) {
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
     * @param assetsZone 属性区域 
     * @param editZone 主编辑区
     */
    public void setZones(Region menuBar, Region assetsZone, Region editZone) {
        this.menuBar = menuBar;
        this.assetsZone = assetsZone;
        this.editZone = editZone;
        buildLayout();
    }
    
    private void buildLayout() {
        getChildren().clear();
        getChildren().addAll(menuBar, contentZone);
        contentZone.getItems().clear();
        contentZone.getItems().addAll(assetsZone, editZone);
        
        // -- zone size
        minHeightProperty().bind(root.heightProperty());
        minWidthProperty().bind(root.widthProperty());
        
        contentZone.minHeightProperty().bind(heightProperty().subtract(menuBar.heightProperty()));
        
        assetsZone.minHeightProperty().bind(contentZone.heightProperty());
        editZone.minHeightProperty().bind(contentZone.heightProperty());
        
        // 必须延迟一帧进行设置dividerPosition,否则无效
        Jfx.runOnJfx(() -> {contentZone.setDividerPositions(0.2);});
        
    }
    
}
