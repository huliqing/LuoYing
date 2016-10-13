/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.jme3.app.SimpleApplication;
import name.huliqing.ly.LuoYing;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.scene.Scene;

/**
 * @author huliqing
 */
public class SceneLoadTest extends SimpleApplication {

    public static void main(String[] args) {
        new SceneLoadTest().start();
    }
    
    @Override
    public void simpleInitApp() {
        
        LuoYing.initialize(this, settings);
        
        Scene scene = Loader.load(IdConstants.SYS_SCENE);
        scene.setProcessorViewPorts(getViewPort());
        scene.initialize();
        rootNode.attachChild(scene.getRoot());
        
    }
    
}
