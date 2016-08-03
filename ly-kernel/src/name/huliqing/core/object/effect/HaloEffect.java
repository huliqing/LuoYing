/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.effect;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SafeArrayList;
import com.jme3.util.TempVars;
import java.util.List;
import java.util.logging.Logger;
import name.huliqing.core.data.EffectData;
import name.huliqing.core.object.anim.Loop;
import name.huliqing.core.object.anim.ColorAnim;
import name.huliqing.core.object.anim.RotationAnim;
import name.huliqing.core.object.anim.ScaleAnim;
import name.huliqing.core.shape.QuadXY;
import name.huliqing.core.utils.GeometryUtils;
import name.huliqing.core.utils.MatUtils;
import name.huliqing.core.utils.MathUtils;

/**
 * @deprecated 20160202不通用，将不再使用。
 * 光晕效果,在xz面上竖起多束光束。
 * @author huliqing
 */
public class HaloEffect extends AbstractEffect {
    private final static Logger logger = Logger.getLogger(HaloEffect.class.getName());
    
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
    
    // halo是否一个一个出现，否则立即全部出现
    private boolean dynamicShow = true;
    // halo是否反方向呈现（need dynamicShow= true),
    private boolean dynamicInvert = false;
    // halo是否随机出现,如果为true,则dynamicInvert无效(need dynamicShow=true)，
    private boolean dynamicRandom = false;
    
    // 是否让每束光晕在出现时使用缩放动画，即让每束光晕从小到大出现。
    private boolean haloScaleShow = true;
    private Vector3f haloScaleStart = new Vector3f(0.01f, 0.01f, 0.01f);
    private Vector3f haloScaleEnd = new Vector3f(1, 1, 1);
    private float haloScaleSpeed = 2;
    
    // 是否旋转
    private boolean rotate = true;
    // 光晕是否立即旋转,默认false，即在光晕全部出现后再旋转(need rotate = true)
    private boolean rotateImmediate = true;
    // 是否逆向旋转(need rotate = true);
    private boolean rotateInvert = true;
    // 旋转速度,大于1加速，小于1减速.(need rotate=true)
    private float rotateSpeed = 0.1f;
    private Loop rotateLoop = Loop.loop;
    
    // haloRoot的缩放动画,在display 阶段进行缩放动画
    private boolean scale = true;
    private Vector3f scaleStart = new Vector3f(0.9f, 0.9f, 0.9f);
    private Vector3f scaleEnd = new Vector3f(1f, 1f, 1f);
    private float scaleSpeed = 2f;
    private Loop scaleLoop = Loop.cycle;
    
    // 是否起用光晕的颜色动画，如忽亮忽暗，彩用线性渐变
    private boolean color = true;
    private ColorRGBA colorStart = ColorRGBA.DarkGray.clone();
    private ColorRGBA colorEnd = ColorRGBA.White.clone();
    private float colorSpeed = 2f; 
    private Loop colorLoop = Loop.cycle;
    
    // -------- 内部参数
    // 光晕的根节点
    private Node haloRoot;
    
    // 最后一个已显示的halo，dynamicRandom=false时使用，即顺序显示
    private int lastShowIndex = -1;
    // 还未显示的列表，dynamicRandom=true时使用，随机显示
    private List<Spatial> unshowList;
    
    // 控制旋转start和display阶段的旋转
    private RotationAnim rotationAnim;
    // 控制display阶段的缩放
    private ScaleAnim scaleAnim;
    // 控制的发光动画
    private ColorAnim colorAnim;
    // 控制结束时的缩放
    private ScaleAnim closeAnim;
    // 需要重新创建halo？当改变某些参数时需要,或者首次进入时需要
    private boolean needRecreate = true;

