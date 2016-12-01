/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.sceneloader;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 *
 * @author huliqing
 */
public class SimpleSceneLoader extends ProgressSceneLoader {

    private final PlayService playService = Factory.get(PlayService.class);
    
    private Node progress;
    private Spatial background;
    private Spatial foreground;

    @Override
    protected void initProgress(Scene scene) {
        float sw = LuoYing.getSettings().getWidth();
        float sh = LuoYing.getSettings().getHeight();
        
        float pWidth = sw * 0.9f;
        float pHeight = sh * 0.01f;
        float leftMargin = (sw - pWidth) * 0.5f;
        
        progress = new Node();
        background = createBox("background", ColorRGBA.Gray);
        
        foreground = createBox("foreground", ColorRGBA.Green);
        foreground.setLocalScale(0, 1, 1);
        foreground.setLocalTranslation(0, 0, 1);
        
        progress.attachChild(background);
        progress.attachChild(foreground);
        progress.setLocalScale(pWidth, pHeight, 1);
        progress.setLocalTranslation(leftMargin, sh * 0.5f, 0);
        
        Node guiRoot = playService.getGame().getGuiScene().getRoot();
        guiRoot.attachChild(progress);
    }

    @Override
    protected void cleanup() {
        progress.removeFromParent();
        super.cleanup(); 
    }
    
    @Override
    protected void displayProgress(float progress) {
        foreground.setLocalScale(progress, 1, 1);
    }
    
    private Geometry createBox(String name, ColorRGBA color) {
        Material mat = MaterialUtils.createUnshaded();
        mat.setColor("Color", color);
        Geometry box = new Geometry(name, new Quad(1,1));
        box.setMaterial(mat);
        return box;
    }
    
}
