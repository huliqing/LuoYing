/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import name.huliqing.fighter.data.Proto;

/**
 * 环境物体数据载入器
 * @author huliqing
 * @param <T>
 */
public class ModelEnvLoader<T extends ModelEnvData> extends EnvDataLoader<T> {

    @Override
    public void load(Proto proto, T store) {
        store.setFile(proto.getFile());
        Vector3f location = proto.getAsVector3f("location");
        if (location != null) {
            store.setLocation(location);
        }
        Quaternion rotation = proto.getAsQuaternion("rotation");
        if (rotation != null) {
            store.setRotation(rotation);
        }
        Vector3f scale = proto.getAsVector3f("scale");
        if (scale != null) {
            store.setScale(scale);
        }
        store.setPhysics(proto.getAsBoolean("physics", false));
        store.setMass(proto.getAsFloat("mass", 0));
        store.setFriction(proto.getAsFloat("friction", 1.0f));
        
        String shadowMode = proto.getAttribute("shadowMode");
        if (shadowMode != null) {
            if (shadowMode.equalsIgnoreCase(ShadowMode.Cast.name())) {
                store.setShadowMode(ShadowMode.Cast);
                
            } else if (shadowMode.equalsIgnoreCase(ShadowMode.CastAndReceive.name())) {
                store.setShadowMode(ShadowMode.CastAndReceive);
                
            } else if (shadowMode.equalsIgnoreCase(ShadowMode.Inherit.name())) {
                store.setShadowMode(ShadowMode.Inherit);
                
            } else if (shadowMode.equalsIgnoreCase(ShadowMode.Off.name())) {
                store.setShadowMode(ShadowMode.Off);
                
            } else if (shadowMode.equalsIgnoreCase(ShadowMode.Receive.name())) {
                store.setShadowMode(ShadowMode.Receive);
            }
        }
        
        store.setUseUnshaded(proto.getAsBoolean("useUnshaded", false));
        store.setTerrain(proto.getAsBoolean("terrain", false));
    }
    
}
