/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

/**
 *
 * @author huliqing
 */
public class EditorApp extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        getInputManager().setCursorVisible(true);
        getFlyByCamera().setDragToRotate(true);
        getFlyByCamera().setMoveSpeed(10);
        
        for (int i = 0; i < 1000; i++) {
            Box box = new Box(new Vector3f(-0.01f,-0.01f,-0.01f), new Vector3f(0.01f,0.01f,0.01f));
            Geometry geo = new Geometry("Test box", box);
            geo.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"));
            
            geo.setLocalTranslation(5 * FastMath.nextRandomFloat() - 2.5f
                    , 5 * FastMath.nextRandomFloat() - 2.5f
                    , 5 * FastMath.nextRandomFloat() - 2.5f);
            
            this.rootNode.attachChild(geo);
            
        }


        System.out.println("EditorApp started......");
    }

    
    public static void main(String[] args) {
        
        AppSettings settings = new AppSettings(true);
        settings.setResolution(300, 300);
        
        EditorApp app = new EditorApp();
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }
}
