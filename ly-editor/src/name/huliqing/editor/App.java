/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;

/**
 *
 * @author huliqing
 */
public class App extends SimpleApplication{
    
    public App() {}

    @Override
    public void simpleInitApp() {
        
        doTest();

    }
    
    private void doTest() {

        inputManager.setCursorVisible(true);
        flyCam.setDragToRotate(true);
        flyCam.setMoveSpeed(10);
        
        rootNode.addLight(new DirectionalLight());
        rootNode.addLight(new AmbientLight());
        
        for (int i = 0; i < 10; i++) {
            Spatial sinbad = this.assetManager.loadModel("Models/Sinbad.mesh.j3o");
            
            sinbad.setLocalScale(0.05f);
            sinbad.setLocalTranslation(5 * FastMath.nextRandomFloat() - 2.5f, 5 * FastMath.nextRandomFloat() - 2.5f, 5 * FastMath.nextRandomFloat() - 2.5f);
            sinbad.getControl(AnimControl.class).createChannel().setAnim("run");
            this.rootNode.attachChild(sinbad);
        }
    }
    
    public static void main(String[] args) {
        new App().start();
    }
}
