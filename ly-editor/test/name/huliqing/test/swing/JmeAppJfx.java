///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.test.swing;
//
//import com.jme3.animation.AnimControl;
//import com.jme3.app.SimpleApplication;
//import com.jme3.light.AmbientLight;
//import com.jme3.light.DirectionalLight;
//import com.jme3.math.FastMath;
//import com.jme3.scene.Spatial;
//import com.jme3.system.AppSettings;
//import com.jme3x.jfx.JmeFxContainer;
//import com.jme3x.jfx.JmeFxScreenContainer;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.Background;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
//
///**
// *
// * @author huliqing
// */
//public class JmeAppJfx extends SimpleApplication {
//
//    @Override
//    public void simpleInitApp() {
//        
//        getInputManager().setCursorVisible(true);
//        getFlyByCamera().setDragToRotate(true);
//        getFlyByCamera().setMoveSpeed(10);
//        
//        rootNode.addLight(new DirectionalLight());
//        rootNode.addLight(new AmbientLight());
//        
//        for (int i = 0; i < 10; i++) {
//            Spatial sinbad = this.assetManager.loadModel("Models/Sinbad.mesh.j3o");
//            
//            sinbad.setLocalScale(0.05f);
//            sinbad.setLocalTranslation(5 * FastMath.nextRandomFloat() - 2.5f, 5 * FastMath.nextRandomFloat() - 2.5f, 5 * FastMath.nextRandomFloat() - 2.5f);
//            sinbad.getControl(AnimControl.class).createChannel().setAnim("run");
//            this.rootNode.attachChild(sinbad);
//        }
//        
//        buildScene();
//        
//        System.out.println("EditorApp started......");
//    }
// 
//    
//    private void buildScene() {
//        VBox root = new VBox();
//        root.getChildren().add(new Button("Hello JFX"));
////        root.setStyle("-fx-background-color: transparent;");  
//        root.setBackground(Background.EMPTY);
//        
//        Scene scene = new Scene(root, 200, 200);
//        scene.setFill(Color.TRANSPARENT);
//        
//        JmeFxScreenContainer jsc = JmeFxContainer.install(this, guiNode, false, null);
//        jsc.setScene(scene);
//    }
//    
//    public static void main(String[] args) {
//        AppSettings settings = new AppSettings(true);
//        settings.setResolution(640, 480);
//        
//        JmeAppJfx app = new JmeAppJfx();
//        app.setSettings(settings);
//        app.setShowSettings(false);
//        app.start();
//    }
//}
