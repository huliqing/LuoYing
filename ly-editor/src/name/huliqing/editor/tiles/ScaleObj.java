/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

import com.jme3.scene.Node;

/**
 *
 * @author huliqing
 */
public class ScaleObj extends Node {
    
    private final TileScale tileScale;
    
    public ScaleObj() {
        tileScale = new TileScale();
        tileScale.addControl(new AutoScaleControl());
        attachChild(tileScale);
    }
    
}
