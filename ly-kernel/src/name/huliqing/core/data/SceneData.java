/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.data;

import name.huliqing.core.data.env.EnvData;
import com.jme3.network.serializing.Serializable;
import java.util.List;

/**
 * @author huliqing
 */
@Serializable
public class SceneData extends ObjectData {

    // 环境物体
    private List<EnvData> envs;
    
    public SceneData() {}

    public List<EnvData> getEnvs() {
        return envs;
    }

    public void setEnvs(List<EnvData> envs) {
        this.envs = envs;
    }
    
}
