/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.utils.modifier;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.logging.Level;
import java.util.logging.Logger;
import jme3tools.optimize.GeometryBatchFactory;
import name.huliqing.ly.LY;
import name.huliqing.ly.utils.ModelFileUtils;
import name.huliqing.ly.utils.GeometryUtils;

/**
 * 为场景生成寻路信息。
 * @author huliqing
 */
public class NavMeshUtils {
    
    /**
     * 为模型生成NavMesh寻路信息，该方法为模型添加一个名为"NavMesh"的子Geometry
     * 作为Nav寻路信息.如果该模型已经存在"NavMesh"，则可设置replace=true来重
     * 新生成并覆盖该NavMesh,否则不覆盖并返回null.
     * @param terrainJ3oFile 模型文件，如：Scenes/scene.j3o
     * @param replace 是否替换寻路信息
     * @return 
     */
    public static Node createNavMesh(String terrainJ3oFile, boolean replace) {
        long startTime = System.currentTimeMillis();
        Node terrain = null;
        Spatial temp = LY.getAssetManager().loadModel(terrainJ3oFile);
        if (temp instanceof Geometry) {
            terrain = new Node();
            terrain.attachChild(temp);
        } else {
            terrain = (Node) temp;
        }
        String navMeshName = "NavMesh";
        Geometry geo = (Geometry) terrain.getChild(navMeshName);
        if (geo != null) {
            if (!replace) {
                Logger.getLogger(NavMeshUtils.class.getName()).log(Level.WARNING, "The spatial already has a \"NavMesh\", geo={0}", geo.getName());
                return null;
            } else {
                Logger.getLogger(NavMeshUtils.class.getName()).log(Level.WARNING, "The spatial already has a \"NavMesh\", it will be replace!");
                geo.removeFromParent(); // 移除已有的NavMesh,将使用新的替代
            }
        }
        Mesh mesh = new Mesh();
        GeometryBatchFactory.mergeGeometries(GeometryUtils.findAllGeometry(terrain), mesh);
        NavMeshGenerator generator = new NavMeshGenerator();
        Mesh optiMesh = generator.optimize(mesh);
        Geometry navMesh = new Geometry(navMeshName);
        navMesh.setMesh(optiMesh);
        navMesh.setCullHint(Spatial.CullHint.Always);
        navMesh.setModelBound(new BoundingBox());
        navMesh.setMaterial(new Material(LY.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md"));
        terrain.attachChild(navMesh);
        long useTime = System.currentTimeMillis() - startTime;
        Logger.getLogger(NavMeshUtils.class.getName()).log(Level.INFO, "Create nav mesh use time={0}", useTime);
        return terrain;
    }
    
    public static void main(String[] args) {
        String filename = "Scenes/scene.j3o";
        Node terrain = createNavMesh(filename, true);
        // 覆盖保存到原来位置
        if (terrain != null) {
            ModelFileUtils.saveTo(terrain, filename);
        } 
    }
}
