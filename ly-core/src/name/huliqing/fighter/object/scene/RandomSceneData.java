/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.scene;

import com.jme3.network.serializing.Serializable;
import java.util.List;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.data.SceneData;

/**
 *
 * @author huliqing
 */
@Serializable
public class RandomSceneData extends SceneData {
    
    private List<EnvData> randomEnvs;
    
    public RandomSceneData() {}

    public List<EnvData> getRandomEnvs() {
        return randomEnvs;
    }

    public void setRandomEnvs(List<EnvData> randomEnvs) {
        this.randomEnvs = randomEnvs;
    }


}
