/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.test;

import com.jme3.math.FastMath;
import com.jme3.system.AppSettings;
import javafx.animation.AnimationTimer;
import name.huliqing.fxjme.JfxView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import name.huliqing.fxjme.JfxSystem;

/**
 *
 * @author huliqing
 */
public class TestJfx extends Application {
    
    private JfxView jfxView;
    
    private final int width = 1024;
    private final int height = 768;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        AppSettings settings = new AppSettings(true);
        settings.setResolution(width, height);
        settings.setFrameRate(60);
        
        jfxView = JfxSystem.startApp(TestEditorApp.class.getName(), settings);
        
        Button btn = new Button();
        btn.setText("Stop JFX Application");
        btn.setOnAction(e -> {
            Platform.exit();
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(jfxView);
        root.getChildren().add(btn);
        
        Scene scene = new Scene(root, width, height);
        
        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.show();

        
    }

    @Override
    public void stop() throws Exception {
        // Remember to stop the jme application.
        jfxView.getApplication().stop();
        super.stop();
    }
    
    
}
