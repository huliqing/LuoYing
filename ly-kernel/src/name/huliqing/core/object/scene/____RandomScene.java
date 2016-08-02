package name.huliqing.core.object.scene;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.scene;
//
//import com.jme3.app.Application;
//import com.jme3.app.state.AppStateManager;
//import com.jme3.bullet.control.RigidBodyControl;
//import com.jme3.scene.Spatial;
//import java.util.ArrayList;
//import java.util.List;
//import name.huliqing.fighter.data.EnvData;
//import name.huliqing.fighter.object.DataFactory;
//import name.huliqing.fighter.object.env.Env;
//import name.huliqing.fighter.object.env.ModelEnv;
//
///**
// * 一个可随机生成树木或草地的场景
// * @author huliqing
// */
//public class RandomScene extends Scene<RandomSceneData> {
//
//    private List<Env> randomEnv;
//    
//    public RandomScene() {}
//
//    @Override
//    public void initialize(AppStateManager stateManager, Application app) {
//        super.initialize(stateManager, app);
//        // env列表
//        List<EnvData> randomEnvData = data.getRandomEnvs();
//        if (randomEnvData != null && !randomEnvData.isEmpty()) {
//            randomEnv = new ArrayList<Env>();
//            for (EnvData ed : randomEnvData) {
//                Env env = DataFactory.createProcessor(ed);
//                randomEnv.add(env);
//                env.initialize(app, this);
//            }
//        }
//    }
//    
//    @Override
//    public void update(float tpf) {
//        // ignore
//    }
//
//    @Override
//    public void cleanup() {
//         if (randomEnv != null) {
//            for (Env env : randomEnv) {
//                env.cleanup();
//            }
//        }
//        super.cleanup(); 
//    }
//    
//    /**
//     * 判断一个位置是否为空白区域，即无树木等障碍物之类。
//     * @param x
//     * @param z
//     * @param radius
//     * @return 
//     */
//    @Override
//    public boolean checkIsEmptyZone(float x, float z, float radius) {
//        if (super.checkIsEmptyZone(x, z, radius)) {
//            RigidBodyControl rbc;
//            float radiusSquare = radius * radius;
//            Spatial model;
//            if (randomEnv != null) {
//                for (Env s : randomEnv) {
//                    if (!(s instanceof ModelEnv)) {
//                        continue;
//                    }
//                    model = ((ModelEnv) s).getModel();
//                    if (model == null) {
//                        continue;
//                    }
//                    rbc = model.getControl(RigidBodyControl.class);
//                    if (rbc == null) {
//                        continue;
//                    }
//                    if (distanceSquare(x, z, rbc.getPhysicsLocation().x, rbc.getPhysicsLocation().z) < radiusSquare) {
//                        return false;
//                    }
//                }
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
//}
