/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.effect;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import name.huliqing.core.LY;
import name.huliqing.core.constants.MaterialConstants;
import name.huliqing.core.constants.ModelConstants;
import name.huliqing.core.constants.TextureConstants;
import name.huliqing.core.data.EffectData;
import name.huliqing.core.loader.Loader;

/**
 *
 * @author huliqing
 */
public class SlideColorEffect extends AbstractEffect {
    
    private ColorRGBA startColor = new ColorRGBA(1, 1, 1, 1);
    private ColorRGBA endColor = new ColorRGBA(0, 0, 3, 1);
    
    private String mask = TextureConstants.TEX_MASK;
    private boolean maskAnimY;
    private boolean maskAnimX;
    private boolean maskChangeDir;
    private float maskScaleY = 1;
    private float maskScaleX = 1;
    private float maskSpeed = 1;
    private WrapMode maskWrap = WrapMode.EdgeClamp;
    
    private String tex = TextureConstants.TEX_PARTICLES;
    private boolean texAnimY;
    private boolean texAnimX;
    private boolean texChangeDir;
    private float texScaleY = 1;
    private float texScaleX = 1;
    private float texSpeed = 1;
    private Texture.WrapMode texWrap = WrapMode.MirroredRepeat;
    
    // ---- inner
    private Spatial animObj;
    private Material mat;

    @Override
    public void setData(EffectData data) {
        super.setData(data);
        startColor = data.getAsColor("startColor", startColor);
        endColor = data.getAsColor("endColor", endColor);
        
        mask = data.getAsString("mask", mask);
        maskAnimY = data.getAsBoolean("maskAnimY", maskAnimY);
        maskAnimX = data.getAsBoolean("maskAnimX", maskAnimX);
        maskChangeDir = data.getAsBoolean("maskChangeDir", maskChangeDir);
        maskScaleY = data.getAsFloat("maskScaleY", maskScaleY);
        maskScaleX = data.getAsFloat("maskScaleX", maskScaleX);
        maskSpeed = data.getAsFloat("maskSpeed", maskSpeed);
        maskWrap = getWrapMode(data.getAsString("maskWrap"), maskWrap);
        
        tex = data.getAsString("tex", tex);
        texAnimY = data.getAsBoolean("texAnimY", texAnimY);
        texAnimX = data.getAsBoolean("texAnimX", texAnimX);
        texChangeDir = data.getAsBoolean("texChangeDir", texChangeDir);
        texScaleY = data.getAsFloat("texScaleY", texScaleY);
        texScaleX = data.getAsFloat("texScaleX", texScaleX);
        texSpeed = data.getAsFloat("texSpeed", texSpeed);
        texWrap = getWrapMode(data.getAsString("texWrap"), texWrap);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        create();
    }
    
    private void create() {
        if (mat == null) {
            AssetManager am = LY.getAssetManager();
            mat = new Material(am, MaterialConstants.MAT_SLIDE_COLOR);
            mat.setColor("StartColor", startColor);
            mat.setColor("EndColor", endColor);

            Texture maskMap = am.loadTexture(mask);
            maskMap.setWrap(maskWrap);
            mat.setTexture("MaskMap", maskMap);
            mat.setBoolean("MaskAnimY", maskAnimY);
            mat.setBoolean("MaskAnimX", maskAnimX);
            mat.setBoolean("MaskChangeDir", maskChangeDir);
            mat.setFloat("MaskScaleY", maskScaleY);
            mat.setFloat("MaskScaleX", maskScaleX);
            mat.setFloat("MaskSpeed", maskSpeed);

            Texture texMap = am.loadTexture(tex);
            texMap.setWrap(texWrap);
            mat.setTexture("TexMap", texMap);
            mat.setBoolean("TexAnimY", texAnimY);
            mat.setBoolean("TexAnimX", texAnimX);
            mat.setBoolean("TexChangeDir", texChangeDir);
            mat.setFloat("TexScaleY", texScaleY);
            mat.setFloat("TexScaleX", texScaleX);
            mat.setFloat("TexSpeed", texSpeed);

            mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off); // Allow to see both sides of a face
            mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Additive);
            mat.getAdditionalRenderState().setColorWrite(true);
            mat.getAdditionalRenderState().setDepthTest(true);
            mat.getAdditionalRenderState().setDepthWrite(false);
        }
        if (animObj == null) {
            animObj = loadAnimModel();
            animObj.setMaterial(mat);
            animObj.setQueueBucket(Bucket.Transparent);
            animRoot.attachChild(animObj);
        }
    }
    
    /**
     * 载入
     * @return 
     */
    protected Spatial loadAnimModel() {
        return Loader.loadModel(ModelConstants.MODEL_SLIDE_COLOR);
    }
    
    private WrapMode getWrapMode(String name, WrapMode defValue) {
        if (name == null) {
            return defValue;
        }
        for (WrapMode wm : WrapMode.values()) {
            if (wm.name().equals(name)) {
                return wm;
            }
        }
        return defValue;
    }
    
    
}
