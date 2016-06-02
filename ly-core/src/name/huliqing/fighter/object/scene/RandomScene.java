/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.scene;

import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.object.env.Env;
import name.huliqing.fighter.utils.GeometryUtils;

/**
 * 一个可随机生成树木或草地的场景
 * @author huliqing
 */
public class RandomScene extends Scene<RandomSceneData> {
    private final PlayService playService = Factory.get(PlayService.class);

    private Node treeRoot;
    private Node grassRoot;
    
    private List<Env> trees;
    
    public RandomScene() {}

    @Override
    public void initialize() {
        super.initialize();
        treeRoot = new Node("TreeRoot");
        grassRoot = new Node("GlassRoot");
        
        // trees
        List<EnvData> treesData = data.getTrees();
        if (treesData != null && !treesData.isEmpty()) {
            trees = new ArrayList<Env>(treesData.size());
            for (EnvData treeData : treesData) {
                // 修正树的高度，不要让树沉在地下之下
                if (terrain != null) {
                    GeometryUtils.getTerrainHeight(terrain.getModel(), treeData.getLocation());
                }
                treeData.getLocation().subtractLocal(0, 0.5f, 0);
                Env tree = Loader.loadEnv(treeData);
                trees.add(tree);
                treeRoot.attachChild(tree.getModel());
                addPhysicsObject(tree.getModel());
            }
        }
        
        // grass 
        List<EnvData> grassesData = data.getGrasses();
        if (grassesData != null && !grassesData.isEmpty()) {
            for (EnvData grassData : grassesData) {
                // 修正高度，否则花草可能在地面下
                if (terrain != null) {
                    GeometryUtils.getTerrainHeight(terrain.getModel(), grassData.getLocation());
                }
                Env grass = Loader.loadEnv(grassData);
                grassRoot.attachChild(grass.getModel());
            }
        }
        
        treeRoot.setShadowMode(RenderQueue.ShadowMode.Cast);
        localRoot.attachChild(treeRoot);
        localRoot.attachChild(grassRoot);
        // 需要手动update一次，因为localRoot是直接添加在ViewPort下的
        localRoot.updateGeometricState();
    }
    
    @Override
    public void update(float tpf) {
        // ignore
    }

    @Override
    public void cleanup() {
        super.cleanup(); 
    }
    
    /**
     * 判断一个位置是否为空白区域，即无树木等障碍物之类。
     * @param x
     * @param z
     * @param radius
     * @return 
     */
    @Override
    public boolean checkIsEmptyZone(float x, float z, float radius) {
        if (trees == null || trees.isEmpty())
            return true;
        float radiusSquare = radius * radius;
        for (Env tree : trees) {
            Vector3f loc = tree.getModel().getLocalTranslation();
            if (distanceSquare(x, z, loc.x, loc.z) < radiusSquare) {
                return false;
            }
        }
        return true;
    }
    
    private double distanceSquare(float x, float z, float otherX, float otherZ) {
        double dx = x - otherX;
        double dz = z - otherZ;
        return dx * dx + dz * dz;
    }


}
