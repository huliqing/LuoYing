/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.logging.Logger;
import name.huliqing.editor.tiles.ChaseObj;
import name.huliqing.editor.tiles.Grid;
import name.huliqing.editor.tiles.LocationObj;
import name.huliqing.editor.tiles.RotationObj;
import name.huliqing.editor.tiles.ScaleObj;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 *
 * @author huliqing
 */
public class Editor extends SimpleApplication{
    private static final Logger LOG = Logger.getLogger(Editor.class.getName());
    
    private static Application app;
    private final EditorInputManager editorInputManager = new EditorInputManager();
    
    // 网格
    public Grid grid;
    // 被镜头跟随的物体
    public Spatial chaseObj;
    // 物体选择、操作标记（位置）
    public LocationObj locationObj;
    // 物体选择、操作标记（旋转） 
    public RotationObj rotationObj;
    // 缩放
    public ScaleObj scaleObj;
    // 镜头
    public EditorCamera editorCam;
    
    private Spatial selected;
    
    public final static Application getApp() {
        return app;
    }

    @Override
    public void simpleInitApp() {
        app = this;
        flyCam.setEnabled(false);
        inputManager.setCursorVisible(true);
        app.getViewPort().setBackgroundColor(ColorRGBA.DarkGray);
        
        grid = new Grid();
        locationObj = new LocationObj();
        rotationObj = new RotationObj();
        scaleObj = new ScaleObj();
        chaseObj = new ChaseObj();
        
        editorCam = new EditorCamera(getCamera(), inputManager);
        editorCam.setChase(chaseObj);
        
        rootNode.attachChild(grid); 
        rootNode.attachChild(locationObj);
        rootNode.attachChild(rotationObj);
        rootNode.attachChild(scaleObj);
        rootNode.attachChild(chaseObj);
        
        locationObj.setCullHint(Spatial.CullHint.Never);
        rotationObj.setCullHint(Spatial.CullHint.Always);
        scaleObj.setCullHint(Spatial.CullHint.Always);
        chaseObj.setCullHint(Spatial.CullHint.Always);
        
        editorInputManager.initialize(this);

        // test
        Box box = new Box(2,2,2);
        Geometry ge = new Geometry("", box);
        ge.setMaterial(MaterialUtils.createUnshaded(ColorRGBA.Gray));
        rootNode.attachChild(ge);
        
        app.getCamera().setLocation(new Vector3f(10,10,10));
        app.getCamera().lookAt(new Vector3f(), Vector3f.UNIT_Y);
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        editorInputManager.update(tpf);
    }
}
