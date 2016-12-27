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
public class RotationAxis extends Node implements AxisObj {
    
    private final Axis axisX;
    private final Axis axisY;
    private final Axis axisZ;
    
    public RotationAxis() {
        Spatial rotX = createTorus("rotX", ColorRGBA.Red);
        rotX.rotate(0, FastMath.PI / 2, 0);
        axisX = new Axis(Axis.Type.x);
        axisX.attachChild(rotX);
        
        Spatial rotY = createTorus("rotY", ColorRGBA.Green);
        rotY.rotate(FastMath.PI / 2, 0, 0);
        axisY = new Axis(Axis.Type.y);
        axisY.attachChild(rotY);
        
        Spatial rotZ = createTorus("rotZ", ColorRGBA.Blue);
        axisZ = new Axis(Axis.Type.z);
        axisZ.attachChild(rotZ);
        
        attachChild(axisX);
        attachChild(axisY);
        attachChild(axisZ);
        
        setQueueBucket(RenderQueue.Bucket.Translucent);
    }
    
    private Spatial createTorus(String name, ColorRGBA color) {
        Node torus = new Node(name);
        
        // 可见的旋转圈
        Material mat = MaterialUtils.createUnshaded(color);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.getAdditionalRenderState().setDepthTest(false);
        Geometry torusInner = new Geometry(name + "tours", new Torus(60, 4, 0.015f, 1.0f));
        torusInner.setMaterial(mat);
        
        // 用于优化点选
        Geometry torusOuter = new Geometry(name + "picker", new Torus(20, 4, 0.15f, 1.0f));
        torusOuter.setMaterial(MaterialUtils.createUnshaded());
        torusOuter.setCullHint(CullHint.Always);
        
        torus.attachChild(torusInner);
        torus.attachChild(torusOuter);
        
        return torus;
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
