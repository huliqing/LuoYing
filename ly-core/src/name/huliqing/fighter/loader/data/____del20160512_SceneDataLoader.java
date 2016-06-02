///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.loader.data;
//
//import name.huliqing.fighter.data.DataLoaderFactory;
//import com.jme3.math.FastMath;
//import com.jme3.math.Vector3f;
//import com.jme3.util.TempVars;
//import java.util.ArrayList;
//import java.util.List;
//import name.huliqing.fighter.data.EnvData;
//import name.huliqing.fighter.data.Proto;
//import name.huliqing.fighter.data.SceneData;
//import name.huliqing.fighter.utils.MathUtils;
//
///**
// *
// * @author huliqing
// */
//public class SceneDataLoader implements DataLoader<SceneData>{
//
//    @Override
//    public SceneData loadData(Proto proto) {
////        SceneData data = new SceneData(proto.getId());
////        
////        // sky
////        String envSkyId = proto.getAttribute("sky");
////        if (envSkyId != null) {
////            data.setSky(DataLoaderFactory.createEnvData(envSkyId));
////        }
////        
////        // terrain
////        String terrainId = proto.getAttribute("terrain");
////        if (terrainId != null) {
////            data.setTerrain(DataLoaderFactory.createEnvData(terrainId));
////        }
////        
////        // 边界
////        Vector3f boundary = proto.getAsVector3f("boundary");
////        if (boundary != null) {
////            data.setBoundary(boundary);
////        }
////        
////        String tagName = data.getProto().getTagName();
////        if (tagName.equals("random")) {
////            loadRandom(proto, data);
////        } else {
////            throw new UnsupportedOperationException("Unsupported tagName=" + tagName);
////        }
////        return data;
//        
//        throw new UnsupportedOperationException();
//    }
//    
////    // 随机场景，树木花草要随机
////    private SceneData loadRandom(Proto in, SceneData out) {
////        // 一些被保护的区域，不能在这些区域内生成障碍物（如树木或其它）
////        String[] tempEZS = in.getAsArray("emptyZones");
////        List<EmptyZone> emptyZones = null;
////        if (tempEZS != null && tempEZS.length > 0) {
////            emptyZones = new ArrayList<EmptyZone>(tempEZS.length);
////            String[] zone;
////            for (String ez : tempEZS) {
////                zone = ez.split("\\|");
////                EmptyZone emptyZone = new EmptyZone(Float.parseFloat(zone[0])
////                        ,Float.parseFloat(zone[1])
////                        ,Float.parseFloat(zone[2]));
////                emptyZones.add(emptyZone);
////            }
////        }
////        
////        // ---- 生成随机位置
////        TempVars tv = TempVars.get();
////        Vector3f tempPos = tv.vect1;
////        float xExt = 80;
////        float zExt = 80;
////        Vector3f boundary = out.getBoundary();
////        if (boundary != null) {
////            xExt = boundary.getX();
////            zExt = boundary.getZ();
////        }
////        
////        // random trees
////        List<EnvData> treesData;
////        String[] treeIds = in.getAsArray("trees");
////        int treeSize = in.getAsInteger("treeSize", 0);
////        if (treeIds != null && treeSize > 0) {
////            treesData = new ArrayList<EnvData>(treeSize);
////            for (int i = 0; i < treeSize; i++) {
////                tempPos.setY(0);
////                
////                // 生成随机位置（但需要防止生成的位置与空白区域冲突）
////                do {
////                    MathUtils.getRandomPosition(xExt, zExt, tempPos);
////                } while (checkInEmptyZone(tempPos.x, tempPos.z, emptyZones));
////                
////                EnvData treeData = DataLoaderFactory.createEnvData(
////                        treeIds[FastMath.nextRandomInt(0, treeIds.length - 1)]);
////                treeData.setLocation(tempPos);
////                treesData.add(treeData);
////            }
////            out.setTrees(treesData);
////        }
////        
////        // random grasses
////        List<EnvData> grassesData;
////        String[] grassIds = in.getAsArray("grasses");
////        int grassSize = in.getAsInteger("grassSize", 0);
////        if (grassIds != null && grassSize > 0) {
////            grassesData = new ArrayList<EnvData>(grassSize);
////            for (int i = 0; i < grassSize; i++) {
////                tempPos.setY(0);
////                MathUtils.getRandomPosition(xExt, zExt, tempPos);
////                EnvData grassData = DataLoaderFactory.createEnvData(
////                        grassIds[FastMath.nextRandomInt(0, grassIds.length - 1)]);
////                grassData.setLocation(tempPos);
////                grassesData.add(grassData);
////            }
////            out.setGrasses(grassesData);
////        }
////        
////        tv.release();
////        return out;
////    }
////    
////    // 检查目标点是否在空白区域内
////    private boolean checkInEmptyZone(float otherX, float otherZ, List<EmptyZone> emptyZones) {
////        if (emptyZones == null || emptyZones.isEmpty())
////            return false;
////        for (EmptyZone ez : emptyZones) {
////            if (ez.checkInZone(otherX, otherZ)) {
////                return true;
////            }
////        }
////        return false;
////    }
////    
////    private class EmptyZone {
////        private float x;
////        private float z;
////        private float radiusSquare;
////        public EmptyZone(float x, float z, float radius) {
////            this.x = x;
////            this.z = z;
////            this.radiusSquare = radius * radius;
////        }
////        public boolean checkInZone(float ox, float oz) {
////            double dx = x - ox;
////            double dz = z - oz;
////            double distanceSquare = (dx * dx + dz * dz);
////            return distanceSquare < radiusSquare;
////        }
////    }
//}
