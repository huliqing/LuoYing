/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.scene;

import java.util.List;
import name.huliqing.ly.object.env.Env;

/**
 *
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
    public static <T extends Env> T findEnv(Scene scene, Class<T> type) {
        List<Env> envs = scene.getEnvs();
        for (Env env : envs) {
            if (env.getClass().isAssignableFrom(type)) {
                return (T) env;
            }
        }
        return null;
    }
}
