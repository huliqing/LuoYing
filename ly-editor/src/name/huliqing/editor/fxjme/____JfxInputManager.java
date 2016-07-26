///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.editor.fxjme;
//
//import com.jme3.app.Application;
//import com.jme3.input.MouseInput;
//import com.jme3.input.event.MouseButtonEvent;
//import javafx.event.Event;
//import javafx.event.EventHandler;
//import javafx.event.EventType;
//import javafx.scene.input.MouseEvent;
//import org.lwjgl.input.Mouse;
//
///**
// *
// * @author huliqing
// */
//public class JfxInputManager {
//    
//    private final Application app;
//    private final JfxView jfxView;
//    private final JfxMouseInput mouse;
//    
//    public JfxInputManager(Application app, JfxView jfxView) {
//        this.app = app;
//        this.jfxView = jfxView;
//        
//        // Mouse EventConvert
//        this.mouse = new JfxMouseInput(app);
//        this.mouse.setInputListener(app.getInputManager());
//        this.jfxView.addEventHandler(MouseEvent.ANY, mouse);
//    }
//    
//    public void update() {
//        mouse.update();
//    }
//    
//    public void cleanup() {
//        jfxView.removeEventHandler(MouseEvent.ANY, mouse);
//    }
//
//}
