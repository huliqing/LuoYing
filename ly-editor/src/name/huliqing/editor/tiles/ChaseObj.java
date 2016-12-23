/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author huliqing
 */
public class ChaseObj extends Node {
    
    private final LocationAxis tileLocation;
    
    public ChaseObj() {
        tileLocation = new LocationAxis();
        tileLocation.addControl(new AutoScaleControl());
        attachChild(tileLocation);
        setCullHint(Spatial.CullHint.Always);
    }
}
