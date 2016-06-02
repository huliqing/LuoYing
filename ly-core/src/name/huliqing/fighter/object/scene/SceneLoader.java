/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.scene;

import com.jme3.math.Vector3f;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.data.SceneData;
import name.huliqing.fighter.object.DataFactory;
import name.huliqing.fighter.object.DataLoader;

/**
 * 用于载入场景数据
 * @author huliqing
 */
public class SceneLoader<T extends SceneData> implements DataLoader<T> {

    @Override
    public void load(Proto proto, T store) {
        // sky
        String envSkyId = proto.getAttribute("sky");
        if (envSkyId != null) {
            store.setSky((EnvData)DataFactory.createData(envSkyId));
        }
        
        // terrain
        String terrainId = proto.getAttribute("terrain");
        if (terrainId != null) {
            store.setTerrain((EnvData)DataFactory.createData(terrainId));
        }
        
        // 边界
        store.setBoundary(proto.getAsVector3f("boundary"));
        
        // 直射光
        store.setDirectionalLightColor(proto.getAsColor("directionalLightColor"));
        Vector3f directionalLightDir = proto.getAsVector3f("directionalLightDir");
        if (directionalLightDir != null) {
            directionalLightDir.normalizeLocal();
            store.setDirectionalLightDir(directionalLightDir);
        }
        
        // 环境光
        store.setAmbientLightColor(proto.getAsColor("ambientLightColor"));
        store.setUseShadow(proto.getAsBoolean("useShadow", false));
        store.setUsePhysics(proto.getAsBoolean("usePhysics", false));
        store.setGravity(proto.getAsVector3f("gravity", new Vector3f(0, -10f, 0)));
        store.setDebugPhysics(proto.getAsBoolean("debugPhysics", false));
    }
    
}
