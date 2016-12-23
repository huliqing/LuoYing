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
public class ChaseObj extends Node {
    
    private final TileLocation tileLocation;
    
    public ChaseObj() {
        tileLocation = new TileLocation();
        tileLocation.addControl(new AutoScaleControl());
        attachChild(tileLocation);
    }
}
