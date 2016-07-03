/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.scene;

import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.object.DataFactory;
import name.huliqing.fighter.object.env.ModelEnvData;
import name.huliqing.fighter.utils.ConvertUtils;
import name.huliqing.fighter.utils.MathUtils;

/**
 * 载入随机场景数据
 * @author huliqing
 * @param <T>
 */
public class RandomSceneLoader<T extends RandomSceneData> extends SceneLoader<T> {

    @Override
    public void load(Proto proto, T store) {
        // super loader
        super.load(proto, store);
        
         // EmptyZones
        String[] tempEZS = proto.getAsArray("emptyZones");
        List<EmptyZone> emptyZones = null;
        if (tempEZS != null && tempEZS.length > 0) {
            emptyZones = new ArrayList<EmptyZone>(tempEZS.length);
            String[] zone;
            for (String ez : tempEZS) {
                zone = ez.split("\\|");
                EmptyZone emptyZone = new EmptyZone(Float.parseFloat(zone[0])
                        ,Float.parseFloat(zone[1])
                        ,Float.parseFloat(zone[2]));
                emptyZones.add(emptyZone);
            }
        }
        
        // 载入随机的树木和花草
        TempVars tv = TempVars.get();
        Vector3f tempPos = tv.vect1;
        float xExt = 80;
        float zExt = 80;
        Vector3f boundary = store.getBoundary();
        if (boundary != null) {
            xExt = boundary.getX();
            zExt = boundary.getZ();
        }

        // 随机环境物体ID，格式："envId|size, envId2|size, ..."
        // envId关联到环境物体(env.xml), size指定该随机物体的数量。
        String[] reArrs = proto.getAsArray("randomEnvs");
        String envId;
        int size;
        String[] tempArr;
        List<EnvData> randomEnvs;
        if (reArrs != null && reArrs.length > 0) {
            randomEnvs = new ArrayList<EnvData>();
            store.setRandomEnvs(randomEnvs);
            for (String reArr : reArrs) {
                tempArr = reArr.split("\\|");
                envId = tempArr[0];
                size = tempArr.length > 1 ? ConvertUtils.toInteger(tempArr[1], 1) : 1;
                for (int j = 0; j < size; j++) {
                    EnvData envData = DataFactory.createData(envId);
                    randomEnvs.add(envData);
                    
                    if (envData instanceof ModelEnvData) {
                        // 生成随机位置（但需要防止生成的位置与空白区域冲突）
                        ModelEnvData med = (ModelEnvData) envData;
                        do {
                            MathUtils.getRandomPosition(xExt, zExt, tempPos);
                        } while (checkInEmptyZone(tempPos.x, tempPos.z, emptyZones));
                        med.setLocation(tempPos);
                    } 
                }
            }
        }
        
        tv.release();
    }
    
    // 检查目标点是否在空白区域内
    private boolean checkInEmptyZone(float otherX, float otherZ, List<EmptyZone> emptyZones) {
        if (emptyZones == null || emptyZones.isEmpty())
            return false;
        for (EmptyZone ez : emptyZones) {
            if (ez.checkInZone(otherX, otherZ)) {
                return true;
            }
        }
        return false;
    }
    
    // 注意不要把没有经过Serializer.registerClass注册的特殊类型作为类字段中去。
    // 即使是加了transient也不行，会导致联网异常
    private class EmptyZone {
        private float x;
        private float z;
        private float radiusSquare;
        
        public EmptyZone() {}
        public EmptyZone(float x, float z, float radius) {
            this.x = x;
            this.z = z;
            this.radiusSquare = radius * radius;
        }
        public boolean checkInZone(float ox, float oz) {
            double dx = x - ox;
            double dz = z - oz;
            double distanceSquare = (dx * dx + dz * dz);
            return distanceSquare < radiusSquare;
        }
    }
}
