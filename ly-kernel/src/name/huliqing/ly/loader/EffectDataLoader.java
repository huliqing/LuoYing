/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.loader;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import name.huliqing.ly.data.EffectData;
import name.huliqing.ly.object.effect.TracePositionType;
import name.huliqing.ly.object.effect.TraceType;
import name.huliqing.ly.xml.Proto;
import name.huliqing.ly.xml.DataLoader;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class EffectDataLoader<T extends EffectData> implements DataLoader<T>{

    @Override
    public void load(Proto proto, T store) {

        store.setUseTime(proto.getAsFloat("useTime", 1.0f));
        store.setTracePosition(TraceType.identity(proto.getAsString("tracePosition", TraceType.no.name())));
        store.setTraceRotation(TraceType.identity(proto.getAsString("traceRotation", TraceType.no.name())));
        store.setTracePositionType(TracePositionType.identify(proto.getAsString("tracePositionType", TracePositionType.origin.name())));
        store.setAutoDetach(proto.getAsBoolean("autoDetach", false));

        // 特效的初始位置
        Vector3f initLocation = proto.getAsVector3f("initLocation");
        if (initLocation != null) {
            store.setInitLocation(initLocation);
        }

        // 特效的初始旋转
        float[] initRotation = proto.getAsFloatArray("initRotation");
        if (initRotation != null) {
            Quaternion rot = new Quaternion();
            rot.fromAngles(initRotation);
            store.setInitRotation(rot);
        }

        // 缩放
        Vector3f initScale = proto.getAsVector3f("initScale");
        if (initScale != null) {
            store.setInitScale(initScale);
        }
        
        // 以下参数暂不开放到xml中配置。
//        store.setSpeed(speed);
    }
    
    
}
