/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.scene;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.data.SceneData;
import name.huliqing.fighter.object.DataFactory;
import name.huliqing.fighter.object.DataLoader;

/**
 * 用于载入场景数据
 * @author huliqing
 * @param <T>
 */
public class SceneLoader<T extends SceneData> implements DataLoader<T> {

    @Override
    public void load(Proto proto, T store) {        
        
        store.setUseShadow(proto.getAsBoolean("useShadow", false));
        store.setUsePhysics(proto.getAsBoolean("usePhysics", false));
        store.setGravity(proto.getAsVector3f("gravity", new Vector3f(0, -10f, 0)));
        store.setDebugPhysics(proto.getAsBoolean("debugPhysics", false));
        
        // 环境物体
        String[] envIds = proto.getAsArray("envs");
        if (envIds != null && envIds.length > 0) {
            List<EnvData> edStore = store.getEnvs();
            if (edStore == null) {
                edStore = new ArrayList<EnvData>(envIds.length);
                store.setEnvs(edStore);
            }
            for (String eid : envIds) {
                EnvData ed = DataFactory.createData(eid);
                edStore.add(ed);
            }
        }
    }
    
}
