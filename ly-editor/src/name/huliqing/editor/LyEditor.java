/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

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

        JfxView jmeView = new JfxView();
        
        VBox root = new VBox();
        root.getChildren().add(btn);
        root.getChildren().add(jmeView);
        Scene scene = new Scene(root, 640, 480);
        
        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.show();
        
        startApp(new JfxAppState(jmeView));

    }
    
    public void startApp(JfxAppState appState) throws Exception {
        AppSettings settings = new AppSettings(true);

        // ==== Notice1
        // 使用定制的JfxContext的目的只是为了勾住事件响应,为了将事件从Jfx转化到Jme内部必须这样做，因为无法直接从外部
        // 去替换inputManager中事件的处理，即无法直接替换keys,mouse,joystick,touch等事件处理.
        
        // ==== Notice2
        // 如果从外部直接调用inputManager.onMouseButtonEvent(xxx);会导致事件错误,参考inputManager的以下代码：eventsPermitted不允许
//    public void onMouseButtonEvent(MouseButtonEvent evt) {
//        if (!eventsPermitted) {
//            throw new UnsupportedOperationException("MouseInput has raised an event at an illegal time.");
//        }
//        //updating cursor pos on click, so that non android touch events can properly update cursor position.
//        cursorPos.set(evt.getX(), evt.getY());
//        inputQueue.add(evt);
//    }

        // ==== Notice3
        // 使用定制的renderer需要在app启动之前设置: settings.setRenderer("CUSTOMname.huliqing.editor.fxjme.JfxContext");
        // 参考代码：com.jme3.system.JmeDesktopSystem.newContext(xx,xx).
        
        // ==== Notice4
        // 在JfxContext.run()运行之前必须把renderer值设置回去. 因为JfxContext继承自LwjglOffscreenBuffer,
        // 而LwjglOffscreenBuffer在run的时候会检查renderer, 如果不设置回去则会报错，参考以下:
//        java.lang.UnsupportedOperationException: Unsupported renderer: CUSTOMname.huliqing.editor.fxjme.JfxContext
//	at com.jme3.system.lwjgl.LwjglContext.initContextFirstTime(LwjglContext.java:228)
//	at com.jme3.system.lwjgl.LwjglContext.internalCreate(LwjglContext.java:266)
//	at com.jme3.system.lwjgl.LwjglOffscreenBuffer.initInThread(LwjglOffscreenBuffer.java:92)
//	at com.jme3.system.lwjgl.LwjglOffscreenBuffer.run(LwjglOffscreenBuffer.java:154)
//	at java.lang.Thread.run(Thread.java:745)
        
        // 因为JfxContext已经启动，所以设置回去没有关系 
        String originRenderer = settings.getRenderer();
        settings.put("originRenderer", originRenderer);
        settings.setRenderer("CUSTOM" + JfxContext.class.getName());
        
        // other settings.
        settings.setResolution(300, 300);
//        settings.setFrameRate(60);
        app = new EditorApp();
        app.setSettings(settings);
        app.setPauseOnLostFocus(false);
        app.setShowSettings(false);
        // 因为我们的目的是把frameBuffer渲染到JfxView组件上，所以这里不要使用Display\Canvas\Headless, 只要获得frameBuffer
        // 就可以。
        app.start(JmeContext.Type.OffscreenSurface);
        
        app.getStateManager().attach(appState);

    }
    
}
