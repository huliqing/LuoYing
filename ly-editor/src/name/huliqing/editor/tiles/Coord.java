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
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Line;
import name.huliqing.editor.AssetConstants;
import name.huliqing.editor.Editor;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 *
 * @author huliqing
 */
public final class Coord extends Node implements Control {

    private final Vector3f lastCamLoc = new Vector3f();
    private float size = 0.1f;
    
    public Coord() {
        
        Spatial arrowX = createArrow("arrowX", new ColorRGBA(1.0f, 0.1f, 0.1f, 1.0f));
        Spatial arrowY = createArrow("arrowY", new ColorRGBA(0.1f, 1.0f, 0.1f, 1.0f));
        Spatial arrowZ = createArrow("arrowZ", new ColorRGBA(0.1f, 0.1f, 1.0f, 1.0f));
        
        arrowX.rotate(0, 0, -FastMath.PI / 2);
        arrowZ.rotate(FastMath.PI / 2, 0, 0);
        
        attachChild(arrowX);
        attachChild(arrowY);
        attachChild(arrowZ);
        
        addControl(this);
    }
    
    private Spatial createArrow(String name, ColorRGBA color) {
        Node node = new Node(name);
        // 轴线
        Geometry line = new Geometry("line" + name, new Line(new Vector3f(), new Vector3f(0,1,0)));
        line.setMaterial(MaterialUtils.createUnshaded(color));
        node.attachChild(line);
        // 一个圆锥箭头
        Spatial cone = Editor.getApp().getAssetManager().loadModel(AssetConstants.MODEL_CONE);
        Material mat = MaterialUtils.createUnshaded(color);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        cone.setMaterial(mat);
        cone.setLocalTranslation(0, 1, 0);
        cone.setLocalScale(0.05f, 0.1f, 0.05f);
        node.attachChild(cone);
        return node;
    }
    
    public void setSize(float size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size could not less than zero!");
        }
        this.size = size;
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        return null;
    }

    @Override
    public void setSpatial(Spatial spatial) {
    }

    @Override
    public void update(float tpf) {
        Vector3f camloc = Editor.getApp().getCamera().getLocation();
        if (Float.compare(camloc.x, lastCamLoc.x) != 0
                || Float.compare(camloc.y, lastCamLoc.y) != 0
                || Float.compare(camloc.z, lastCamLoc.z) != 0) {
            float scale = size * getWorldTranslation().distance(camloc);
            if (scale > 0) {
                setLocalScale(scale);
            }
            lastCamLoc.set(camloc);
        }
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
    }


}
