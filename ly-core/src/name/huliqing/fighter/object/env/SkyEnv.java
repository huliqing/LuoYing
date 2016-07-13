/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import name.huliqing.fighter.data.EnvData;
import name.huliqing.fighter.object.scene.Scene;

/**
 * 天空盒模型
 * @author huliqing
 * @param <T>
 */
public class SkyEnv <T extends EnvData> extends AbstractEnv<T> {

    // 基本文件夹路径，如：“Textures/tex/sky/default_min/"
    // 如果指定了这个路径，则把它加在west,east,north,south,up,down前面
    private String baseDir;
    
    private String west;
    private String east;
    private String north;
    private String south;
    private String up;
    private String down;
    
    // ---- inner
    private Spatial sky;

    @Override
    public void setData(T data) {
        super.setData(data);
        baseDir = data.getAttribute("baseDir");
        west = data.getAttribute("west");
        east = data.getAttribute("east");
        north = data.getAttribute("north");
        south = data.getAttribute("south");
        up = data.getAttribute("up");
        down = data.getAttribute("down");
    }

    @Override
    public void initialize(Application app, Scene scene) {
        super.initialize(app, scene);
        if (sky == null) {
            AssetManager am = app.getAssetManager();
            Texture w = am.loadTexture(baseDir != null ? baseDir + west : west);
            Texture e = am.loadTexture(baseDir != null ? baseDir + east : east);
            Texture n = am.loadTexture(baseDir != null ? baseDir + north : north);
            Texture s = am.loadTexture(baseDir != null ? baseDir + south : south);
            Texture u = am.loadTexture(baseDir != null ? baseDir + up : up);
            Texture d = am.loadTexture(baseDir != null ? baseDir + down : down);
            sky = SkyFactory.createSky(am, w, e, n, s, u, d);
            sky.setCullHint(Spatial.CullHint.Never);
            sky.setQueueBucket(RenderQueue.Bucket.Sky);
        }
        scene.addSceneObject(sky);
    }

    @Override
    public void cleanup() {
        if (scene != null) {
            scene.removeSceneObject(sky);
        }
        super.cleanup(); 
    }
    
}
