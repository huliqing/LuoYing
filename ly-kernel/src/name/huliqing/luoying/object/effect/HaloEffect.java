/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.effect;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import name.huliqing.luoying.data.EffectData;
import name.huliqing.luoying.shape.QuadXY;
import name.huliqing.luoying.utils.MatUtils;
import name.huliqing.luoying.utils.MathUtils;

/**
 * @deprecated 20160202不通用，将不再使用。
 * 光晕效果,在xz面上竖起多束光束。
 * @author huliqing
 */
public class HaloEffect extends Effect {
    
    // 光晕材质贴图
    private String texture = "Textures/effect/spark_v.jpg";
    // 半径
    private float radius = 1.5f;
    // 光晕数量
    private int haloTotal = 7;
    // 光晕图片的缩放大小
    private Vector3f haloSize = new Vector3f(5, 5, 1);
    // 倾斜值,大于0则向上倾斜，小于0则向下倾斜
    private float incline = 0.5f;
    
    // -------- 内部参数
    // 光晕的根节点
    private Node haloRoot;
    
    @Override
    public void setData(EffectData data) {
        super.setData(data); 
        this.texture = data.getProto().getAsString("texture", texture);
        this.radius = data.getProto().getAsFloat("radius", radius);
        this.haloTotal = data.getProto().getAsInteger("haloTotal", haloTotal);
        this.haloSize = data.getProto().getAsVector3f("haloSize", haloSize);
        this.incline = data.getProto().getAsFloat("incline", incline);
    }
    
    @Override
    public void initEntity() {
        super.initEntity();
        create();
    }
    
    private void create() {
        if (haloRoot != null) {
            haloRoot.removeFromParent();
        }
        
        // -- 初始化创建所有光晕
        haloRoot = new Node("CircleHalo_root");
        float angle = FastMath.TWO_PI / haloTotal;
        TempVars tv = TempVars.get();
        Vector3f lookAt = tv.vect1.set(0, incline, 0);
        Vector3f pos = tv.vect2;
        Vector3f size = tv.vect3.set(1,1,1).multLocal(haloSize);
        Quaternion rot = tv.quat1;
        for (int i = 0; i < haloTotal; i++) {
            pos.zero().setX(radius);
            if (i == 0) {
                haloRoot.attachChild(createHaloOne(texture, pos, size, lookAt));
            } else {
                MathUtils.createRotation(angle * i, Vector3f.UNIT_Y, rot);
                rot.mult(pos, pos);
                haloRoot.attachChild(createHaloOne(texture, pos, size, lookAt));
            }
        }
        tv.release();
        this.animNode.attachChild(haloRoot);
    }
    
    private Spatial createHaloOne(String texture, Vector3f pos, Vector3f size, Vector3f lookAt) {
        QuadXY shape = new QuadXY(size.x, size.y);
        Geometry geo = new Geometry("QuadShape_halo", shape);
        geo.setMaterial(MatUtils.createTransparent(texture));
        geo.setLocalTranslation(pos);
        geo.lookAt(lookAt, Vector3f.UNIT_Y);
//        geo.setQueueBucket(Bucket.Transparent); // remove20161004
        geo.setQueueBucket(Bucket.Translucent);

        return geo;
    }
    
}
