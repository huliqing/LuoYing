/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.loader;

import name.huliqing.luoying.data.EffectData;
import name.huliqing.luoying.xml.Proto;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class EffectDataLoader<T extends EffectData> extends EntityDataLoader <T>{

    @Override
    public void load(Proto proto, T store) {
        super.load(proto, store);
        
        // ... do effect loader
        
        // remove20161014
//        store.setUseTime(proto.getAsFloat("useTime", 1.0f));
//        store.setTracePosition(TraceType.identity(proto.getAsString("tracePosition", TraceType.no.name())));
//        store.setTraceRotation(TraceType.identity(proto.getAsString("traceRotation", TraceType.no.name())));
//        store.setTracePositionType(TracePositionType.identify(proto.getAsString("tracePositionType", TracePositionType.origin.name())));
//        store.setAutoDetach(proto.getAsBoolean("autoDetach", false));
//
//        // 特效的初始位置
//        Vector3f initLocation = proto.getAsVector3f("initLocation");
//        if (initLocation != null) {
//            store.setInitLocation(initLocation);
//        }
//
//        // 特效的初始旋转
//        float[] initRotation = proto.getAsFloatArray("initRotation");
//        if (initRotation != null) {
//            Quaternion rot = new Quaternion();
//            rot.fromAngles(initRotation);
//            store.setInitRotation(rot);
//        }
//
//        // 缩放
//        Vector3f initScale = proto.getAsVector3f("initScale");
//        if (initScale != null) {
//            store.setInitScale(initScale);
//        }
//        
//        // 以下参数暂不开放到xml中配置。
////        store.setSpeed(speed);
    }
    
    
}
