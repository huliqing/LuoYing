package name.huliqing.editor.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import java.util.logging.Logger;

/**
 *
 * @author huliqing
 */
public class EditorApp extends SimpleApplication {

    private static final Logger LOG = Logger.getLogger(EditorApp.class.getName());

    @Override
    public void simpleInitApp() {
        getInputManager().setCursorVisible(true);
        getFlyByCamera().setDragToRotate(true);
        getFlyByCamera().setMoveSpeed(10);
        
        rootNode.addLight(new DirectionalLight());
        rootNode.addLight(new AmbientLight());
        
        for (int i = 0; i < 50; i++) {
            Spatial sinbad = this.assetManager.loadModel("Models/Sinbad.mesh.j3o");
            sinbad.setLocalScale(0.01f);
            sinbad.setLocalTranslation(5 * FastMath.nextRandomFloat() - 2.5f, 5 * FastMath.nextRandomFloat() - 2.5f, 5 * FastMath.nextRandomFloat() - 2.5f);
            this.rootNode.attachChild(sinbad);
            
        }
        
        
        System.out.println("EditorApp started......");
    }

    
//    private int frameCount;
    
    @Override
    public void update() {
        super.update(); 
        
//        frameCount++;
//        if (frameCount % 60 == 7) {
//            this.settings.setWidth(FastMath.nextRandomInt(300, 400));
//            this.settings.setHeight(FastMath.nextRandomInt(300, 400));
//            
//            this.restart();
//            LOG.info("restart: width=" + settings.getWidth() + ", height=" + settings.getHeight());
//        }
    }

    
    
    public static void main(String[] args) {
        
        AppSettings settings = new AppSettings(true);
        settings.setResolution(300, 300);
        settings.setFrameRate(60);
        
        EditorApp app = new EditorApp();
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }
}
