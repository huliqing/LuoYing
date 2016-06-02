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
    
    private List<EnvData> trees;
    private List<EnvData> grasses;
    
    public RandomSceneData() {}
    
    public List<EnvData> getTrees() {
        return trees;
    }

    public void setTrees(List<EnvData> trees) {
        this.trees = trees;
    }

    public List<EnvData> getGrasses() {
        return grasses;
    }

    public void setGrasses(List<EnvData> grasses) {
        this.grasses = grasses;
    }
    
    
}
