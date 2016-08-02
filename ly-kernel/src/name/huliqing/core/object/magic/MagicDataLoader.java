/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.magic;

import com.jme3.math.Quaternion;
import name.huliqing.core.data.HitCheckerData;
import name.huliqing.core.data.MagicData;
import name.huliqing.core.data.Proto;
import name.huliqing.core.enums.TraceType;
import name.huliqing.core.object.DataFactory;
import name.huliqing.core.object.DataLoader;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class MagicDataLoader<T extends MagicData> implements DataLoader<T> {

    @Override
    public void load(Proto proto, T store) {
        store.setLocation(proto.getAsVector3f("location"));
        float[] rotationAngle = proto.getAsFloatArray("rotation");
        if (rotationAngle != null) {
            Quaternion rotation = new Quaternion();
            rotation.fromAngles(rotationAngle);
            store.setRotation(rotation);
        }
        store.setDebug(proto.getAsBoolean("debug", false));
        store.setUseTime(proto.getAsFloat("useTime", 1.0f));
        store.setTracePosition(TraceType.identity(proto.getAttribute("tracePosition", TraceType.no.name())));
        store.setTracePositionOffset(proto.getAsVector3f("tracePositionOffset"));
        
        store.setTraceRotation(TraceType.identity(proto.getAttribute("traceRotation", TraceType.no.name())));
        float[] tempRotationOffset = proto.getAsFloatArray("traceRotationOffset");// xyz angle
        if (tempRotationOffset != null) {
            Quaternion traceRotationOffset = new Quaternion();
            traceRotationOffset.fromAngles(tempRotationOffset);
            store.setTraceRotationOffset(traceRotationOffset);
        }
        
        String hitCheckerId = proto.getAttribute("hitChecker");
        if (hitCheckerId != null) {
            HitCheckerData hd = DataFactory.createData(hitCheckerId);
            store.setHitCheckerData(hd);
        }
        
        // 以下三个参数一般作为动态在程序中设置,无法也不能作为静态设置存在。
        // sourceActor;
        // targetActor;
        // timeUsed;
        
    }
    
    
}
