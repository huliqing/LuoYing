/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.utils;

import com.jme3.ai.navmesh.Path;
import com.jme3.animation.Skeleton;
import com.jme3.cinematic.MotionPath;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.debug.SkeletonDebugger;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.debug.WireSphere;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import java.util.List;
import name.huliqing.ly.Ly;

/**
 *
 * @author huliqing
 */
public class DebugUtils {

    /**
     * Coordinate Axes
     *
     * @param pos
     * @return
     */
    public static Spatial attachCoordinateAxes(Vector3f pos) {
        Arrow arrow = new Arrow(Vector3f.UNIT_X);
        arrow.setLineWidth(4); // make arrow thicker
        Geometry x = putShape(arrow, ColorRGBA.Red);

        arrow = new Arrow(Vector3f.UNIT_Y);
        arrow.setLineWidth(4); // make arrow thicker
        Geometry y = putShape(arrow, ColorRGBA.Green);
        
        arrow = new Arrow(Vector3f.UNIT_Z);
        arrow.setLineWidth(4); // make arrow thicker
        Geometry z = putShape(arrow, ColorRGBA.Blue);
        
        Node root = new Node();
        root.attachChild(x);
        root.attachChild(y);
        root.attachChild(z);
        root.setLocalTranslation(pos);
        return root;
    }

    public static Geometry putShape(Mesh shape, ColorRGBA color) {
        Geometry g = new Geometry("coordinate axis", shape);
        Material mat = new Material(Ly.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.setColor("Color", color);
        g.setMaterial(mat);
        return g;
    }

    /**
     * Wireframe Grid
     *
     * @param pos
     * @param size
     * @param color
     * @return
     */
    public static Geometry attachGrid(Vector3f pos, int size, ColorRGBA color) {
        Geometry g = new Geometry("wireframe grid", new Grid(size, size, 0.2f));
        Material mat = new Material(Ly.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        g.setMaterial(mat);
        g.center().move(pos);
        return g;
    }

    /**
     * Wireframe Cube
     * @param pos
     * @param size
     * @param color
     * @return
     */
    public static Geometry createWireBox(float xExt, float yExt, float zExt, ColorRGBA color) {
        Geometry g = new Geometry("wireframe cube", new WireBox(xExt, yExt, zExt));
        Material mat = new Material(Ly.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        g.setMaterial(mat);
        return g;
    }

    /**
     * Wireframe Sphere
     * @param pos
     * @param size
     * @param color
     * @return
     */
    public static Geometry createWireSphere(float size, ColorRGBA color) {
        Geometry g = new Geometry("wireframe sphere", new WireSphere(size));
        Material mat = new Material(Ly.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        g.setMaterial(mat);
        return g;
    }
    
    /**
     * Debug Skeleton
     * @param skeleton
     * @param player 
     */
    public static void debugSkeleton(Skeleton skeleton, Node player) {
        SkeletonDebugger skeletonDebug = new SkeletonDebugger("skeleton", skeleton);
        Material mat = new Material(Ly.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        mat.getAdditionalRenderState().setDepthTest(false);
        skeletonDebug.setMaterial(mat);
        player.attachChild(skeletonDebug);
    }
    
    public static void makeWireFrame(Spatial spatial) {
        SceneGraphVisitor sgv = new SceneGraphVisitor() {
            @Override
            public void visit(Spatial spa) {
                if (spa instanceof Geometry) {
                    Geometry geo = (Geometry) spa;
                    Material mat = geo.getMaterial();
                    if (mat == null) {
                        mat = new Material(Ly.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                    }
                    mat.getAdditionalRenderState().setWireframe(true);
                    geo.setMaterial(mat);
                    geo.setCullHint(Spatial.CullHint.Never);
                }
            }
        };
        spatial.depthFirstTraversal(sgv);
    }
    
    public static MotionPath debugMotionPath(MotionPath motionPath, Path path, Node parentNode) {
        if (motionPath == null) {
            motionPath = new MotionPath();
        }
        try {
            motionPath.disableDebugShape();
        } catch (Exception e){
            // 如果还没有enable过，则会抛异常
        }
        motionPath.clearWayPoints();
        List<Path.Waypoint> points = path.getWaypoints();
        Vector3f temp;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < points.size(); i++) {
            temp = points.get(i).getPosition().clone();
            motionPath.addWayPoint(temp);
            sb.append("(").append(temp.x).append(",").append(temp.y).append(",").append(temp.z).append(")");
        }
//        Logger.get(GeometryUtils.class).log(Level.INFO, "path={0}", sb.toString());
        if (points.size() > 0) { // size必须大于0，否则抛异常
            motionPath.enableDebugShape(Ly.getAssetManager(), parentNode);
        }
        return motionPath;
    }
    
    /**
     * 可视化显示坐标点
     * @param positions
     * @param parentNode
     * @deprecated 使用DebugDynamicUtils
     */
    public static void debugPoints(List<Vector3f> positions, Node parentNode) {
        MotionPath motionPath = new MotionPath();
        for (Vector3f pos : positions) {
            motionPath.addWayPoint(pos);
        }
        motionPath.enableDebugShape(Ly.getAssetManager(), parentNode);
    }
    
    public static Geometry createBox(float xExt, float yExt, float zExt, ColorRGBA color) {
        Geometry g = new Geometry("wireframe cube", new Box(xExt, yExt, zExt));
        Material mat = new Material(Ly.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        g.setMaterial(mat);
        return g;
    }
    
    public static Geometry createLine(Vector3f start, Vector3f end, ColorRGBA color) {
        Geometry g = new Geometry("wireframe cube", new Line(start, end));
        Material mat = new Material(Ly.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        g.setMaterial(mat);
        return g;
    }
        
    public static Spatial createArrow(Vector3f start, Vector3f end, ColorRGBA color) {
        Arrow arrow = new Arrow(end.subtract(start).normalizeLocal());
        arrow.setLineWidth(2);
        Geometry geo = putShape(arrow, color);
        geo.setLocalTranslation(start);
        return geo;
    }
}
