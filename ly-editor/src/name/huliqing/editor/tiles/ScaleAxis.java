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
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 *
 * @author huliqing
 */
public class ScaleAxis extends Node implements AxisObj {
    
    private final Axis axisX;
    private final Axis axisY;
    private final Axis axisZ;
    private final Spatial center;
    
    public ScaleAxis() {
        Spatial axisXInner = createAxis("axisXInner", new ColorRGBA(1.0f, 0.1f, 0.1f, 1.0f));
        axisXInner.rotate(0, 0, -FastMath.PI / 2);
        axisX = new Axis(Axis.Type.x);
        axisX.attachChild(axisXInner);
        
        Spatial axisYInner = createAxis("axisYInner", new ColorRGBA(0.1f, 1.0f, 0.1f, 1.0f));
        axisY = new Axis(Axis.Type.y);
        axisY.attachChild(axisYInner);
        
        Spatial axisZInner = createAxis("axisZInner", new ColorRGBA(0.1f, 0.1f, 1.0f, 1.0f));
        axisZInner.rotate(FastMath.PI / 2, 0, 0);
        axisZ = new Axis(Axis.Type.z);
        axisZ.attachChild(axisZInner);
        
        center = createCenterBox("center");
        
        attachChild(center);
        attachChild(axisX);
        attachChild(axisY);
        attachChild(axisZ);
        
        // 默认放在半透明中桶中，这样可以盖住其它所有物体
        setQueueBucket(RenderQueue.Bucket.Translucent);
    }
    
    private Spatial createAxis(String name, ColorRGBA color) {
        Node axis = new Node(name);
        Material mat = MaterialUtils.createUnshaded(color);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.getAdditionalRenderState().setDepthTest(false);
        
        // 轴线
        Geometry line = new Geometry(name + "line", new Line(new Vector3f(), new Vector3f(0,1,0)));
        line.setMaterial(mat);
        axis.attachChild(line);
        
        // 小box
        Geometry boxGeo = new Geometry(name + "box", new Box(0.5f, 0.5f, 0.5f));
        boxGeo.setMaterial(mat);
        boxGeo.setLocalTranslation(0, 1, 0);
        boxGeo.setLocalScale(0.1f, 0.1f, 0.1f);
        boxGeo.setQueueBucket(RenderQueue.Bucket.Translucent);
        axis.attachChild(boxGeo);
        
        // for pick
        Geometry outer = new Geometry(name + "picker", new Box(0.15f, 0.5f, 0.15f));
        outer.setLocalTranslation(0, 0.5f, 0);
        outer.setMaterial(MaterialUtils.createUnshaded());
        outer.setCullHint(CullHint.Always);
        axis.attachChild(outer);
        
        return axis;
    }
    
    private Spatial createCenterBox(String name) {
        Geometry centerSpatial = new Geometry(name + "center", new Box(0.2f, 0.2f, 0.2f));
        centerSpatial.setMaterial(MaterialUtils.createUnshaded(ColorRGBA.White));
        return centerSpatial;
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
    
    public Spatial getCenter() {
        return center;
    }
}
