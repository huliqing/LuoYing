/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.test;

import com.jme3.math.FastMath;
import com.jme3.system.AppSettings;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import name.huliqing.fxjme.JfxSystem;
import name.huliqing.fxjme.JfxView;

/**
 *
 * @author huliqing
 */
public class TestJfx extends Application {
    
    private JfxView jfxView;
    
    private final int width = 640;
    private final int height = 480;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        AppSettings settings = new AppSettings(true);
        settings.setResolution(500, 400);
        settings.setFrameRate(60);
        
        jfxView = JfxSystem.startApp(TestEditorApp.class.getName(), settings);
        jfxView.setUseDepthBuffer(true);
        
        Button btn = new Button();
        btn.setText("Stop JFX Application");
        btn.setOnAction(e -> {
            Platform.exit();
        });
        
//        new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                if (now % 120 == 0) {
//                    int w = FastMath.nextRandomInt(200, 500);
//                    int h = FastMath.nextRandomInt(300, 400);
//                    jfxView.setResolution(w, h);
//                    
//                }
//                
//            }
//        }.start();
        
        
        
        StackPane root = new StackPane();
//        VBox root = new VBox();
        
        root.getChildren().add(btn);
        root.getChildren().add(jfxView);
        
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
