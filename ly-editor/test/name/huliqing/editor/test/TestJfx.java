/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.test;

import com.jme3.math.FastMath;
import com.jme3.system.AppSettings;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import name.huliqing.fxjme.JfxSystem;
import name.huliqing.fxjme.JfxView;

/**
 *
 * @author huliqing
 */
public class TestJfx extends Application {

    private static final Logger LOG = Logger.getLogger(TestJfx.class.getName());
    
    
    private JfxView jfxView;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        AppSettings settings = new AppSettings(true);
        // 这里必须把初始化时的分辨率调高一些，最好刚好或大于整个屏幕，因为一些jmeContext（如LwjglOffscreenBuffer)会
        // 使用分辨率来初始化Pbuffer,但是Pbuffer在运行过程无法重建，即大小无法调整，这会导致如果一开始太小则当窗口调整
        // 时，渲染窗口生成的图片无法覆盖整个窗口。
        settings.setResolution(1920, 1280);
        settings.setFrameRate(60);
        
        // setKeepResolution保持分辨率不要太大，以节省性能。
        jfxView = JfxSystem.startApp(TestEditorApp.class.getName(), settings);
        jfxView.setResolutionLimit(1024, 768);
        jfxView.setUseDepthBuffer(true);
        
        Button btn = new Button();
        btn.setText("Stop JFX Application");
        btn.setOnAction(e -> {
            Platform.exit();
        });        
        
        StackPane root = new StackPane();
        
        root.getChildren().add(jfxView);
        root.getChildren().add(btn);
        
        Scene scene = new Scene(root, 640, 480);
        
        jfxView.fitWidthProperty().bind(scene.widthProperty());
        jfxView.fitHeightProperty().bind(scene.heightProperty());
        jfxView.setEffect(new DropShadow());
        
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