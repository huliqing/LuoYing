
package name.huliqing.luoying.utils.modifier;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import jme3tools.optimize.GeometryBatchFactory;

/**
 * 为场景生成寻路信息。
 * @author huliqing
 */
public class NavMeshUtils {
    
    /**
     * 创建一个NavMesh网格物体
     * @param rootNode
     * @param nmg
     * @param am
     * @return 
     */
    public final static Spatial doCreateSpatial(Node rootNode, NavMeshGenerator nmg, AssetManager am) {
        final Geometry navMesh = new Geometry("NavMesh");
        
//            IntermediateData id = new IntermediateData();
        
        nmg.setIntermediateData(null);
        Mesh mesh = new Mesh();
        GeometryBatchFactory.mergeGeometries(findGeometries(rootNode, new LinkedList<Geometry>(), nmg, rootNode), mesh);
        Mesh optiMesh = nmg.optimize(mesh);
        if(optiMesh == null) return null;

        Material material = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        material.getAdditionalRenderState().setWireframe(true);
        material.setColor("Color", ColorRGBA.Green);
        navMesh.setMaterial(material);
        navMesh.setMesh(optiMesh);
        navMesh.setModelBound(new BoundingBox());

        return navMesh;
    }
    
    private static List<Geometry> findGeometries(Node node, List<Geometry> geoms, NavMeshGenerator generator, Node originalRoot) {
        if (node instanceof Terrain) {
            Terrain terr = (Terrain)node;
            Mesh merged = generator.terrain2mesh(terr);
            Geometry g = new Geometry("mergedTerrain");
            g.setMesh(merged);
            if (node != originalRoot) {
                g.setLocalScale(((Node)terr).getLocalScale());
                g.setLocalTranslation(((Node)terr).getLocalTranslation());
            }
            geoms.add(g);
            return geoms;
        }
        
        for (Iterator<Spatial> it = node.getChildren().iterator(); it.hasNext();) {
            Spatial spatial = it.next();
            if (spatial instanceof Geometry) {
                geoms.add((Geometry) spatial);
            } else if (spatial instanceof Node) {
                findGeometries((Node) spatial, geoms, generator, originalRoot);
            }
        }
        return geoms;
    }
    
    // remove20170502
//    /**
//     * 为模型生成NavMesh寻路信息，该方法为模型添加一个名为"NavMesh"的子Geometry
//     * 作为Nav寻路信息.如果该模型已经存在"NavMesh"，则可设置replace=true来重
//     * 新生成并覆盖该NavMesh,否则不覆盖并返回null.
//     * @param terrainJ3oFile 模型文件，如：Scenes/scene.j3o
//     * @param replace 是否替换寻路信息
//     * @return 
//     */
//    public static Node createNavMesh(String terrainJ3oFile, boolean replace) {
//        long startTime = System.currentTimeMillis();
//        Node terrain = null;
//        Spatial temp = LuoYing.getAssetManager().loadModel(terrainJ3oFile);
//        if (temp instanceof Geometry) {
//            terrain = new Node();
//            terrain.attachChild(temp);
//        } else {
//            terrain = (Node) temp;
//        }
//        String navMeshName = "NavMesh";
//        Geometry geo = (Geometry) terrain.getChild(navMeshName);
//        if (geo != null) {
//            if (!replace) {
//                Logger.getLogger(NavMeshUtils.class.getName()).log(Level.WARNING, "The spatial already has a \"NavMesh\", geo={0}", geo.getName());
//                return null;
//            } else {
//                Logger.getLogger(NavMeshUtils.class.getName()).log(Level.WARNING, "The spatial already has a \"NavMesh\", it will be replace!");
//                geo.removeFromParent(); // 移除已有的NavMesh,将使用新的替代
//            }
//        }
//        Mesh mesh = new Mesh();
//        GeometryBatchFactory.mergeGeometries(GeometryUtils.findAllGeometry(terrain), mesh);
//        NavMeshGenerator generator = new NavMeshGenerator();
//        Mesh optiMesh = generator.optimize(mesh);
//        Geometry navMesh = new Geometry(navMeshName);
//        navMesh.setMesh(optiMesh);
//        navMesh.setCullHint(Spatial.CullHint.Always);
//        navMesh.setModelBound(new BoundingBox());
//        navMesh.setMaterial(new Material(LuoYing.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md"));
//        terrain.attachChild(navMesh);
//        long useTime = System.currentTimeMillis() - startTime;
//        Logger.getLogger(NavMeshUtils.class.getName()).log(Level.INFO, "Create nav mesh use time={0}", useTime);
//        return terrain;
//    }
//    
//    public static void main(String[] args) {
//        String filename = "Scenes/scene.j3o";
//        Node terrain = createNavMesh(filename, true);
//        // 覆盖保存到原来位置
//        if (terrain != null) {
//            ModelFileUtils.saveTo(terrain, filename);
//        } 
//    }
}
