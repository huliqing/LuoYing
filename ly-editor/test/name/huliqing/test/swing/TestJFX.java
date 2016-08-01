/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.test.swing;

import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import name.huliqing.editor.ui.MenuView;

/**
 *
 * @author huliqing
 */
public class TestJFX extends Application {

    @Override
    public void start(Stage s) throws Exception {
        VBox root = new VBox();
        root.setBackground(Background.EMPTY);
        Scene scene = new Scene(root, 1024, 768, Color.TRANSPARENT);
        
        ObservableList<Node>  rootNode = root.getChildren();
        MenuView menuView = new MenuView();
        rootNode.add(menuView);
        
        DoubleBinding db = scene.heightProperty().subtract(menuView.heightProperty()).subtract(-0.1);
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
        
        s.setScene(scene);
        s.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
