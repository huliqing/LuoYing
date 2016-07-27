/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.test;

import name.huliqing.editor.fxjme.JfxAppState;
import name.huliqing.editor.fxjme.JfxView;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import name.huliqing.editor.fxjme.JfxContext;
import name.huliqing.editor.fxjme.JfxSystem;

/**
 *
 * @author huliqing
 */
public class LyEditor extends Application {
    
    private EditorApp app;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(e -> {
            System.out.println("Hello World!");
        });

        JfxView jmeView = JfxSystem.startApp(EditorApp.class.getName(), 300, 300);
        
        VBox root = new VBox();
        root.getChildren().add(btn);
        root.getChildren().add(jmeView);
        Scene scene = new Scene(root, 640, 480);
        
        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.show();

    }
    
    
}