    @Override
    public void setData(EffectData data) {
        super.setData(data); 
        this.texture = data.getProto().getAttribute("texture", texture);
        this.radius = data.getProto().getAsFloat("radius", radius);
        this.haloTotal = data.getProto().getAsInteger("haloTotal", haloTotal);
        this.haloSize = data.getProto().getAsVector3f("haloSize", haloSize);
        this.incline = data.getProto().getAsFloat("incline", incline);
        
        this.dynamicShow = data.getProto().getAsBoolean("dynamicShow", dynamicShow);
        this.dynamicInvert = data.getProto().getAsBoolean("dynamicInvert", dynamicInvert);
        this.dynamicRandom = data.getProto().getAsBoolean("dynamicRandom", dynamicRandom);
        
        this.haloScaleShow = data.getProto().getAsBoolean("haloScaleShow", haloScaleShow);
        this.haloScaleStart = data.getProto().getAsVector3f("haloScaleStart", haloScaleStart);
        this.haloScaleEnd = data.getProto().getAsVector3f("haloScaleEnd", haloScaleEnd);
        this.haloScaleSpeed = data.getProto().getAsFloat("haloScaleSpeed", haloScaleSpeed);
        
        this.rotate = data.getProto().getAsBoolean("rotate", rotate);
        this.rotateImmediate = data.getProto().getAsBoolean("rotateImmediate", rotateImmediate);
        this.rotateInvert = data.getProto().getAsBoolean("rotateInvert", rotateInvert);
        this.rotateSpeed = data.getProto().getAsFloat("rotateSpeed", rotateSpeed);
        String tempRotateLoop = data.getProto().getAttribute("rotateLoop");
        if (tempRotateLoop != null) {
            rotateLoop = Loop.identify(tempRotateLoop);
        }
        
        this.scale = data.getProto().getAsBoolean("scale", scale);
        this.scaleStart = data.getProto().getAsVector3f("scaleStart", scaleStart);
        this.scaleEnd = data.getProto().getAsVector3f("scaleEnd", scaleEnd);
        this.scaleSpeed = data.getProto().getAsFloat("scaleSpeed", scaleSpeed);
        String tempScaleLoop = data.getProto().getAttribute("scaleLoop");
        if (tempScaleLoop != null) {
            scaleLoop = Loop.identify(tempScaleLoop);
        }
        
        this.color = data.getProto().getAsBoolean("color", color);
        this.colorStart = data.getProto().getAsColor("colorStart", colorStart);
        this.colorEnd = data.getProto().getAsColor("colorEnd", colorEnd);
        this.colorSpeed = data.getProto().getAsFloat("colorSpeed", colorSpeed);
        String tempColorLoop = data.getProto().getAttribute("colorLoop");
        if (tempColorLoop != null) {
            colorLoop = Loop.identify(tempColorLoop);
        }
    }
    
    private void reCreate() {
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
        this.localRoot.attachChild(haloRoot);
        haloRoot.setCullHint(CullHint.Always);
        
    }
    
    private Spatial createHaloOne(String texture, Vector3f pos, Vector3f size, Vector3f lookAt) {
        QuadXY shape = new QuadXY(size.x, size.y);
        Geometry geo = new Geometry("QuadShape_halo", shape);
        geo.setMaterial(MatUtils.createTransparent(texture));
        geo.setLocalTranslation(pos);
        geo.lookAt(lookAt, Vector3f.UNIT_Y);
        geo.setQueueBucket(Bucket.Transparent);
        
        // 给每束halo添加一个动画控制器,作为conTrol添加，不需要手动更新update
        ScaleAnim scaleControl = new ScaleAnim();
        geo.addControl(scaleControl);

        return geo;
    }
    
    // 立即显示所有光晕
    private void setAllHaloVisible(boolean visible) {
        CullHint ch = visible ? CullHint.Never : CullHint.Always;
        haloRoot.setCullHint(ch);
        List<Spatial> cs = haloRoot.getChildren();
        for (int i = 0; i < cs.size(); i++) {
            cs.get(i).setCullHint(ch);
        }
    }

    @Override
    protected void doInit() {
        super.doInit();
        if (needRecreate) {
            reCreate();
            needRecreate = false;
        }
        
        // -- 先把所有都隐藏,根节点显示
        setAllHaloVisible(false);
        haloRoot.setCullHint(CullHint.Never);
        
        // -- 如果是动态显示，则初始化一些信息
        if (dynamicShow) {
            if (dynamicRandom) {
                // 随机显示
                if (unshowList == null) {
                    unshowList = new SafeArrayList<Spatial>(Spatial.class);
                }
                unshowList.clear();
                unshowList.addAll(haloRoot.getChildren());
            } else {
                // 按顺序显示
                lastShowIndex = dynamicShow ? -1 : haloRoot.getChildren().size() - 1;
            }
        }
        
        // -- 如果要旋转
        if (rotate) {
            if (rotationAnim == null) {
                rotationAnim = new RotationAnim();
            }
            rotationAnim.setTarget(haloRoot);
            rotationAnim.setSpeed(rotateSpeed);
            rotationAnim.setInvert(rotateInvert);
            rotationAnim.setLoop(rotateLoop);
            rotationAnim.start();
        }
        
        if (scale) {
            if (scaleAnim == null) {
                scaleAnim = new ScaleAnim();
            }
            scaleAnim.setTarget(haloRoot);
            scaleAnim.setStartScale(scaleStart);
            scaleAnim.setEndScale(scaleEnd);
            scaleAnim.setLoop(scaleLoop);
            scaleAnim.setSpeed(scaleSpeed);
            scaleAnim.setRestore(true);
            scaleAnim.start();
        }
        
        if (color) {
            if (colorAnim == null) {
                colorAnim = new ColorAnim();
            }
            colorAnim.setTarget(haloRoot);
            colorAnim.setStartColor(colorStart);
            colorAnim.setEndColor(colorEnd);
            colorAnim.setLoop(colorLoop);
            colorAnim.setSpeed(colorSpeed);
            colorAnim.start();
            // 初始化haloRoot开始的颜色,这可以避免ColorAnim开始运行时与haloRoot的初始颜色
            // 差距太大,导致瞬间颜色过渡太大产生不协调
            GeometryUtils.setColor(haloRoot, colorAnim.getStartColor());
        }
        
        // 如果开始了动态显示每一个光晕，则需要先将它们缩在最小
        if (haloScaleShow) {            
            List<Spatial> halos = haloRoot.getChildren();
            for (int i = 0; i < halos.size(); i++) {
                halos.get(i).setLocalScale(haloScaleStart);
            }
        }
        
        // 如果不是动态show或者start阶段时间小于等于0，则应该立即显示全部halo
        // startTime <= 0时，updatePhaseStart方法不会运行
        if (!dynamicShow || data.getPhaseTimeStart() <= 0) {
            List<Spatial> cs = haloRoot.getChildren();
            for (int i = 0; i < cs.size(); i++) {
                showHalo(cs.get(i));
            }
        }
    }
    
