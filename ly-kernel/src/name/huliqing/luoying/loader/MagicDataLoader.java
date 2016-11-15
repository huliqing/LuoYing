/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.loader;

import name.huliqing.luoying.data.EffectData;
import name.huliqing.luoying.data.MagicData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.xml.DataLoader;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class MagicDataLoader<T extends MagicData> implements DataLoader<T> {

    @Override
    public void load(Proto proto, T store) {
        
//        String[] effectIds = proto.getAsArray("effects");
//        if (effectIds != null && effectIds.length > 0) {
//            EffectData[] effectDatas = new EffectData[effectIds.length];
//            for (int i = 0; i < effectIds.length; i++) {
//                effectDatas[i] = Loader.loadData(effectIds[i]);
//            }
//            store.setEffectDatas(effectDatas);
//        }
        
//        store.setLocation(proto.getAsVector3f("location"));
//        float[] rotationAngle = proto.getAsFloatArray("rotation");
//        if (rotationAngle != null) {
//            Quaternion rotation = new Quaternion();
//            rotation.fromAngles(rotationAngle);
//            store.setRotation(rotation);
//        }
//        store.setDebug(proto.getAsBoolean("debug", false));
//        store.setUseTime(proto.getAsFloat("useTime", 1.0f));
//        store.setTracePosition(TraceType.identity(proto.getAsString("tracePosition", TraceType.no.name())));
//        store.setTracePositionOffset(proto.getAsVector3f("tracePositionOffset"));
//        
//        store.setTraceRotation(TraceType.identity(proto.getAsString("traceRotation", TraceType.no.name())));
//        float[] tempRotationOffset = proto.getAsFloatArray("traceRotationOffset");// xyz angle
//        if (tempRotationOffset != null) {
//            Quaternion traceRotationOffset = new Quaternion();
//            traceRotationOffset.fromAngles(tempRotationOffset);
//            store.setTraceRotationOffset(traceRotationOffset);
//        }
//        
//        String hitCheckerId = proto.getAsString("hitChecker");
//        if (hitCheckerId != null) {
//            HitCheckerData hd = DataFactory.createData(hitCheckerId);
//            store.setHitCheckerData(hd);
//        }
        
        // 以下三个参数一般作为动态在程序中设置,无法也不能作为静态设置存在。
        // sourceActor;
        // targetActor;
        // timeUsed;
        
    }
    
    
}
