/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import name.huliqing.editor.fxjme.JFXAppState;
import name.huliqing.editor.fxjme.JmeView;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author huliqing
 */
public class LyEditor extends Application {
    
    private EditorApp app;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(e -> {
            System.out.println("Hello World!");
        });

        JmeView jmeView = new JmeView();
        
        VBox root = new VBox();
        root.getChildren().add(btn);
        root.getChildren().add(jmeView);
        Scene scene = new Scene(root, 640, 480);
        
        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.show();
        
        startApp(new JFXAppState(jmeView));

    }
    
    public void startApp(JFXAppState appState) throws Exception {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(300, 300);
//        settings.setFrameRate(60);
        app = new EditorApp();
        app.setSettings(settings);
        app.setPauseOnLostFocus(false);
        app.setShowSettings(false);
        app.start(JmeContext.Type.OffscreenSurface);
        app.getStateManager().attach(appState);

    }
    
}
