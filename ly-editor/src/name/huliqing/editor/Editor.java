/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import com.jme3.system.AppSettings;
import com.jme3x.jfx.JmeFxContainer;
import com.jme3x.jfx.JmeFxScreenContainer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import name.huliqing.editor.App.AppListener;

/**
 *
 * @author huliqing
 */
public class Editor extends Application implements AppListener {

    @Override
    public void start(Stage primaryStage) throws Exception {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(640, 480);
        settings.setResizable(true);
        
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
        StackPane root = new StackPane();
        root.getChildren().add(new Button("Hello JFX"));
        root.setBackground(Background.EMPTY);
        
        JmeFxContainer.install(app, app.getGuiNode(), false, null).setScene(new Scene(root, Color.TRANSPARENT));
    }
}
