/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.serializing.Serializable;

/**
 *
 * @author huliqing
 */
@Serializable
public class SceneLoadedMess extends GameMess {
    
    private String sceneId;
    
    public SceneLoadedMess() {}
    
    public SceneLoadedMess(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneId() {
        return sceneId;
    }
    
    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }
}
