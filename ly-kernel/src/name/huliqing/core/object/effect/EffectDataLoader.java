/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.effect;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import name.huliqing.core.data.EffectData;
import name.huliqing.core.xml.Proto;
import name.huliqing.core.enums.TracePositionType;
import name.huliqing.core.enums.TraceType;
import name.huliqing.core.xml.DataLoader;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class EffectDataLoader<T extends EffectData> implements DataLoader<T>{

    @Override
    public void load(Proto proto, T store) {

        store.setUseTime(proto.getAsFloat("useTime", 1.0f));
        store.setTracePosition(TraceType.identity(proto.getAttribute("tracePosition", TraceType.no.name())));
        store.setTraceRotation(TraceType.identity(proto.getAttribute("traceRotation", TraceType.no.name())));
        store.setTracePositionType(TracePositionType.identify(proto.getAttribute("tracePositionType", TracePositionType.origin.name())));

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
