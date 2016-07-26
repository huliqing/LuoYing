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

    private Geometry geo;
    
    @Override
    public void simpleInitApp() {
        getInputManager().setCursorVisible(true);
        getFlyByCamera().setDragToRotate(true);
        getFlyByCamera().setMoveSpeed(10);
        
        Box box = new Box(new Vector3f(-0.1f,-0.1f,-0.1f), new Vector3f(0.1f,0.1f,0.1f));
        geo = new Geometry("Test box", box);
        geo.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"));

        geo.setLocalTranslation(0,0,0);

        geo.setLocalScale(10);
        this.rootNode.attachChild(geo);

        System.out.println("EditorApp started......");
    }

    @Override
    public void update() {
        super.update(); 
        geo.rotate(0.01f, 0.01f, 0.01f);
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
