/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.effect;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import name.huliqing.luoying.data.EffectData;
import name.huliqing.luoying.shape.QuadXYC;
import name.huliqing.luoying.utils.MaterialUtils;
import name.huliqing.luoying.utils.MathUtils;

/**
 * 贴图材质特效，支持用一幅图像作为特效,默认的贴图在xy平面上，并且原点在图
 * 片的中心点上。通过plane属性来指定不同的平面。
 * @author huliqing
 */
public class TextureEffect extends Effect {
    // 贴图路径
    private String texture;
    // 贴图的大小，只有xy有效。
    private Vector3f size = new Vector3f(5,5,1);
    // 指定贴图所在的原始平面：xy\xz\yz
    private String plane = "xy";
    // 贴图的颜色。
    private ColorRGBA color = ColorRGBA.White.clone();
    
    // ---- inner
    private Spatial textureNode;

    @Override
    public void setData(EffectData data) {
        super.setData(data);
        this.texture = data.getAsString("texture", texture);
        this.size = data.getAsVector3f("size", size);
        this.plane = data.getAsString("plane", plane);
        this.color = data.getAsColor("color", color);
    }
    
    @Override
    public void initEntity() {
        super.initEntity();
        Material mat = MaterialUtils.createTransparent(texture);
        mat.setColor("Color", color);
        textureNode = new Geometry("TextureEffect_root", new QuadXYC(size.x, size.y));
        textureNode.setMaterial(mat);
        
        // 默认贴图在xy平面上，当指定了其它方向时需要进行旋转，默认以逆时针旋转到指定平面
        if ("xz".equals(plane)) {
            textureNode.setLocalRotation(MathUtils.createRotation(FastMath.HALF_PI, Vector3f.UNIT_X, textureNode.getLocalRotation()));
        } else if ("yz".equals(plane)) {
            textureNode.setLocalRotation(MathUtils.createRotation(FastMath.HALF_PI, Vector3f.UNIT_Y, textureNode.getLocalRotation()));
        }
        
        animNode.attachChild(textureNode);
    }

    @Override
    public void cleanup() {
        textureNode.removeFromParent();
        super.cleanup();
    }
    
}
