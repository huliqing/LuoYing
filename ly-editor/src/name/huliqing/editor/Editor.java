/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import com.jme3.system.AppSettings;
import com.jme3x.jfx.JmeFxContainer;
import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import name.huliqing.editor.App.AppListener;
import name.huliqing.editor.ui.MenuView;

/**
 *
 * @author huliqing
 */
public class Editor extends Application implements AppListener {
    
    private JmeFxContainer jfxContainer;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1024, 768);
        settings.setResizable(true);
        settings.setFrameRate(60);
        
        App app = new App(this);
        app.setSettings(settings);
        app.setPauseOnLostFocus(false);
        app.setShowSettings(false);
        app.start();
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void onInitialized(App app) {
        app.getGuiNode().detachAllChildren();
        jfxContainer = JmeFxContainer.install(app, app.getGuiNode(), false, null);
        createScene(app);
    }
    
    private Scene createScene(App app) {
        VBox root = new VBox();
        root.setBackground(Background.EMPTY);
        scene = new Scene(root, 1024, 768, Color.TRANSPARENT);
        
        ObservableList<Node>  rootNode = root.getChildren();
        MenuView menuView = new MenuView();
        menuView.setOnQuick(e -> {app.stop();});
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
        
        jfxContainer.setScene(scene);
        return scene;
    }
}
