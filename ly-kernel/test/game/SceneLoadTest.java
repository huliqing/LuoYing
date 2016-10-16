/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.scene.Scene;

/**
 * @author huliqing
 */
public class SceneLoadTest extends SimpleApplication {

    public static void main(String[] args) {
        
        AppSettings settings = new AppSettings(true);
        settings.setFrameRate(60);
        SceneLoadTest test = new SceneLoadTest();
        test.setSettings(settings);
        test.start();
    }
    
    @Override
    public void simpleInitApp() {
        this.flyCam.setMoveSpeed(100);
        LuoYing.initialize(this, settings);
        
        Scene scene = Loader.load(IdConstants.SYS_SCENE);
        scene.setProcessorViewPorts(getViewPort());
        scene.initialize();
        this.getRootNode().attachChild(scene.getRoot());
        
//        Spatial boundaryBox = AssetLoader.loadModel(AssetConstants.MODEL_BOUNDARY);
//        Material mat = new Material(LuoYing.getAssetManager(), AssetConstants.MATERIAL_UNSHADED);
//        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
//        mat.setColor("Color", ColorRGBA.Yellow);
//        boundaryBox.setMaterial(mat);
//        rootNode.attachChild(boundaryBox);
//        ModelFileUtils.tempSave(boundaryBox, "c:\\Temp\\unitBoundaryBox.j3o");
    }
    
}
