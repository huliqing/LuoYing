/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.scene;

import java.util.Collection;
import name.huliqing.ly.object.SceneObject;

/**
 * @author huliqing
 */
public class SceneUtils {
    
    /**
     * 从场景中查找指定类型的Env,如果找不到则返回null.
     * @param <T>
     * @param scene
     * @param type
     * @return 
     */
    public static <T extends SceneObject> T findSceneObject(Scene scene, Class<T> type) {
        Collection<SceneObject> sceneObjects = scene.getSceneObjects();
        for (SceneObject so : sceneObjects) {
            if (so.getClass().isAssignableFrom(type)) {
                return (T) so;
            }
        }
        return null;
    }
}