    @Override
    protected void updatePhaseAll(float tpf) {
        if (rotate && rotateImmediate) {
            rotationAnim.update(tpf);
        }
    }

    @Override
    protected void updatePhaseStart(float tpf, float interpolation) {
        if (dynamicShow) {
            // 根据插值显示halo
            List<Spatial> halos = haloRoot.getChildren();
            if (dynamicRandom) {
                if (unshowList.size() > 0) {
                    int totalSizeShow = (int) (interpolation / (1.0f / haloTotal)) + 1; // 当前需要显示的总数
                    int showNow = halos.size() - unshowList.size(); // 已经显示数
                    int needShow = totalSizeShow - showNow; // 需要显示数
                    for (int i = 0; i < needShow; i++) {
                        Spatial halo = unshowList.remove(FastMath.nextRandomInt(0, unshowList.size() - 1));
                        showHalo(halo);
                        if (unshowList.size() <= 0) {
                            break;
                        }
                    }
                }
            } else {
                // 按顺序出现，index为需要显示到的目标索引
                int index = (int) (interpolation / (1.0f / haloTotal)); // 当前要显示的是第几个halo
//                logger.log(Level.INFO, "last show index=" + lastShowIndex + ", index=" + index + ",interpolation=" + interpolation);
                if (index > lastShowIndex) {
                    while (lastShowIndex < halos.size() - 1 && lastShowIndex < index) {
                        lastShowIndex++;
                        showHalo(halos.get(dynamicInvert ? (halos.size() - 1 - lastShowIndex) : lastShowIndex));
                    }
                }                
            }
        }
        
        // move to doInit 避免start阶段<=0 时updatePhaseStart没有运行的问题
//        else {
//            // 立即显示全部halo
//            List<Spatial> cs = haloRoot.getChildren();
//            for (int i = 0; i < cs.size(); i++) {
//                showHalo(cs.get(i));
//            }
//        }
    }
    
    /**
     * 动画：显示halo，即渲染单个halo的过程
     * @param halo 
     */
    protected void showHalo(Spatial halo) {
        halo.setCullHint(CullHint.Never);
        if (haloScaleShow) {
            // 起始动画控制
            ScaleAnim sa = halo.getControl(ScaleAnim.class);
            sa.setStartScale(haloScaleStart);
            sa.setEndScale(haloScaleEnd);
            sa.setSpeed(haloScaleSpeed);
            sa.start();            
        }
    }

    @Override
    protected void updatePhaseDisplay(float tpf, float phaseTime) {
//        logger.log(Level.INFO, "updatePhaseDisplay, phaseTime={0}", phaseTime);
        if(rotate && !rotateImmediate) {
            rotationAnim.update(tpf);
        }
        if (scale) {
            scaleAnim.update(tpf);
        }
        if (color) {
            colorAnim.update(tpf);
        }
    }

    @Override
    protected void updatePhaseEnd(float tpf, float interpolation) {
//        logger.log(Level.INFO, "updatePhaseEnd, phaseTime={0}", phaseTime);
        if (closeAnim == null) {
            closeAnim = new ScaleAnim();
        }
        closeAnim.setTarget(haloRoot);
        closeAnim.setStartScale(haloRoot.getLocalScale());
        closeAnim.setEndScale(0.05f);
        closeAnim.setRestore(true);
        closeAnim.display(interpolation); // 直接插值显示
    }

