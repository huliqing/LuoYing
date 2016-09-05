/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.effect;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Curve;
import name.huliqing.core.object.anim.AnimationControl;
import name.huliqing.core.object.anim.Loop;
import name.huliqing.core.object.anim.RotationAnim;
import name.huliqing.core.shape.QuadXYC;
import name.huliqing.core.utils.MatUtils;

/**
 * 渲染一个在XZ平面上的圆环及附着在该环上的一个星光
 * @author huliqing
 */
public class HaloCircle extends Node {
    
    // 星光的半径
    private float radius;
    // 星光材质
    private String texture;
    // 星光大小
    private Vector3f haloSize = new Vector3f(1, 1, 1);
    // 星光颜色
    private ColorRGBA haloColor = ColorRGBA.White.clone();
    // 是否显示线条
    private boolean showLine;
    // 线条颜色
    private ColorRGBA lineColor = new ColorRGBA(0.8f, 0.8f, 1, 1f);
    
    // ---- 内部
    private Node localRoot;
    // 圆环
    private Spatial circle;
    // 星光
    private Spatial halo;
    // 旋转控制
    private RotationAnim rotationAnim;
    
    public HaloCircle(float radius
            , String texture
            , Vector3f haloSize
            , ColorRGBA haloColor
            , boolean showLine
            , ColorRGBA lineColor
                    ) {
        this.radius = radius;
        this.texture = texture;
        this.haloSize.set(haloSize != null ? haloSize : this.haloSize);
        this.haloColor.set(haloColor != null ? haloColor : this.haloColor);
        this.showLine = showLine;
        this.lineColor.set(lineColor != null ? lineColor : this.lineColor);
        
        create();
    }
    
    public void setRotateSpeed(float speed) {
        rotationAnim.setSpeed(speed);
    }
    
    public float getRotateSpeed() {
        return rotationAnim.getSpeed();
    }
    
    /**
     * 让星光旋转，默认情况下星光是不会旋转的，只有启动后才会旋转
     * @param bool 
     */
    public void startRotate(boolean bool) {
        if (bool) {
            if (!rotationAnim.isEnd()) {
                return;
            }
            rotationAnim.start();
        } else {
            if (!rotationAnim.isEnd()) {
                rotationAnim.cleanup();
            }
        }
    }
    
    /**
     * 创建一个星光,该方法必须在所有设置完之后再调用，否则一些后续的设置将对
     * 已经创建的星光不会生效。
     */
    private void create() {
        localRoot = new Node("HaloCircle_localRoot");
        attachChild(localRoot);
        
        // 创建星光
        halo = createHalo(texture, radius, haloSize, haloColor);
        localRoot.attachChild(halo);
        
        // 创建线圈
        if (showLine) {
            circle = createCircle(radius, lineColor);
            localRoot.attachChild(circle);
        }
        
        // 创建旋转动画控制
        rotationAnim = createAnim();
        localRoot.addControl(new AnimationControl(rotationAnim));
    }
    
    private RotationAnim createAnim() {
        RotationAnim ra = new RotationAnim();
        ra.setAngleDegree(360);
        ra.setAxis(Vector3f.UNIT_Y);
        ra.setLoop(Loop.loop);
        return ra;
    }
    
    private Spatial createHalo(String texture, float radius, Vector3f size, ColorRGBA color) {
        QuadXYC quad = new QuadXYC(size.x, size.y);
        Geometry geo = new Geometry("HaloCircle_halo", quad);
        Material mat = MatUtils.createTransparent(texture);
        mat.setColor("Color", color);
        geo.setMaterial(mat);
        geo.setQueueBucket(Bucket.Transparent);
        geo.setLocalTranslation(0, 0, radius);
        return geo;
    }
    
    /**
     * 创建一个在xz平面上的圆环
     * @param radius 圆环的半径
     * @return 
     */
    private Spatial createCircle(float radius, ColorRGBA lineColor) {
        Spline sp = new Spline();
        sp.setCycle(true);
        sp.addControlPoint(new Vector3f(radius, 0, 0));
        sp.addControlPoint(new Vector3f(0, 0, -radius));
        sp.addControlPoint(new Vector3f(-radius, 0, 0));
        sp.addControlPoint(new Vector3f(0, 0, radius));
        sp.setCurveTension(0.83f);
        Geometry geo = new Geometry("HaloCircle_circle", new Curve(sp, 10));
        Material mat = MatUtils.createWireFrame(lineColor);
        geo.setMaterial(mat);
        return geo;
    }
    
}
