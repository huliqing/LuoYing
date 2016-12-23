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
import com.jme3.math.Vector3f;
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
public class RotationAxis extends Node implements AxisObj {
    
    private final Axis axisX;
    private final Axis axisY;
    private final Axis axisZ;
    
    public RotationAxis() {
        Spatial rotX = createTorus("rotX", ColorRGBA.Red);
        rotX.rotate(0, FastMath.PI / 2, 0);
        axisX = new Axis(new Vector3f(1,0,0));
        axisX.attachChild(rotX);
        
        Spatial rotY = createTorus("rotY", ColorRGBA.Green);
        rotY.rotate(FastMath.PI / 2, 0, 0);
        axisY = new Axis(new Vector3f(0,1,0));
        axisY.attachChild(rotY);
        
        Spatial rotZ = createTorus("rotZ", ColorRGBA.Blue);
        axisZ = new Axis(new Vector3f(0,0,1));
        axisZ.attachChild(rotZ);
        
        attachChild(axisX);
        attachChild(axisY);
        attachChild(axisZ);
        
        setQueueBucket(RenderQueue.Bucket.Translucent);
    }
    
    private Spatial createTorus(String name, ColorRGBA color) {
        Material mat = MaterialUtils.createUnshaded(color);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.getAdditionalRenderState().setDepthTest(false);
        
        Torus torus = new Torus(60, 4, 0.015f, 1.0f);
        Geometry geo = new Geometry(name, torus);
        geo.setMaterial(mat);
        return geo;
    }

    @Override
    public Axis getAxisX() {
        return axisX;
    }

    @Override
    public Axis getAxisY() {
        return axisY;
    }

    @Override
    public Axis getAxisZ() {
        return axisZ;
    }
}
