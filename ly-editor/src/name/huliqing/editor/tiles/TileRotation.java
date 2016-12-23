/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Torus;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 *
 * @author huliqing
 */
public class TileRotation extends Node {
    
    public TileRotation() {
        Spatial rotX = createTorus("rotX", ColorRGBA.Red);
        Spatial rotY = createTorus("rotY", ColorRGBA.Green);
        Spatial rotZ = createTorus("rotZ", ColorRGBA.Blue);
        
        rotX.rotate(0, FastMath.PI / 2, 0);
        rotY.rotate(FastMath.PI / 2, 0, 0);
        
        attachChild(rotX);
        attachChild(rotY);
        attachChild(rotZ);
        
        setQueueBucket(RenderQueue.Bucket.Translucent);
    }
    
    private Spatial createTorus(String name, ColorRGBA color) {
        Material mat = MaterialUtils.createUnshaded(color);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.getAdditionalRenderState().setDepthTest(false);
        
        Torus torus = new Torus(60, 4, 0.01f, 1.0f);
        Geometry geo = new Geometry(name, torus);
        geo.setMaterial(mat);
        return geo;
    }
}
