/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.test.fxjme;

import com.jme3.system.AppSettings;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import name.huliqing.fxjme.JfxSystem;
import name.huliqing.fxjme.JfxView;

/**
 *
 * @author huliqing
 */
public class TestJfx extends Application {

    private static final Logger LOG = Logger.getLogger(TestJfx.class.getName());
    
    private int width = 480;
    private int height = 640;
    
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
        settings.setResolution(width, height);
        settings.setFrameRate(30);
        
        // setKeepResolution保持分辨率不要太大，以节省性能。
        jfxView = JfxSystem.startApp(TestEditor.class.getName(), settings);
        jfxView.setResolutionLimit(width, height);
        jfxView.setUseDepthBuffer(true);
        
//        Button btn = new Button();
//        btn.setText("Stop JFX Application");
//        btn.setOnAction(e -> {
//            Platform.exit();
//        });        
        
        StackPane root = new StackPane();
        root.setBackground(Background.EMPTY);
        root.getChildren().add(jfxView);
//        root.getChildren().add(btn);
        
        Scene scene = new Scene(root);
        scene.setFill(new Color(0f, 0f, 0f, 0f));
        
        jfxView.fitWidthProperty().bind(scene.widthProperty());
        jfxView.fitHeightProperty().bind(scene.heightProperty());
        jfxView.setEffect(new DropShadow());
        
        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        // Remember to stop the jme application.
        jfxView.getApplication().stop();
        super.stop();
    }
    
    
}
