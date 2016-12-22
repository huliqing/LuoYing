/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 *
 * @author huliqing
 */
public class Grid extends Node {
    
    public Grid() {
        Geometry grid = new Geometry("grid", new com.jme3.scene.debug.Grid(17, 17, 1.0f));
        grid.setMaterial(MaterialUtils.createUnshaded(new ColorRGBA(0.3f, 0.3f, 0.3f, 1.0f)));
        grid.setLocalTranslation(-8f, 0, -8f);
        attachChild(grid);
        
        Geometry xBox = new Geometry("xBox", new Box(0.5f, 0.5f, 0.5f));
        xBox.setMaterial(MaterialUtils.createUnshaded(new ColorRGBA(2f, 0f, 0.1f, 1f)));
        xBox.setLocalScale(16, 0.005f, 0.005f);
        attachChild(xBox);

        Geometry zBox = new Geometry("zBox", new Box(0.5f, 0.5f, 0.5f));
        zBox.setMaterial(MaterialUtils.createUnshaded(new ColorRGBA(0.1f, 0f, 1f, 1f)));
        zBox.setLocalScale(0.005f, 0.005f, 16);
        attachChild(zBox);
    }
}
