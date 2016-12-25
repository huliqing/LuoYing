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
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import name.huliqing.editor.AssetConstants;
import name.huliqing.editor.Editor;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 *
 * @author huliqing
 */
public class LocationAxis extends Node implements AxisObj {
    
    private final Axis axisX;
    private final Axis axisY;
    private final Axis axisZ;

    public LocationAxis() {
        
        Spatial axisXInner = createAxis("axisXInner", new ColorRGBA(1.0f, 0.1f, 0.1f, 1.0f));
        axisXInner.rotate(0, 0, -FastMath.PI / 2);
        axisX = new Axis(Axis.Type.x);
        axisX.attachChild(axisXInner);
        
        axisY = new Axis(Axis.Type.y);
        axisY.attachChild(createAxis("axisYInner", new ColorRGBA(0.1f, 1.0f, 0.1f, 1.0f)));
        
        Spatial axisZInner = createAxis("axisZInner", new ColorRGBA(0.1f, 0.1f, 1.0f, 1.0f));
        axisZInner.rotate(FastMath.PI / 2, 0, 0);
        axisZ = new Axis(Axis.Type.z);
        axisZ.attachChild(axisZInner);
        
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
        Geometry line = new Geometry(name, new Line(new Vector3f(), new Vector3f(0,1,0)));
        line.setMaterial(mat);
        axis.attachChild(line);
        
        // 一个圆锥箭头
        Spatial cone = Editor.getEditor().getAssetManager().loadModel(AssetConstants.MODEL_CONE);
        cone.setMaterial(mat);
        cone.setLocalTranslation(0, 1, 0);
        cone.setLocalScale(0.05f, 0.1f, 0.05f);
        cone.setQueueBucket(Bucket.Translucent); // 这里需要特别设置一下，否则仍然会被其它物体挡住
        axis.attachChild(cone);
        
        // for pick
        Geometry outer = new Geometry(name + "outer", new Box(0.15f, 0.5f, 0.15f));
        outer.setLocalTranslation(0, 0.5f, 0);
        outer.setMaterial(MaterialUtils.createUnshaded());
        outer.setCullHint(CullHint.Never);
        axis.attachChild(outer);
        
        return axis;
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
