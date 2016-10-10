/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.data.SceneData;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.env.TerrainEnv;
import name.huliqing.ly.object.scene.Scene;
import name.huliqing.ly.utils.GeometryUtils;

/**
 * just for test
 * @author huliqing
 */
public class SceneServiceImpl implements SceneService {

    @Override
    public void inject() {
        // 
    }

    @Override
    public Scene loadScene(String sceneId) {
        return Loader.load(sceneId);
    }

    @Override
    public Scene loadScene(SceneData sceneData) {
        return Loader.load(sceneData);
    }

    @Override
    public Vector3f getSceneHeight(Scene scene, float x, float z) {
        // 在场景载入完毕之后将植皮位置移到terrain节点的上面。
        List<TerrainEnv> sos = scene.getEntities(TerrainEnv.class, new ArrayList<TerrainEnv>());
        Vector3f terrainHeight = null;
        for (TerrainEnv so : sos) {
            Spatial terrain = so.getSpatial();
            Vector3f terrainPoint = GeometryUtils.getModelHeight(terrain, x, z);
            if (terrainPoint != null) {
                if (terrainHeight == null || terrainPoint.y > terrainHeight.y) {
                    terrainHeight = terrainPoint;
                }
            }
        }
        return terrainHeight;
    }

}
