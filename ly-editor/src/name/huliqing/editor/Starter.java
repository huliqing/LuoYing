/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import com.jme3.system.AppSettings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import name.huliqing.editor.ui.MenuView;
import name.huliqing.fxswing.JfxSwing;

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
        
        JfxSwing js = JfxSwing.create(Editor.class.getName(), settings);
        js.getMainFrame().setLocationRelativeTo(null);
        js.getMainFrame().setVisible(true);
        js.runOnJfx(() -> {
            js.getJfxRoot().getChildren().add(createScene(js));
        });
    }

    private Node createScene(JfxSwing js) {
        VBox root = new VBox();
        root.setBackground(Background.EMPTY);
        
        // menu
        ObservableList<Node>  rootNode = root.getChildren();
        MenuView menuView = new MenuView();
        menuView.setOnQuick((e) -> {
            js.getMainFrame().dispose();
        });
        
        DoubleBinding db = js.getJfxRoot().heightProperty().subtract(menuView.heightProperty());
        SplitPane sp = new SplitPane();
        sp.setBackground(Background.EMPTY);
        sp.minHeightProperty().bind(db);
        sp.maxHeightProperty().bind(db);
        // 必须延迟一帧进行设置dividerPosition,否则无效
//        sp.setDividerPosition(0, 0.3);
        js.runOnJfx(() -> {sp.setDividerPositions(0.25);});

        VBox propertyPanel = new VBox();
        propertyPanel.setStyle("-fx-background-color:#c0c0c0;");
        propertyPanel.getChildren().add(new Label("this is Left"));
        
        SplitPane rightPane = new SplitPane();
        rightPane.setBackground(Background.EMPTY);
        rightPane.setOrientation(Orientation.VERTICAL);
        js.runOnJfx(() -> {rightPane.setDividerPositions(0.8, 0.2);});
        
        VBox editPane = new VBox();
        editPane.setBackground(Background.EMPTY);
        
        TextArea consolePane = new TextArea();
        consolePane.setPrefRowCount(10);
        
        rootNode.add(menuView);
        rootNode.add(sp);
        sp.getItems().addAll(propertyPanel, rightPane);
        rightPane.getItems().addAll(editPane, consolePane);
        
        return root;
    }
}
