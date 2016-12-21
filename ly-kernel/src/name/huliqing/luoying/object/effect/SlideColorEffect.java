/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.object.effect;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.constants.AssetConstants;
import name.huliqing.luoying.data.EffectData;
import name.huliqing.luoying.object.Loader;

/**
 *
 * @author huliqing
 */
public class SlideColorEffect extends Effect {
    
    private ColorRGBA startColor = new ColorRGBA(1, 1, 1, 1);
    private ColorRGBA endColor = new ColorRGBA(0, 0, 3, 1);
    
    private String mask = AssetConstants.TEXTURE_MASK;
    private boolean maskAnimY;
    private boolean maskAnimX;
    private boolean maskChangeDir;
    private float maskScaleY = 1;
    private float maskScaleX = 1;
    private float maskSpeed = 1;
    private WrapMode maskWrap = WrapMode.EdgeClamp;
    
    private String tex = AssetConstants.TEXTURE_PARTICLES;
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
    public void initEntity() {
        super.initEntity();
        create();
    }
    
    private void create() {
        if (mat == null) {
            AssetManager am = LuoYing.getAssetManager();
            mat = new Material(am, AssetConstants.MATERIAL_SLIDE_COLOR);
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
            mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
            mat.getAdditionalRenderState().setColorWrite(true);
            mat.getAdditionalRenderState().setDepthTest(true);
            mat.getAdditionalRenderState().setDepthWrite(false);
        }
        if (animObj == null) {
            animObj = loadAnimModel();
            animObj.setMaterial(mat);
            // animObj有可能是从模型(j3o)中载入的，bucket有可能不是Inherit, 这里要确保inherit,
            // 以便交给effect父节点去统一设置Bucket,对于大部分效果来说，bucket应该都是Translucent.
            animObj.setQueueBucket(RenderQueue.Bucket.Inherit);  
            animNode.attachChild(animObj);
        }
    }
    
    /**
     * 载入
     * @return 
     */
    protected Spatial loadAnimModel() {
        return Loader.loadModel(AssetConstants.MODEL_SLIDE_COLOR);
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
