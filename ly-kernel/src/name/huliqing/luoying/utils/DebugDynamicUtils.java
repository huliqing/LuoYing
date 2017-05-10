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
package name.huliqing.luoying.utils;

import com.jme3.ai.navmesh.Path.Waypoint;
import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Spline;
import static com.jme3.math.Spline.SplineType.CatmullRom;
import static com.jme3.math.Spline.SplineType.Linear;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Curve;
import com.jme3.util.TempVars;
import java.util.Iterator;
import java.util.List;
import name.huliqing.luoying.LuoYing;

/**
 * 用于动态DEBUG的工具类，相关的方法需要在update中运行才有意义
 * @author huliqing
 */
public class DebugDynamicUtils {
    
    /**
     * 显示一个箭头
     * @param debugId
     * @param worldOrigin
     * @param worldDirection dir必须已经归一化
     * @param length 
     */
    public static void debugArrow(String debugId, Vector3f worldOrigin, Vector3f worldDirection, float length) {
//        Logger.get(DebugDynamicUtils.class).log(Level.INFO, "debugArrow, debugId={0}", debugId);
        
        Arrow arrow = new Arrow(worldDirection);
        arrow.setLineWidth(2); // make arrow thicker
        Geometry s = DebugUtils.putShape(arrow, ColorRGBA.Green);
        addDebugObject("debugArrow" + debugId, s, false);
        
        TempVars tv = TempVars.get();
        arrow.setArrowExtent(worldDirection.mult(length, tv.vect1));
        s.setLocalTranslation(worldOrigin);
        tv.release();
    }
    
    /**
     * 显示目标spatial的bounding
     * 暂支持：AABB,Sphere
     * @param debugId
     * @param spatial 
     */
    public static void debugBounding(String debugId, Spatial spatial) {
        BoundingVolume bv = spatial.getWorldBound();
        Geometry geo = null;
        if (bv.getType() == BoundingVolume.Type.AABB) {
            BoundingBox bb = (BoundingBox) bv;
            geo = DebugUtils.createWireBox(bb.getXExtent(), bb.getYExtent(), bb.getZExtent(), ColorRGBA.Blue);
        } else if (bv.getType() == BoundingVolume.Type.Sphere) {
            BoundingSphere bs = (BoundingSphere) bv;
            geo = DebugUtils.createWireSphere(bs.getRadius(), ColorRGBA.Blue);
        }
        if (geo != null) {
            geo.setLocalTranslation(bv.getCenter());
            addDebugObject("debugBounding_" + debugId, geo, false);
        }
    }
    
    public static void debugPath(String debugId, List<Waypoint> waypoints) {
        Node path = new Node();
        Waypoint previous = null;
        ColorRGBA lineColor = ColorRGBA.randomColor();
        for (Waypoint p : waypoints) {
            Spatial box = DebugUtils.createBox(0.2f, 0.2f, 0.2f, ColorRGBA.Red);
            box.setLocalTranslation(p.getPosition());
            path.attachChild(box);

            if (previous != null) {
                Spatial arrow = DebugUtils.createArrow(previous.getPosition(), p.getPosition(), lineColor);
                arrow.scale(3);
                Spatial line = DebugUtils.createLine(previous.getPosition(), p.getPosition(), lineColor);
                path.attachChild(line);
                path.attachChild(arrow);
            }
            previous = p;
        }
        addDebugObject("debugPath_" + debugId, path, false);
    }
    
    /**
     * 将mesh以wireframe显示出来
     * @param debugId
     * @param shape
     * @return 
     */
    public static Spatial debugShape(String debugId, Mesh shape) {
        Geometry geo = DebugUtils.putShape(shape, ColorRGBA.Blue);
        addDebugObject("debugShape_" + debugId, geo, false);
        return geo;
    }
    
    /**
     * 在原点显示一个坐标
     * @param debugId
     * @return 
     */
    public static Spatial debugCoordinate(String debugId) {
        Spatial geo = DebugUtils.attachCoordinateAxes(new Vector3f());
        addDebugObject("debugCoordinate_" + debugId, geo, false);
        return geo;
    }
    
    public static void debugSpline(String debugId, Spline spline, boolean inGui) {
        Node debugRoot = new Node();
        Material m = LuoYing.getAssetManager().loadMaterial("Common/Materials/RedColor.j3m");
        for (Iterator<Vector3f> it = spline.getControlPoints().iterator(); it.hasNext();) {
            Vector3f cp = it.next();
            Geometry geo = new Geometry("box", new Box(cp, 0.1f, 0.1f, 0.1f));
            geo.setMaterial(m);
            debugRoot.attachChild(geo);
        }
        switch (spline.getType()) {
            case CatmullRom:
                debugRoot.attachChild(CreateCatmullRomPath(spline));
                break;
            case Linear:
                debugRoot.attachChild(CreateLinearPath(spline));
                break;
            default:
                debugRoot.attachChild(CreateLinearPath(spline));
                break;
        }
        addDebugObject("debugSpline_" + debugId, debugRoot, inGui);
    }
    
    private static void addDebugObject(String debugId, Spatial spatial, boolean inGui) {
        SimpleApplication sa = (SimpleApplication) LuoYing.getApp();
        
        Node root = inGui ? sa.getGuiNode() : sa.getRootNode();
        Spatial old = root.getChild(debugId);
        if (old != null) {
            old.removeFromParent();
        }
        spatial.setName(debugId);
        root.attachChild(spatial);
    }

    private static Geometry CreateLinearPath(Spline spline) {
        Material mat = new Material(LuoYing.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Blue);
        Geometry lineGeometry = new Geometry("line", new Curve(spline, 0));
        lineGeometry.setMaterial(mat);
        return lineGeometry;
    }

    private static Geometry CreateCatmullRomPath(Spline spline) {
        Material mat = new Material(LuoYing.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Blue);
        Geometry lineGeometry = new Geometry("line", new Curve(spline, 10));
        lineGeometry.setMaterial(mat);
        return lineGeometry;
    }
}
