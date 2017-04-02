/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
import name.huliqing.luoying.utils.MaterialUtils;

/**
 * 缩放控制轴
 * @author huliqing
 */
public class ScaleAxis extends Node implements AxisObj {
    
    private final AxisNode axisXNode;
    private final AxisNode axisYNode;
    private final AxisNode axisZNode;
    private final Spatial center;
    
    public ScaleAxis() {
        Spatial axisXInner = createAxis("axisXInner", AxisObj.AXIS_COLOR_X);
        axisXInner.rotate(0, 0, -FastMath.PI / 2);
        Axis axisX = new Axis(Axis.Type.x);
        axisX.attachChild(axisXInner);
        axisXNode = new AxisNode(axisX, AxisObj.AXIS_COLOR_X);
        
        Spatial axisYInner = createAxis("axisYInner", AxisObj.AXIS_COLOR_Y);
        Axis axisY = new Axis(Axis.Type.y);
        axisY.attachChild(axisYInner);
        axisYNode = new AxisNode(axisY, AxisObj.AXIS_COLOR_Y);
        
        Spatial axisZInner = createAxis("axisZInner", AxisObj.AXIS_COLOR_Z);
        axisZInner.rotate(FastMath.PI / 2, 0, 0);
        Axis axisZ = new Axis(Axis.Type.z);
        axisZ.attachChild(axisZInner);
        axisZNode = new AxisNode(axisZ, AxisObj.AXIS_COLOR_Z);
        
        center = createCenterBox("center");
        
        attachChild(center);
        attachChild(axisXNode);
        attachChild(axisYNode);
        attachChild(axisZNode);
        
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
        Material mat = MaterialUtils.createUnshaded(new ColorRGBA(1f, 1f, 0f, 0.3f));
        mat.getAdditionalRenderState().setDepthTest(false);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        centerSpatial.setMaterial(mat);
        centerSpatial.setQueueBucket(Bucket.Translucent);
        return centerSpatial;
    }

    @Override
    public AxisNode getAxisX() {
        return axisXNode;
    }

    @Override
    public AxisNode getAxisY() {
        return axisYNode;
    }

    @Override
    public AxisNode getAxisZ() {
        return axisZNode;
    }
    
    public Spatial getCenter() {
        return center;
    }
    
}
