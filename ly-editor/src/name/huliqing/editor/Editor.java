/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import name.huliqing.editor.ui.MenuView;
import name.huliqing.fxswing.JfxSwing;
import name.huliqing.fxswing.JmeAppTest;

/**
 *
 * @author huliqing
 */
public class Editor {
    
    private Scene scene;
    
    public static void main(String[] args) {
        new Editor().start();
    }
    
    private void start() {
        JfxSwing js = JfxSwing.create(JmeAppTest.class.getName(), 640, 480);
        js.getMainFrame().setLocationRelativeTo(null);
        js.getMainFrame().setVisible(true);
        js.runOnJfx(() -> {
            js.getJfxRoot().getChildren().add(createScene(js));
        });
    }

    private Node createScene(JfxSwing jfxSwing) {
        VBox root = new VBox();
        root.setBackground(Background.EMPTY);
        root.setEffect(new DropShadow());
        
        ObservableList<Node>  rootNode = root.getChildren();
        MenuView menuView = new MenuView();
        menuView.setOnQuick((e) -> {
            jfxSwing.getMainFrame().dispose();
        });
        rootNode.add(menuView);
        
        DoubleBinding db = jfxSwing.getJfxRoot().heightProperty().subtract(menuView.heightProperty().subtract(3f));
        SplitPane sp = new SplitPane();
        sp.setBackground(Background.EMPTY);
        sp.minHeightProperty().bind(db);
        sp.maxHeightProperty().bind(db);
//        sp.setDividerPositions(0, 0.3);
        rootNode.add(sp);

        VBox left = new VBox();
        left.setStyle("-fx-background-color:#c0c0c0;");
        left.getChildren().add(new Label("this is Left"));
        
        SplitPane rightPane = new SplitPane();
        rightPane.setBackground(Background.EMPTY);
        rightPane.setOrientation(Orientation.VERTICAL);
        sp.getItems().addAll(left, rightPane);
        
        VBox editPane = new VBox();
        editPane.setBackground(Background.EMPTY);
        
        TextArea consolePane = new TextArea();
        consolePane.setPrefRowCount(10);
        rightPane.getItems().addAll(editPane, consolePane);
        
        return root;
    }
}
