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
public class RotationObj extends Node {
    
    private final TileRotation tileRotation;
    
    public RotationObj() {
        tileRotation = new TileRotation();
        tileRotation.addControl(new AutoScaleControl());
        attachChild(tileRotation);
    }
}