    @Override
    public void cleanup() {
        if (rotationAnim != null && !rotationAnim.isEnd()) {
            rotationAnim.cleanup();
        }
        if (scaleAnim != null && !scaleAnim.isEnd()) {
            scaleAnim.cleanup();
        }
        if (colorAnim != null && !colorAnim.isEnd()) {
            colorAnim.cleanup();
        }
        if (closeAnim != null) {
            closeAnim.cleanup();// 确保haloRoot的scale还原
        }
        haloRoot.setCullHint(CullHint.Always);
        super.cleanup(); 
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
        needRecreate = true;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getHaloTotal() {
        return haloTotal;
    }

    public void setHaloTotal(int haloTotal) {
        this.haloTotal = haloTotal;
        needRecreate = true;
    }

    public Vector3f getHaloScale() {
        return haloSize;
    }

    public void setHaloScale(Vector3f haloScale) {
        this.haloSize.set(haloScale);
        needRecreate = true;
    }

    public float getIncline() {
        return incline;
    }

    public void setIncline(float incline) {
        this.incline = incline;
        needRecreate = true;
    }

    public boolean isDynamicShow() {
        return dynamicShow;
    }

    public void setDynamicShow(boolean dynamicShow) {
        this.dynamicShow = dynamicShow;
    }

    public boolean isDynamicInvert() {
        return dynamicInvert;
    }

    public void setDynamicInvert(boolean dynamicInvert) {
        this.dynamicInvert = dynamicInvert;
    }

    public boolean isDynamicRandom() {
        return dynamicRandom;
    }

    public void setDynamicRandom(boolean dynamicRandom) {
        this.dynamicRandom = dynamicRandom;
    }

    public boolean isRotate() {
        return rotate;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }

    public boolean isRotateImmediate() {
        return rotateImmediate;
    }

    public void setRotateImmediate(boolean rotateImmediate) {
        this.rotateImmediate = rotateImmediate;
    }

    public boolean isRotateInvert() {
        return rotateInvert;
    }

    public void setRotateInvert(boolean rotateInvert) {
        this.rotateInvert = rotateInvert;
    }

    public float getRotateSpeed() {
        return rotateSpeed;
    }

    public void setRotateSpeed(float rotateSpeed) {
        this.rotateSpeed = rotateSpeed;
    }

    /**
     * 是否打开了颜色变换动画
     * @return 
     */
    public boolean isColor() {
        return color;
    }

    /**
     * 设置是否使用颜色动画
     * @param color 
     */
    public void setColor(boolean color) {
        this.color = color;
    }

    public ColorRGBA getColorStart() {
        return colorStart;
    }

    public void setColorStart(ColorRGBA colorStart) {
        this.colorStart.set(colorStart);
    }

    public ColorRGBA getColorEnd() {
        return colorEnd;
    }

    public void setColorEnd(ColorRGBA colorEnd) {
        this.colorEnd.set(colorEnd);
    }

    public boolean isScale() {
        return scale;
    }

    public void setScale(boolean scale) {
        this.scale = scale;
    }

    public Vector3f getScaleStart() {
        return scaleStart;
    }

    public void setScaleStart(Vector3f scaleStart) {
        this.scaleStart.set(scaleStart);
    }

    public Vector3f getScaleEnd() {
        return scaleEnd;
    }

    public void setScaleEnd(Vector3f scaleEnd) {
        this.scaleEnd.set(scaleEnd);
    }

    public float getScaleSpeed() {
        return scaleSpeed;
    }

    public void setScaleSpeed(float scaleSpeed) {
        this.scaleSpeed = scaleSpeed;
    }

    public boolean isHaloScaleShow() {
        return haloScaleShow;
    }

    public void setHaloScaleShow(boolean haloScaleShow) {
        this.haloScaleShow = haloScaleShow;
    }

    public Vector3f getHaloScaleStart() {
        return haloScaleStart;
    }

    public void setHaloScaleStart(Vector3f haloScaleStart) {
        this.haloScaleStart.set(haloScaleStart);
    }

    public Vector3f getHaloScaleEnd() {
        return haloScaleEnd;
    }

    public void setHaloScaleEnd(Vector3f haloScaleEnd) {
        this.haloScaleEnd.set(haloScaleEnd);
    }

    public float getHaloScaleSpeed() {
        return haloScaleSpeed;
    }

    public void setHaloScaleSpeed(float haloScaleSpeed) {
        this.haloScaleSpeed = haloScaleSpeed;
    }
    
    
}
