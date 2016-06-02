///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.scene;
//
//import com.jme3.bullet.BulletAppState;
//import com.jme3.bullet.control.RigidBodyControl;
//import com.jme3.math.Vector3f;
//import com.jme3.scene.Node;
//import com.jme3.scene.Spatial;
//import java.util.ArrayList;
//import java.util.List;
//import jme3tools.optimize.GeometryBatchFactory;
//import name.huliqing.fighter.data.EnvData;
//import name.huliqing.fighter.data.SceneData;
//import name.huliqing.fighter.loader.Loader;
//import name.huliqing.fighter.object.ProtoObject;
//import name.huliqing.fighter.object.env.Env;
//import name.huliqing.fighter.utils.GeometryUtils;
//
///**
// * @deprecated 
// * @author huliqing
// */
//public class Scene extends Node implements ProtoObject<SceneData> {
//    
//    private SceneData data;
//    private Env sky;
//    private Env terrain;
//    private Spatial boundaryGeo;
//    private List<Env> trees;
//    private List<Env> grasses;
//    
//    private boolean loaded;
//    public Scene() {}
//    
//    public Scene(SceneData data) {
//        this.data = data;
//    }
//
//    @Override
//    public SceneData getData() {
//        return data;
//    }
//
//    @Override
//    public SceneData getUpdateData() {
//        // ignore
//        if (sky != null) 
//            sky.getUpdateData();
//        if (terrain != null) 
//            terrain.getUpdateData();
//        if (trees != null) {
//            for (Env tree : trees) {
//                tree.getUpdateData();
//            }
//        }
//        if (grasses != null) {
//            for (Env grass : grasses) {
//                grass.getUpdateData();
//            }
//        }
//        return data;
//    }
//    
//    public Spatial getTerrain() {
//        if (terrain != null) {
//            return terrain.getModel();
//        }
//        return null;
//    }
//    
//    /**
//     * 载入场景
//     */
//    public void load() {
//        if (loaded) return;
//        
//        // sky
//        EnvData skyData = data.getSky();
//        if (skyData != null) {
//            sky = Loader.loadEnv(skyData);
//            attachChild(sky.getModel());
//        }
//        
//        // terrain
//        EnvData terrainData = data.getTerrain();
//        if (terrainData != null) {
//            terrain = Loader.loadEnv(terrainData);
//            attachChild(terrain.getModel());
//        }
//        
//        // boundary
//        Vector3f boundary = data.getBoundary();
//        if (boundary != null) {
//            boundaryGeo = Loader.loadModel("Scenes/boundary/boundaryBox.j3o");
//            boundaryGeo.setLocalScale(boundary.mult(2));
//            boundaryGeo.setCullHint(CullHint.Always);
//            boundaryGeo.addControl(new RigidBodyControl(0));
//            attachChild(boundaryGeo);
//        }
//        
//        // trees
//        List<EnvData> treesData = data.getTrees();
//        if (treesData != null && !treesData.isEmpty()) {
//            trees = new ArrayList<Env>(treesData.size());
//            for (EnvData treeData : treesData) {
//                
//                // 修正树的高度，不要让树沉在地下之下
//                if (terrain != null) {
//                    GeometryUtils.getTerrainHeight(terrain.getModel(), treeData.getLocation());
//                }
//                treeData.getLocation().subtractLocal(0, 0.5f, 0);
//                Env tree = Loader.loadEnv(treeData);
//                trees.add(tree);
//                attachChild(tree.getModel());
//            }
//        }
//        
//        // grass 
//        List<EnvData> grassesData = data.getGrasses();
//        if (grassesData != null && !grassesData.isEmpty()) {
//            grasses = new ArrayList<Env>(grassesData.size());
//            for (EnvData grassData : grassesData) {
//                
//                // 修正高度，否则花草可能在地面下
//                if (terrain != null) {
//                    GeometryUtils.getTerrainHeight(terrain.getModel(), grassData.getLocation());
//                }
//                
//                Env grass = Loader.loadEnv(grassData);
//                grasses.add(grass);
//                attachChild(grass.getModel());
//            }
//        }
//        loaded = true;
//    }
//    
//    /**
//     * 加入物理场景
//     * @param bas 
//     */
//    public void initBulletAppState(BulletAppState bas) {
//        // 地面
//        if (terrain != null) {
//            initPhysics(bas, terrain.getModel());
//        }
//        // 边界
//        initPhysics(bas, boundaryGeo);
//        // 树木
//        if (trees != null) {
//            for (Env tree : trees) {
//                initPhysics(bas, tree.getModel());
//            }
//        }
//    }
//    
//    private void initPhysics(BulletAppState bulletAppState, Spatial s) {
//        if (s == null) 
//            return;
//        RigidBodyControl rbc = s.getControl(RigidBodyControl.class);
//        if (rbc != null) {
//            bulletAppState.getPhysicsSpace().add(rbc);
//        }
//    }
//    
//    /**
//     * 判断一个位置是否为空白区域，即无树木等障碍物之类。
//     * @param x
//     * @param z
//     * @param radius
//     * @return 
//     */
//    public boolean checkIsEmptyZone(float x, float z, float radius) {
//        if (trees == null || trees.isEmpty())
//            return true;
//        float radiusSquare = radius * radius;
//        for (Env tree : trees) {
//            Vector3f loc = tree.getModel().getLocalTranslation();
//            if (distanceSquare(x, z, loc.x, loc.z) < radiusSquare) {
//                return false;
//            }
//        }
//        return true;
//    }
//    
//    private double distanceSquare(float x, float z, float otherX, float otherZ) {
//        double dx = x - otherX;
//        double dz = z - otherZ;
//        return dx * dx + dz * dz;
//    }
//    
//}
