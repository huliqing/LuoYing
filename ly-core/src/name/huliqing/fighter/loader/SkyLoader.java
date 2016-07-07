/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import name.huliqing.fighter.Common;

/**
 * @author huliqing
 */
public class SkyLoader {
    
    // 不能使用"default"的sky(1024*1024)，这可能会导致部分小内存的手机内存溢出。
    private final static String FOLDER = "default_min";
    
    // 必须缓存的sky
    private static Spatial cacheSkyDefaut;
    private static Spatial cacheSkyTiny;
    
    /**
     * @deprecated 不再使用
     * @return 
     */
    public static Spatial loadDefaultMin() {
        // 注意： sky必须进行缓存，并进行通用。SkyFactory在创建sky后即使从父节点中detach
        // 仍然无法释放内存，该问题会导致每一次创建Sky后内存中的Textures(M)数会一直累加
        // 直到内存溢出，该问题在性能比较低的手机系统上会很明显。
        // 如：每次从start界面进入play界面时，初始会scene并创建sky,如果sky不进行缓存，
        // 则Texture(M)会一直增加，直到OOM.
        if (cacheSkyDefaut == null) {
            AssetManager am = Common.getAssetManager();
            Texture east = am.loadTexture("Textures/tex/sky/" + FOLDER + "/east.jpg");
            Texture west = am.loadTexture("Textures/tex/sky/" + FOLDER + "/west.jpg");
            Texture south = am.loadTexture("Textures/tex/sky/" + FOLDER + "/south.jpg");
            Texture north = am.loadTexture("Textures/tex/sky/" + FOLDER + "/north.jpg");
            Texture up = am.loadTexture("Textures/tex/sky/" + FOLDER + "/up.jpg");
            Texture down = am.loadTexture("Textures/tex/sky/" + FOLDER + "/down.jpg");
            cacheSkyDefaut = SkyFactory.createSky(am, west, east, north, south, up, down);
            cacheSkyDefaut.setCullHint(Spatial.CullHint.Never);
            cacheSkyDefaut.setQueueBucket(RenderQueue.Bucket.Sky);
        }
        return cacheSkyDefaut;
    }
    
    /**
     * 不再使用
     * @deprecated 
     * @return 
     */
    public static Spatial loadTinySky() {
        if (cacheSkyTiny == null) {
            AssetManager am = Common.getAssetManager();
            Texture east = am.loadTexture("Textures/tex/sky/default_tiny/east.jpg");
            Texture west = am.loadTexture("Textures/tex/sky/default_tiny/west.jpg");
            Texture south = am.loadTexture("Textures/tex/sky/default_tiny/south.jpg");
            Texture north = am.loadTexture("Textures/tex/sky/default_tiny/north.jpg");
            Texture up = am.loadTexture("Textures/tex/sky/default_tiny/up.jpg");
            Texture down = am.loadTexture("Textures/tex/sky/default_tiny/down.jpg");
            cacheSkyTiny = SkyFactory.createSky(am, west, east, north, south, up, down);
            cacheSkyTiny.setCullHint(Spatial.CullHint.Never);
            cacheSkyTiny.setQueueBucket(RenderQueue.Bucket.Sky);
        }
        return cacheSkyTiny;
    }
}
