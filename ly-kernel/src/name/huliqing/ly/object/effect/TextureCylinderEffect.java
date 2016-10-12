/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.effect;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;
import name.huliqing.ly.data.EffectData;
import name.huliqing.ly.data.EntityData;
import name.huliqing.ly.object.scene.Scene;
import name.huliqing.ly.utils.MatUtils;

/**
 * 贴图材质特效，支持用一幅图像作为特效,贴图是贴在一个圆筒状的柱体上的。
 * 默认情况下，圆柱立于地面上（即与y轴同方向），贴图的正面朝向Z方向.
 * 可设置圆柱的初始位置在不同的轴向上
 * @author huliqing
 */
public class TextureCylinderEffect extends Effect {
    // 贴图路径
    private String texture = "Textures/tex/effect/vortex.jpg";
    // 圆柱所在轴向:x/y/z
    private String axis = "y";
    // 圆柱半径
    private float radius = 2.0f;
    // 圆柱高度
    private float height = 5.0f;
    // 圆柱偏移
    private Vector3f offset;
    // 贴图的颜色。
    private ColorRGBA color = ColorRGBA.White.clone();
    
    // ---- inner
    // 圆柱
    private Spatial root;
    
    @Override
    public void setData(EffectData data) {
        super.setData(data); 
        this.texture = data.getAsString("texture", texture);
        this.axis = data.getAsString("axis", axis);
        this.radius = data.getAsFloat("radius", radius);
        this.height = data.getAsFloat("height", height);
        this.offset = data.getAsVector3f("offset");
        this.color = data.getAsColor("color", color);
    }
    
    private void create() {
        Material mat = MatUtils.createTransparent(texture);
        mat.setColor("Color", color);
        
        Cylinder cylinder = new Cylinder(2, 16, radius, height);
        root = new Geometry("TextureCylinderEffect_root", cylinder);
        root.setMaterial(mat);
        root.setQueueBucket(Bucket.Transparent);
        
        // 默认贴图在xy平面上，当指定了其它方向时需要进行旋转，默认以逆时针旋转到指定平面
        if ("x".equals(axis)) {
            root.rotate(0, FastMath.HALF_PI, 0);
        } else if ("y".equals(axis)) {
            root.rotate(FastMath.HALF_PI, FastMath.HALF_PI, 0);
        } else if ("z".equals(axis)) {
            root.rotate(0, 0, -FastMath.HALF_PI);
        }
        
        if (offset != null) {
            root.setLocalTranslation(offset);
        }
        
        animRoot.attachChild(root);
    }
    
    @Override
    public void initialize(Scene scene) {
        super.initialize(scene);
        
        if (root == null) {
            create();
        }
    }

    
    
}
