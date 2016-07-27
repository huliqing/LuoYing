/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.test;

import name.huliqing.editor.fxjme.JfxView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import name.huliqing.editor.fxjme.JfxSystem;

/**
 *
 * @author huliqing
 */
public class TestJfx extends Application {
    
    private JfxView jfxView;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        jfxView = JfxSystem.startApp(TestEditorApp.class.getName(), 400, 400);
        
        Button btn = new Button();
        btn.setText("Stop JFX Application");
        btn.setOnAction(e -> {
            Platform.exit();
        });
        
        VBox root = new VBox();
        root.getChildren().add(btn);
        root.getChildren().add(jfxView);
        Scene scene = new Scene(root, 640, 480);
        
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
