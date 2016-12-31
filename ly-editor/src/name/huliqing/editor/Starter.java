/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import com.jme3.system.AppSettings;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import name.huliqing.editor.ui.MenuView;
import name.huliqing.editor.ui.ToolBarView;
import name.huliqing.editor.ui.layout.SimpleLayout;
import name.huliqing.fxswing.Jfx;

/**
 *
 * @author huliqing
 */
public class Starter {
    
    private Scene scene;
    
    public static void main(String[] args) {
        new Starter().start();
    }
    
    private void start() {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1280, 720);
        settings.setFrameRate(60);
        settings.setSamples(4);
        
        Jfx.create(Editor.class.getName(), settings);
        Jfx.getMainFrame().setLocationRelativeTo(null);
        Jfx.getMainFrame().setVisible(true);
        Jfx.runOnJfx(() -> {
            Jfx.getJfxRoot().getChildren().add(createScene());
        });
    }
    
    private Node createScene() {
        MenuView menuView = new MenuView();
        VBox propertyPanel = new VBox();
        VBox editPane = new VBox();
        editPane.setBackground(Background.EMPTY);
        ToolBarView toolbarView = new ToolBarView();
        
        SimpleLayout layout = new SimpleLayout(Jfx.getJfxRoot());
        layout.setZones(menuView,propertyPanel,editPane,toolbarView);
        return layout;
    }
 
    
//    private Node createScene() {
//        VBox root = new VBox();
//        root.setBackground(Background.EMPTY);
//        
//        // menu
//        ObservableList<Node>  rootNode = root.getChildren();
//        MenuView menuView = new MenuView();
//        menuView.setOnQuick((e) -> {
//            Jfx.getMainFrame().dispose();
//        });
//        
//        DoubleBinding db = Jfx.getJfxRoot().heightProperty().subtract(menuView.heightProperty());
//        SplitPane sp = new SplitPane();
//        sp.setBackground(Background.EMPTY);
//        sp.minHeightProperty().bind(db);
//        sp.maxHeightProperty().bind(db);
//        // 必须延迟一帧进行设置dividerPosition,否则无效
////        sp.setDividerPosition(0, 0.3);
//        Jfx.runOnJfx(() -> {sp.setDividerPositions(0.2);});
//        
//        // 属性面板
//        VBox propertyPanel = new VBox();
//        propertyPanel.setStyle("-fx-background-color:#c0c0c0;");
//        propertyPanel.getChildren().add(new Label(""));
//        
//        // 右面板
//        SplitPane rightPane = new SplitPane();
//        rightPane.setBackground(Background.EMPTY);
//        rightPane.setOrientation(Orientation.VERTICAL);
//        Jfx.runOnJfx(() -> {rightPane.setDividerPositions(0.8, 0.2);});
//        
//        VBox editPane = new VBox();
//        editPane.setBackground(Background.EMPTY);
//        
//        ToolBarView toolbarView = new ToolBarView();
//        
////        TextArea consolePane = new TextArea();
////        consolePane.setPrefRowCount(10);
//        
//        rootNode.add(menuView);
//        rootNode.add(sp);
//        sp.getItems().addAll(propertyPanel, rightPane);
//        
//        rightPane.getItems().addAll(editPane, toolbarView);
//        
//        return root;
//    }
}
