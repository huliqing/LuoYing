/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.anim;

import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.scene.Spatial;
import com.jme3.shader.VarType;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.AnimData;

/**
 * 改变一个节点的颜色
 * @author huliqing
 */
public class ColorAnim extends AbstractAnim<Spatial> {
//    private final static Logger logger = Logger.getLogger(ColorAnim.class.getName());
    
    private final ColorRGBA startColor = ColorRGBA.DarkGray.clone();
    private final ColorRGBA endColor = ColorRGBA.White.mult(1.5f);
    private boolean useSine;
    private final List<MatParam> mats = new ArrayList<MatParam>();
    
    private final SceneGraphVisitor visitor = new SceneGraphVisitorAdapter() {
        @Override
        public void visit(Geometry geom) {
            Material mat = geom.getMaterial();
            MatParam colorParam = mat.getParam("Color");
            if (colorParam != null && colorParam.getVarType() == VarType.Vector4) {
                mats.add(colorParam);
                return;
            } 
            // 如果mat没有设置Color属性，但是MaterialDef的定义中存在Color属性，则手动
            // 添加一个。
            MatParam colorDef = mat.getMaterialDef().getMaterialParam("Color");
            if (colorDef != null && colorDef.getVarType() == VarType.Vector4) {
                mat.setColor("Color", ColorRGBA.White.clone());
                mats.add(mat.getParam("Color"));
            }
                
        }
    };

    @Override
    public void setData(AnimData data) {
        super.setData(data);
        this.startColor.set(data.getAsColor("startColor", startColor));
        this.endColor.set(data.getAsColor("endColor", endColor));
        this.useSine = data.getAsBoolean("useSine", useSine);
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
    protected void doInit() {
        mats.clear();
        target.breadthFirstTraversal(visitor);
        
        updateColor(startColor);
    }
    
    @Override
    protected void doAnimation(float interpolation) {
        float inter;
        if (useSine) {
            inter = FastMath.sin(interpolation * FastMath.HALF_PI);
        } else {
            inter = interpolation;
        }
        
        TempVars tv = TempVars.get();
        
        ColorRGBA tColor = tv.color.set(startColor);
        tColor.interpolateLocal(endColor, inter);
        updateColor(tColor);
        
        tv.release();
    }
    
    private void updateColor(ColorRGBA tColor) {
        for (MatParam mat : mats) {
            ((ColorRGBA)mat.getValue()).set(tColor);
        }
    }

    @Override
    public void cleanup() {
        mats.clear();
        super.cleanup(); 
    }
    
    
    
}
