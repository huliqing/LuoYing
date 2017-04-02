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
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import name.huliqing.luoying.utils.GeometryUtils;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 * 位移控制轴
 * @author huliqing
 */
public class LocationAxis extends Node implements AxisObj {
    
    private final AxisNode axisXNode;
    private final AxisNode axisYNode;
    private final AxisNode axisZNode;

    public LocationAxis() {
        
        Spatial axisXInner = createAxis("axisXInner", AxisObj.AXIS_COLOR_X);
        axisXInner.rotate(0, 0, -FastMath.PI / 2);
        Axis axisX = new Axis(Axis.Type.x);
        axisX.attachChild(axisXInner);
        axisXNode = new AxisNode(axisX, AxisObj.AXIS_COLOR_X);
        
        Axis axisY = new Axis(Axis.Type.y);
        axisY.attachChild(createAxis("axisYInner", AxisObj.AXIS_COLOR_Y));
        axisYNode = new AxisNode(axisY, AxisObj.AXIS_COLOR_Y);
        
        Spatial axisZInner = createAxis("axisZInner", AxisObj.AXIS_COLOR_Z);
        axisZInner.rotate(FastMath.PI / 2, 0, 0);
        Axis axisZ = new Axis(Axis.Type.z);
        axisZ.attachChild(axisZInner);
        axisZNode = new AxisNode(axisZ, AxisObj.AXIS_COLOR_Z);
        
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
        Geometry line = new Geometry(name, new Line(new Vector3f(), new Vector3f(0,1,0)));
        line.setMaterial(mat);
        axis.attachChild(line);
        
        // 一个圆锥箭头
//        Spatial cone = Editor.getEditor().getAssetManager().loadModel(AssetConstants.MODEL_CONE);
        Mesh coneMesh = GeometryUtils.createCone(20, 1, 1);
        Geometry cone = new Geometry("cone", coneMesh);
        cone.setMaterial(mat);
        cone.setLocalTranslation(0, 1, 0);
        cone.setLocalScale(0.05f, 0.1f, 0.05f);
        cone.setQueueBucket(Bucket.Translucent); // 这里需要特别设置一下，否则仍然会被其它物体挡住
        axis.attachChild(cone);
        
        // for pick
        Geometry outer = new Geometry(name + "outer", new Box(0.15f, 0.5f, 0.15f));
        outer.setLocalTranslation(0, 0.5f, 0);
        outer.setMaterial(MaterialUtils.createUnshaded());
        outer.setCullHint(CullHint.Always);
        axis.attachChild(outer);
        
        return axis;
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
   

}
