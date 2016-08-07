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
        store.setTracePositionOffset(proto.getAsVector3f("tracePositionOffset"));
        store.setTracePositionType(TracePositionType.identify(proto.getAttribute("tracePositionType", TracePositionType.origin.name())));
        
        store.setTraceRotation(TraceType.identity(proto.getAttribute("traceRotation", TraceType.no.name())));
        float[] tempRotationOffset = proto.getAsFloatArray("traceRotationOffset");// xyz angle
        if (tempRotationOffset != null) {
            Quaternion traceRotationOffset = new Quaternion();
            traceRotationOffset.fromAngles(tempRotationOffset);
            store.setTraceRotationOffset(traceRotationOffset);
        }

        // 特效的初始位置
        Vector3f location = proto.getAsVector3f("location");
        if (location != null) {
            store.setLocation(location);
        }

        // 特效的初始旋转
        float[] rotation = proto.getAsFloatArray("rotation");
        if (rotation != null) {
            Quaternion rot = new Quaternion();
            rot.fromAngles(rotation);
            store.setRotation(rot);
        }

        // 缩放
        Vector3f scale = proto.getAsVector3f("scale");
        if (scale != null) {
            store.setScale(scale);
        }
        
        // 以下参数暂不开放到xml中配置。
//        store.setSpeed(speed);
    }
    
    
}
