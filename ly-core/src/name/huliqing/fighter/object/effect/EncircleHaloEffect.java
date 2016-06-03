/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.effect;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.data.EffectData;
import name.huliqing.fighter.object.anim.Loop;
import name.huliqing.fighter.object.anim.RandomRotationAnim;
import name.huliqing.fighter.object.anim.ScaleAnim;
import name.huliqing.fighter.utils.MathUtils;

/**
 * @deprecated 20160202
 * 一个由几个星光环绕着目标旋转的特效。
 * @author huliqing
 */
public class EncircleHaloEffect extends AbstractEffect {

    // 星光图片
    private String texture = "Textures/effect/halo_s.jpg";
    // 星光数量
    private int size = 7;
    private float radius = 2;
    // 星光的大小
    private Vector3f haloSize = new Vector3f(1,1,1);
    // 星光的颜色
    private ColorRGBA haloColor = new ColorRGBA(1,1,1,1);
    // 是否显示星光的环绕线
    private boolean showLine = true;
    private ColorRGBA lineColor = new ColorRGBA(0.8f, 0.8f, 1, 1f);
    // 整个星光环的位置偏移。
    private Vector3f offset = new Vector3f();
    
    // 是否缩放显示整个光环
    private boolean scaleShow = true;
    private Vector3f scaleStart = new Vector3f(0.01f, 0.01f, 0.01f);
    private Vector3f scaleEnd = new Vector3f(1, 1, 1);
    
    // ---- inner
    private Node root;
    // 对所有已经创建的星光环的引用
    private List<HaloCircle> circles = new ArrayList<HaloCircle>(size);
    // 用于把localRoot缩放出来
    private ScaleAnim scaleAnim;

    @Override
    public void initData(EffectData data) {
        super.initData(data);
        texture = data.getProto().getAttribute("texture", texture);
        size = data.getProto().getAsInteger("size", size);
        radius = data.getProto().getAsFloat("radius", radius);
        haloSize = data.getProto().getAsVector3f("haloSize", haloSize);
        haloColor = data.getProto().getAsColor("haloColor", haloColor);
        showLine = data.getProto().getAsBoolean("showLine", showLine);
        lineColor = data.getProto().getAsColor("lineColor", lineColor);
        offset = data.getProto().getAsVector3f("offset", offset);
        
        scaleShow = data.getProto().getAsBoolean("scaleShow", scaleShow);
        scaleStart = data.getProto().getAsVector3f("scaleStart", scaleStart);
        scaleEnd = data.getProto().getAsVector3f("scaleEnd", scaleEnd);
        preCreate();
    }
    
    @Override
    protected void doInit() {
        super.doInit();
        // 重置速度
        for (HaloCircle hc : circles) {
            hc.setRotateSpeed(FastMath.nextRandomFloat() * 0.5f + 0.5f);
        }
    }
    
    private void preCreate() {
        root = new Node("EncircleHalo_localRoot");
        root.setLocalTranslation(offset);
        localRoot.attachChild(root);
        
        // ---- 创建星光
        circles.clear();
        float avgAngle = FastMath.TWO_PI / size; // 每个环要旋转的弧度
        TempVars tv = TempVars.get();
        for (int i = 0; i < size; i++) {
            // 逐个创建星光
            HaloCircle hc = new HaloCircle(radius, texture, haloSize, haloColor, showLine, lineColor);
            circles.add(hc);
            
            // 初始化星光的旋转位置及旋转速度
            MathUtils.createRotation(avgAngle * i, Vector3f.UNIT_Z, tv.quat1);
            hc.setLocalRotation(tv.quat1);
            hc.startRotate(true);
            
            // 星光的随机旋转
            RandomRotationAnim rra = new RandomRotationAnim();
            rra.setLoop(Loop.loop);
            rra.setSpeed(0.5f);
            hc.addControl(rra);
            root.attachChild(hc);
            rra.start();
        }
        tv.release();
        
        // 创建缩放动画
        if (scaleShow) {
            scaleAnim = new ScaleAnim();
            scaleAnim.setStartScale(scaleStart);
            scaleAnim.setEndScale(scaleEnd);
            scaleAnim.setTarget(root);
        }
    }

    @Override
    protected void updatePhaseAll(float tpf) {
    }

    @Override
    protected void updatePhaseStart(float tpf, float interpolation) {
        if (scaleShow) {
            scaleAnim.display(interpolation);
        }
    }

    @Override
    protected void updatePhaseDisplay(float tpf, float phaseTime) {
        for (HaloCircle hc : circles) {
            hc.setRotateSpeed(hc.getRotateSpeed() + phaseTime * tpf);
        }
    }

    @Override
    protected void updatePhaseEnd(float tpf, float interpolation) {
        if (scaleShow) {
            scaleAnim.display(1 - interpolation);
        }
    }
    
    @Override
    public void cleanup() {
        if (!scaleAnim.isEnd()) {
            scaleAnim.cleanup();
        }
        super.cleanup();
    }
    
    
}
