/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.anim;

import com.jme3.material.MatParamOverride;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;
import com.jme3.shader.VarType;
import name.huliqing.luoying.data.AnimData;

/**
 * 改变一个节点的颜色
 * @author huliqing
 */
public class ColorAnim extends AbstractAnim<Spatial> {
//    private final static Logger LOG = Logger.getLogger(ColorAnim.class.getName());
    
    private final ColorRGBA startColor = ColorRGBA.DarkGray.clone();
    private final ColorRGBA endColor = ColorRGBA.White.mult(1.5f);
    private boolean useSine; 
    
    // 用于Unshaded.jm3d
    private final MatParamOverride colorOverride = new MatParamOverride(VarType.Vector4, "Color", ColorRGBA.White.clone());
    // 用于Lighting.j3md
    private final MatParamOverride useMaterialColorOverride = new MatParamOverride(VarType.Boolean, "UseMaterialColors", true);
    private final MatParamOverride ambientOverride = new MatParamOverride(VarType.Vector4, "Ambient", ColorRGBA.White.clone());
    private final MatParamOverride diffuseOverride = new MatParamOverride(VarType.Vector4, "Diffuse", ColorRGBA.White.clone());
    
    private boolean localInit;
    private final ColorRGBA updateColor = new ColorRGBA();
    
    @Override
    public void setData(AnimData data) {
        super.setData(data);
        this.startColor.set(data.getAsColor("startColor", startColor));
        this.endColor.set(data.getAsColor("endColor", endColor));
        this.useSine = data.getAsBoolean("useSine", useSine);
    }

    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("startColor", startColor);
        data.setAttribute("endColor", endColor);
    }
    
    public ColorRGBA getStartColor() {
        return startColor;
    }

    public void setStartColor(ColorRGBA startColor) {
        this.startColor.set(startColor);
    }

    public ColorRGBA getEndColor() {
        return endColor;
    } 

    public void setEndColor(ColorRGBA endColor) {
        this.endColor.set(endColor);
    }
    
    @Override
    protected void doAnimInit() {}
    
    @Override
    protected void doAnimUpdate(float interpolation) {
        // 注：初始化放在这里是特殊情况，在doAnimInit里初始化时，如果是从存档中载入会无法更新颜色。
        // BUG未知.
        if (!localInit) {
            target.addMatParamOverride(colorOverride);
            target.addMatParamOverride(useMaterialColorOverride);
            target.addMatParamOverride(ambientOverride);
            target.addMatParamOverride(diffuseOverride);
            localInit = true;
        }
        
        float inter;
        if (useSine) {
            inter = FastMath.sin(interpolation * FastMath.HALF_PI);
        } else {
            inter = interpolation;
        }
        
        updateColor.set(startColor);
        updateColor.interpolateLocal(endColor, inter);
        updateColor(updateColor);
    }
    
    private void updateColor(ColorRGBA tColor) {
//        LOG.log(Level.INFO, "UpdateColor, color={0}", tColor);
        ((ColorRGBA)colorOverride.getValue()).set(tColor);
        ((ColorRGBA)ambientOverride.getValue()).set(tColor);
        ((ColorRGBA)diffuseOverride.getValue()).set(tColor);
    }
    
    @Override
    public void cleanup() {
        target.removeMatParamOverride(colorOverride);
        target.removeMatParamOverride(useMaterialColorOverride);
        target.removeMatParamOverride(ambientOverride);
        target.removeMatParamOverride(diffuseOverride);
        localInit = false;
        super.cleanup();
    }
    
}
