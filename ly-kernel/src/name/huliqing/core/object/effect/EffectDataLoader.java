/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.effect;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import name.huliqing.core.data.AnimData;
import name.huliqing.core.data.EffectData;
import name.huliqing.core.xml.Proto;
import name.huliqing.core.enums.TracePositionType;
import name.huliqing.core.enums.TraceType;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.xml.DataLoader;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class EffectDataLoader<T extends EffectData> implements DataLoader<T>{

    @Override
    public void load(Proto proto, T store) {
//        store.setPhaseTimeStart(proto.getAsFloat("timeStart", 0));
//        store.setPhaseTimeDisplay(proto.getAsFloat("timeDisplay", 0));
//        store.setPhaseTimeEnd(proto.getAsFloat("timeEnd", 0));
//        store.setSound(proto.getAttribute("sound"));
//        store.setSoundInstance(proto.getAsBoolean("soundInstance", false));

        store.setUseTime(proto.getAsFloat("useTime", 1));
        
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

//        // anim
//        String[] tempAnimStarts = proto.getAsArray("animStarts");
//        String[] tempAnimAll = proto.getAsArray("animAll");
//        String[] tempAnimDisplays = proto.getAsArray("animDisplays");
//        String[] tempAnimEnds = proto.getAsArray("animEnds");
//        if (tempAnimStarts != null && tempAnimStarts.length > 0) {
//            AnimData[] animStarts = new AnimData[tempAnimStarts.length];
//            for (int i = 0; i < tempAnimStarts.length; i++) {
//                animStarts[i] = (DataFactory.createData(tempAnimStarts[i]));
//            }
//            store.setAnimStarts(animStarts);
//        }
//        if (tempAnimAll != null && tempAnimAll.length > 0) {
//            AnimData[] animAll = new AnimData[tempAnimAll.length];
//            for (int i = 0; i < tempAnimAll.length; i++) {
//                animAll[i] = (DataFactory.createData(tempAnimAll[i]));
//            }
//            store.setAnimAll(animAll);
//        }
//        if (tempAnimDisplays != null && tempAnimDisplays.length > 0) {
//            AnimData[] animDisplays = new AnimData[tempAnimDisplays.length];
//            for (int i = 0; i < tempAnimDisplays.length; i++) {
//                animDisplays[i] = (DataFactory.createData(tempAnimDisplays[i]));
//            }
//            store.setAnimDisplays(animDisplays);
//        }
//        if (tempAnimEnds != null && tempAnimEnds.length > 0) {
//            AnimData[] animEnds = new AnimData[tempAnimEnds.length];
//            for (int i = 0; i < tempAnimEnds.length; i++) {
//                animEnds[i] = (DataFactory.createData(tempAnimEnds[i]));
//            }
//            store.setAnimEnds(animEnds);
//        }

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
//        store.setPhase(EffectPhase.none);
//        store.setSpeed(speed);
//        store.setTimeUsed(timeUsed);
    }
    
    
}