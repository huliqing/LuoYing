/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.bullet;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import com.jme3.util.TempVars;
import name.huliqing.core.LY;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.MaterialConstants;
import name.huliqing.core.data.BulletData;
import name.huliqing.core.game.service.PlayService;
import name.huliqing.core.shape.SplineSurface;
import name.huliqing.core.utils.MathUtils;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class CurveTrailBullet<T extends BulletData> extends CurveBullet<T> {
    private final PlayService playService = Factory.get(PlayService.class);
    
    // 拖尾的宽度
    private float width = 0.3f;
    // 分段数，越精细越平滑
    private int segments = 50;
    // 维度，越多维实体感越好，但是网格数越多
    private int dimension = 2;
    // 遮罩贴图
    private String mask;
    // 材质及缩放值
    private String tex;
    private float texScale = 1.0f;
    private ColorRGBA color;
    
    // ---- inner
    private Material mat;
    private final Node surface = new Node();

    @Override
    public void setData(T data) {
        super.setData(data);
        this.mask = data.getAttribute("mask", null);
        this.tex = data.getAttribute("tex", null);
        this.texScale = data.getAsFloat("texScale", texScale);
        this.color = data.getAsColor("color", ColorRGBA.White);
        this.width = data.getAsFloat("width", width);
        this.dimension = data.getAsInteger("dimension", dimension);
        this.segments = data.getAsInteger("segments", segments);
    }

    @Override
    protected void doInit() {
        super.doInit();
        surface.detachAllChildren();
        
        AssetManager am = LY.getAssetManager();
        if (mat == null) {
            mat = new Material(LY.getAssetManager(), MaterialConstants.MAT_SLIDE_TRAIL);
            mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off); // Allow to see both sides of a face
            mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Additive);
            mat.getAdditionalRenderState().setDepthTest(true);
            mat.getAdditionalRenderState().setDepthWrite(false);
            final Texture maskMap = am.loadTexture(mask);
            final Texture texMap = am.loadTexture(tex);
            
            mat.setTexture("MaskMap", maskMap);
            mat.setTexture("TexMap", texMap);
            mat.setFloat("TexScale", texScale);
            mat.setColor("Color", color);
        }
        mat.setFloat("Interpolation", 0);
        
        // 如果没有spline，即可能没有设置中间路径点，这时需要自己创建一个spline
        Spline spline = getSpline();
        if (spline == null) {
            spline = new Spline();
            spline.getControlPoints().add(startPoint);
            spline.addControlPoint(endPoint);
            spline.setCurveTension(0);
        }
        
        // 计算出实际的维度旋转轴
        TempVars tv = TempVars.get();
        Vector3f nextPoint = tv.vect1;
        Vector3f forward = tv.vect2;
        Vector3f left = tv.vect3;
        Vector3f rotAxis = tv.vect4;
        MathUtils.getSplinePoint(spline, spline.getTotalLength() / segments, nextPoint);
        nextPoint.subtract(spline.getControlPoints().get(0), forward).normalizeLocal();
        up.cross(forward, left).normalizeLocal();
        left.cross(up, rotAxis).normalizeLocal();

        Quaternion qua = new Quaternion();
        Vector3f tempUp = tv.vect5;
        float angle = FastMath.PI / dimension;
        for (int i = 0; i < dimension; i++) {
            qua.fromAngleAxis(angle * i, rotAxis);
            qua.mult(up, tempUp);
            surface.attachChild(createSurface(spline, width, segments, tempUp));
        }
        tv.release();
        
        // 添加到场景
        playService.addObject(surface);
    }

    @Override
    public void cleanup() {
        surface.removeFromParent();
        super.cleanup();
    }

    @Override
    protected void doUpdatePosition(float tpf, Vector3f endPos) {
//        Logger.getLogger(getClass().getName()).log(Level.INFO, "=Interpolation={0}", inter);
        super.doUpdatePosition(tpf, endPos);

        float inter = getInterpolation(endPos) - 0.1f;
        if (inter >= 0.9f) {
            // 结束
            mat.setFloat("Interpolation", 0);
            surface.removeFromParent();
        } else {
            mat.setFloat("Interpolation", inter);
        }
    }
    
    private Geometry createSurface(Spline spline, float width, int segments, Vector3f up) {
        SplineSurface ssShape = new SplineSurface(spline, width, segments, up);
        Geometry geometry = new Geometry("CTB", ssShape);
        geometry.setMaterial(mat);
        geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        return geometry;
    }
}
