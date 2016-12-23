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
public class LocationObj extends Node {
    
    private final TileLocation tileCoord;
    
    public LocationObj() {
        tileCoord = new TileLocation();
        tileCoord.addControl(new AutoScaleControl());
        attachChild(tileCoord);
    }
    
}
