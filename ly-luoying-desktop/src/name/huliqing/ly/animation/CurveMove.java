/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.animation;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;

/**
 * 曲线的运动效果
 * @author huliqing
 */
public class CurveMove extends SimpleMotion {

    private float sineFactor;
    private float height;
    private Vector3f originScale;
    
    public void setHeight(float height) {
        this.height = height;
    }
    
    @Override
    protected void doInit() {
        super.doInit(); 
        // 记住原始缩放
        if (useScale) {
            if (originScale == null) {
                originScale = new Vector3f();
            }
            originScale.set(target.getLocalScale());
        }
    }
    
    @Override
    protected void doMotion(Spatial ui, float factor) {
        sineFactor = FastMath.sin(factor * FastMath.PI);
        TempVars tv = TempVars.get();
        tv.vect1.set(startPos);
        tv.vect1.interpolateLocal(endPos, factor);
        tv.vect1.setY(tv.vect1.y + height * sineFactor);
        ui.setLocalTranslation(tv.vect1);
        tv.release();
    }

    @Override
    protected void doAlpha(Spatial ui, float factor) {
        // unsupported
    }

    @Override
    public void cleanup() {
        if (useScale) {
            TempVars tv = TempVars.get();
            tv.vect1.set(originScale).multLocal(endScale);
            target.setLocalScale(tv.vect1);
            tv.release();
        }
        super.cleanup();
    }

    @Override
    protected void doScale(Spatial spatial, float factor) {
        float scale = FastMath.interpolateLinear(factor, startScale, endScale);
        TempVars tv = TempVars.get();
        tv.vect1.set(originScale).multLocal(scale);
        spatial.setLocalScale(tv.vect1);
        tv.release();
    }

    
}
